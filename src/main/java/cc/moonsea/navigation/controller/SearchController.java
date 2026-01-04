package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.entity.Website;
import cc.moonsea.navigation.service.UserService;
import cc.moonsea.navigation.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final WebsiteService websiteService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Website>> search(@RequestParam String keyword, Authentication authentication) {
        List<Website> websites;
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                websites = websiteService.searchWebsitesByUser(keyword, user);
                return ResponseEntity.ok().body(websites);
            }
        }

        websites = websiteService.searchWebsitesByUser(keyword, userService.getDefaultUser());
        return ResponseEntity.ok(websites);
    }
}
