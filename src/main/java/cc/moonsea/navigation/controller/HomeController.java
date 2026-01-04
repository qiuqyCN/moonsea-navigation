package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.service.CategoryService;
import cc.moonsea.navigation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        List<Category> categories;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated() &&
            !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);

            if (user != null) {
                categories = categoryService.getUserCategories(user);
                isAuthenticated = true;
                model.addAttribute("currentUser", user);
            } else {
                categories = categoryService.getDefaultCategories();
            }
        } else {
            categories = categoryService.getDefaultCategories();
        }

        model.addAttribute("categories", categories);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("activeTab", "home");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("activeTab", "about");
        return "about";
    }

    @GetMapping("/links")
    public String links(Model model) {
        model.addAttribute("activeTab", "links");
        return "links";
    }
}
