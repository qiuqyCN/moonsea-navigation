package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.dto.WebsiteSettingResponse;
import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.FriendLink;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.entity.WebsiteSetting;
import cc.moonsea.navigation.service.CategoryService;
import cc.moonsea.navigation.service.FriendLinkService;
import cc.moonsea.navigation.service.UserService;
import cc.moonsea.navigation.service.WebsiteSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final WebsiteSettingService websiteSettingService;
    private final FriendLinkService friendLinkService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        List<Category> categories;
        boolean isAuthenticated = false;

        User user = userService.getDefaultUser(authentication);
        if (user != null) {
            categories = categoryService.getUserCategories(user);
            isAuthenticated = true;
            model.addAttribute("currentUser", user);
        } else {
            categories = categoryService.getDefaultCategories();
        }

        // 获取网站设置信息
        Optional<WebsiteSetting> settingOpt = websiteSettingService.getWebsiteSettingByUserId(user.getId());
        settingOpt.ifPresent(websiteSetting -> model.addAttribute("setting", WebsiteSettingResponse.fromEntity(websiteSetting)));

        model.addAttribute("categories", categories);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("activeTab", "home");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model, Authentication authentication) {
        User user = userService.getDefaultUser(authentication);

        // 获取网站设置信息
        Optional<WebsiteSetting> settingOpt = websiteSettingService.getWebsiteSettingByUserId(user.getId());
        settingOpt.ifPresent(websiteSetting -> model.addAttribute("setting", websiteSetting));

        model.addAttribute("activeTab", "about");
        return "about";
    }

    @GetMapping("/links")
    public String links(Model model, Authentication authentication) {
        User user = userService.getDefaultUser(authentication);

        List<FriendLink> friendLinks = friendLinkService.getFriendLinksByUserId(user.getId());
        // 获取网站设置信息
        Optional<WebsiteSetting> settingOpt = websiteSettingService.getWebsiteSettingByUserId(user.getId());
        settingOpt.ifPresent(websiteSetting -> model.addAttribute("setting",  WebsiteSettingResponse.fromEntity(websiteSetting)));

        model.addAttribute("friendLinks", friendLinks);
        model.addAttribute("activeTab", "links");
        return "links";
    }
}
