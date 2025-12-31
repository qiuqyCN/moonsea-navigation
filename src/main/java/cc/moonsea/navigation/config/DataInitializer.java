package cc.moonsea.navigation.config;

import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.Website;
import cc.moonsea.navigation.repository.CategoryRepository;
import cc.moonsea.navigation.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final CategoryRepository categoryRepository;
    private final WebsiteRepository websiteRepository;
    
    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            log.info("初始化默认分类和网址数据...");
            initializeDefaultData();
            log.info("默认数据初始化完成！");
        }
    }
    
    private void initializeDefaultData() {
        // 创建常用工具分类
        Category toolsCategory = createCategory("常用工具", "bi bi-tools", 0);
        createWebsite("百度", "https://www.baidu.com", "全球最大的中文搜索引擎", 
                     "https://www.baidu.com/favicon.ico", toolsCategory, 0);
        createWebsite("Google", "https://www.google.com", "全球最大的搜索引擎", 
                     "https://www.google.com/favicon.ico", toolsCategory, 1);
        createWebsite("必应", "https://www.bing.com", "微软搜索引擎", 
                     "https://www.bing.com/favicon.ico", toolsCategory, 2);
        
        // 创建社交媒体分类
        Category socialCategory = createCategory("社交媒体", "bi bi-chat-dots", 1);
        createWebsite("微信", "https://weixin.qq.com", "腾讯即时通讯工具", 
                     "https://weixin.qq.com/favicon.ico", socialCategory, 0);
        createWebsite("微博", "https://weibo.com", "中国社交媒体平台", 
                     "https://weibo.com/favicon.ico", socialCategory, 1);
        createWebsite("知乎", "https://www.zhihu.com", "中文问答社区", 
                     "https://www.zhihu.com/favicon.ico", socialCategory, 2);
        
        // 创建开发工具分类
        Category devCategory = createCategory("开发工具", "bi bi-code-slash", 2);
        createWebsite("GitHub", "https://github.com", "全球最大的代码托管平台", 
                     "https://github.com/favicon.ico", devCategory, 0);
        createWebsite("Stack Overflow", "https://stackoverflow.com", "程序员问答社区", 
                     "https://stackoverflow.com/favicon.ico", devCategory, 1);
        createWebsite("MDN", "https://developer.mozilla.org", "Web开发文档", 
                     "https://developer.mozilla.org/favicon.ico", devCategory, 2);
        
        // 创建视频娱乐分类
        Category videoCategory = createCategory("视频娱乐", "bi bi-play-circle", 3);
        createWebsite("B站", "https://www.bilibili.com", "年轻人的文化社区", 
                     "https://www.bilibili.com/favicon.ico", videoCategory, 0);
        createWebsite("YouTube", "https://www.youtube.com", "全球最大的视频网站", 
                     "https://www.youtube.com/favicon.ico", videoCategory, 1);
        createWebsite("爱奇艺", "https://www.iqiyi.com", "在线视频平台", 
                     "https://www.iqiyi.com/favicon.ico", videoCategory, 2);
        
        // 创建电商购物分类
        Category shoppingCategory = createCategory("电商购物", "bi bi-cart", 4);
        createWebsite("淘宝", "https://www.taobao.com", "亚洲最大的网上交易平台", 
                     "https://www.taobao.com/favicon.ico", shoppingCategory, 0);
        createWebsite("京东", "https://www.jd.com", "中国自营式电商企业", 
                     "https://www.jd.com/favicon.ico", shoppingCategory, 1);
        createWebsite("天猫", "https://www.tmall.com", "品质购物平台", 
                     "https://www.tmall.com/favicon.ico", shoppingCategory, 2);
    }
    
    private Category createCategory(String name, String icon, int sortOrder) {
        Category category = new Category();
        category.setName(name);
        category.setIcon(icon);
        category.setSortOrder(sortOrder);
        category.setIsDefault(true);
        return categoryRepository.save(category);
    }
    
    private Website createWebsite(String name, String url, String description, 
                                  String logo, Category category, int sortOrder) {
        Website website = new Website();
        website.setName(name);
        website.setUrl(url);
        website.setDescription(description);
        website.setLogo(logo);
        website.setSortOrder(sortOrder);
        website.setCategory(category);
        return websiteRepository.save(website);
    }
}
