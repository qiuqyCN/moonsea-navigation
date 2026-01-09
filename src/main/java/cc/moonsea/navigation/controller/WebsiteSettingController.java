package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.entity.FriendLink;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.entity.WebsiteSetting;
import cc.moonsea.navigation.service.FriendLinkService;
import cc.moonsea.navigation.service.UserService;
import cc.moonsea.navigation.service.WebsiteSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class WebsiteSettingController {

    private final WebsiteSettingService websiteSettingService;
    private final UserService userService;
    private final FriendLinkService friendLinkService;

    // 文件上传目录
    private final String uploadDir = "uploads";

    @GetMapping("/setting")
    public String getWebsiteSettingPage(Model model, Authentication authentication, @RequestParam(required = false) String showAuth) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        websiteSettingService.getWebsiteSettingByUserId(user.getId())
                .ifPresent(setting -> model.addAttribute("setting", setting));

        model.addAttribute("showAuth", "true".equals(showAuth)); // 只有当参数值为"true"时才显示认证按钮
        return "admin/setting";
    }

    @GetMapping("/api/setting")
    @ResponseBody
    public ResponseEntity<?> getWebsiteSetting(Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            return websiteSettingService.getWebsiteSettingByUserId(user.getId())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.ok(new WebsiteSetting()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/setting")
    @ResponseBody
    public ResponseEntity<?> saveWebsiteSetting(@RequestBody WebsiteSetting setting, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 设置用户ID
            setting.setUserId(user.getId());

            // 设置ID
            websiteSettingService.getWebsiteSettingByUserId(user.getId())
                    .ifPresent(s -> setting.setId(s.getId()));

            WebsiteSetting savedSetting = websiteSettingService.saveWebsiteSetting(setting);
            return ResponseEntity.ok(savedSetting);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/upload")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file,
                                          @RequestParam("type") String type,
                                          Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证用户
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要上传的文件");
                return response;
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (null != contentType && !contentType.startsWith("image/")) {
                response.put("success", false);
                response.put("message", "只支持图片文件上传");
                return response;
            }

            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // 生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + fileExtension;

            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            // 返回文件URL
            String fileUrl = "/uploads/" + uniqueFileName;
            response.put("success", true);
            response.put("url", fileUrl);
            response.put("message", "上传成功");

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "上传失败：" + e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/api/file")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@RequestParam("url") String url,
                                       Authentication authentication) {
        try {
            // 验证用户
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 验证URL格式，确保安全
            if (url == null || !url.startsWith("/uploads/")) {
                return ResponseEntity.badRequest().body("无效的文件路径");
            }

            // 获取文件路径
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize()
                    .resolve(url.substring(9)).normalize(); // 移除"/uploads/"前缀

            // 验证路径是否在uploads目录内（防止路径遍历攻击）
            Path uploadsPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!filePath.startsWith(uploadsPath)) {
                return ResponseEntity.badRequest().body("无效的文件路径");
            }

            // 删除文件
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            // 更新网站设置，将对应字段设为null
            websiteSettingService.getWebsiteSettingByUserId(user.getId())
                    .ifPresent(setting -> {
                        if (setting.getSiteLogo() != null && setting.getSiteLogo().equals(url)) {
                            setting.setSiteLogo(null);
                        }
                        if (setting.getSiteAvatar() != null && setting.getSiteAvatar().equals(url)) {
                            setting.setSiteAvatar(null);
                        }
                        websiteSettingService.saveWebsiteSetting(setting);
                    });

            return ResponseEntity.ok("文件删除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("删除失败：" + e.getMessage());
        }
    }

    // 友链相关API
    @GetMapping("/api/friend-links")
    @ResponseBody
    public ResponseEntity<?> getFriendLinks(Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            List<FriendLink> friendLinks = friendLinkService.getFriendLinksByUserId(user.getId());
            return ResponseEntity.ok(friendLinks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/friend-links")
    @ResponseBody
    public ResponseEntity<?> createFriendLink(@RequestBody FriendLink friendLink, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 设置用户ID
            friendLink.setUserId(user.getId());

            // 设置默认排序值
            if (friendLink.getSortOrder() == null) {
                friendLink.setSortOrder(0);
            }

            FriendLink savedFriendLink = friendLinkService.saveFriendLink(friendLink);
            return ResponseEntity.ok(savedFriendLink);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/api/friend-links/{id}")
    @ResponseBody
    public ResponseEntity<?> updateFriendLink(@PathVariable Long id, @RequestBody FriendLink friendLink, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 验证用户权限
            FriendLink existingFriendLink = friendLinkService.getFriendLinksByUserId(user.getId())
                    .stream()
                    .filter(fl -> fl.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("无权限修改此友链"));

            existingFriendLink.setName(friendLink.getName());
            existingFriendLink.setUrl(friendLink.getUrl());
            existingFriendLink.setDescription(friendLink.getDescription());
            existingFriendLink.setLogo(friendLink.getLogo());

            FriendLink updatedFriendLink = friendLinkService.saveFriendLink(existingFriendLink);
            return ResponseEntity.ok(updatedFriendLink);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/friend-links/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteFriendLink(@PathVariable Long id, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 验证用户权限
            FriendLink existingFriendLink = friendLinkService.getFriendLinksByUserId(user.getId())
                    .stream()
                    .filter(fl -> fl.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("无权限删除此友链"));

            friendLinkService.deleteFriendLink(id);
            return ResponseEntity.ok("删除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/friend-links/reorder")
    @ResponseBody
    public ResponseEntity<?> reorderFriendLinks(@RequestBody List<Long> friendLinkIds, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            friendLinkService.updateFriendLinksOrder(friendLinkIds, user.getId());
            return ResponseEntity.ok("排序成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
