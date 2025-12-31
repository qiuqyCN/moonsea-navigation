package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.dto.WebsiteDTO;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.entity.Website;
import cc.moonsea.navigation.service.UserService;
import cc.moonsea.navigation.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/websites")
@RequiredArgsConstructor
public class WebsiteController {
    
    private final WebsiteService websiteService;
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<?> createWebsite(@RequestBody WebsiteDTO dto, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            Website website = websiteService.createWebsite(dto, user.getId());
            return ResponseEntity.ok(website);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWebsite(@PathVariable Long id, 
                                          @RequestBody WebsiteDTO dto, 
                                          Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            Website website = websiteService.updateWebsite(id, dto, user.getId());
            return ResponseEntity.ok(website);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWebsite(@PathVariable Long id, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            websiteService.deleteWebsite(id, user.getId());
            return ResponseEntity.ok("删除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/reorder")
    public ResponseEntity<?> reorderWebsites(@RequestBody List<Long> websiteIds, 
                                            Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            websiteService.updateWebsitesOrder(websiteIds, user.getId());
            return ResponseEntity.ok("排序成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
