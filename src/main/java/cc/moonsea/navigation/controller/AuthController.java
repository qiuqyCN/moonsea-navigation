package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.dto.ChangePasswordRequest;
import cc.moonsea.navigation.dto.RegisterRequest;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(request);
            redirectAttributes.addFlashAttribute("message", "注册成功！请登录");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/api/auth/change-password")
    @ResponseBody
    public String changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                return "未登录";
            }

            // 获取当前登录用户
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            userService.changePassword(user.getId(), request);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
