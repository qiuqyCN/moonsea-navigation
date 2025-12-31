package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.entity.Website;
import cc.moonsea.navigation.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<Website>> search(@RequestParam String keyword) {
        List<Website> websites = websiteService.searchWebsites(keyword);
        return ResponseEntity.ok(websites);
    }
}