package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SitemapController {
    
    private final CategoryService categoryService;
    
    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String getSitemap() {
        StringBuilder sitemap = new StringBuilder();
        sitemap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sitemap.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        
        // 添加首页
        sitemap.append("  <url>\n");
        sitemap.append("    <loc>http://localhost:8080/</loc>\n");
        sitemap.append("    <changefreq>daily</changefreq>\n");
        sitemap.append("    <priority>1.0</priority>\n");
        sitemap.append("  </url>\n");
        
        // 添加关于页面
        sitemap.append("  <url>\n");
        sitemap.append("    <loc>http://localhost:8080/about</loc>\n");
        sitemap.append("    <changefreq>monthly</changefreq>\n");
        sitemap.append("    <priority>0.8</priority>\n");
        sitemap.append("  </url>\n");
        
        // 添加友链页面
        sitemap.append("  <url>\n");
        sitemap.append("    <loc>http://localhost:8080/links</loc>\n");
        sitemap.append("    <changefreq>weekly</changefreq>\n");
        sitemap.append("    <priority>0.7</priority>\n");
        sitemap.append("  </url>\n");
        
        sitemap.append("</urlset>");
        
        return sitemap.toString();
    }
}
