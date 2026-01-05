package cc.moonsea.navigation.config;

import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.entity.Website;
import cc.moonsea.navigation.repository.CategoryRepository;
import cc.moonsea.navigation.repository.UserRepository;
import cc.moonsea.navigation.repository.WebsiteRepository;
import cc.moonsea.navigation.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final WebsiteRepository websiteRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;

    @Override
    public void run(String... args) {
        // 确保默认用户存在
        ensureDefaultUser();

        if (categoryRepository.count() == 0) {
            log.info("初始化默认分类和网址数据...");
            initializeDefaultData();
            log.info("默认数据初始化完成！");
        }
    }

    private void initializeDefaultData() {
        // 创建常用工具分类
        Category toolsCategory = createCategory("常用工具", "/images/icons/wrench-screwdriver.svg", 0);
        createWebsite("百度", "https://www.baidu.com", "全球最大的中文搜索引擎",
                     "https://www.baidu.com/favicon.ico", toolsCategory, 0);
        createWebsite("Google", "https://www.google.com", "全球最大的搜索引擎",
                     "https://www.google.com/favicon.ico", toolsCategory, 1);
        createWebsite("必应", "https://www.bing.com", "微软搜索引擎",
                     "https://www.bing.com/favicon.ico", toolsCategory, 2);
        createWebsite("Wolfram Alpha", "https://www.wolframalpha.com", "计算知识搜索引擎",
                     "https://www.wolframalpha.com/favicon.ico", toolsCategory, 3);
        createWebsite("Convertio", "https://convertio.co/", "在线文件转换工具",
                     "https://convertio.co/images/favicon.ico", toolsCategory, 4);
        createWebsite("TinyPNG", "https://tinypng.com/", "智能WebP、PNG和JPEG压缩工具",
                     "https://tinypng.com/images/favicon.ico", toolsCategory, 5);
        createWebsite("Regex101", "https://regex101.com/", "在线正则表达式测试工具",
                     "https://regex101.com/favicon.ico", toolsCategory, 6);

        // 创建社交媒体分类
        Category socialCategory = createCategory("社交媒体", "/images/icons/chat-bubble-left-right.svg", 1);
        createWebsite("微信", "https://weixin.qq.com", "腾讯即时通讯工具",
                     "https://weixin.qq.com/favicon.ico", socialCategory, 0);
        createWebsite("微博", "https://weibo.com", "中国社交媒体平台",
                     "https://weibo.com/favicon.ico", socialCategory, 1);
        createWebsite("知乎", "https://www.zhihu.com", "中文问答社区",
                     "https://www.zhihu.com/favicon.ico", socialCategory, 2);
        createWebsite("Twitter", "https://twitter.com", "全球社交媒体平台",
                     "https://twitter.com/favicon.ico", socialCategory, 3);
        createWebsite("LinkedIn", "https://www.linkedin.com", "职业社交平台",
                     "https://www.linkedin.com/favicon.ico", socialCategory, 4);
        createWebsite("Reddit", "https://www.reddit.com", "社交新闻聚合网站",
                     "https://www.reddit.com/favicon.ico", socialCategory, 5);
        createWebsite("Discord", "https://discord.com", "语音、视频和文字聊天平台",
                     "https://discord.com/favicon.ico", socialCategory, 6);

        // 创建开发工具分类
        Category devCategory = createCategory("开发工具", "/images/icons/command-line.svg", 2);
        createWebsite("GitHub", "https://github.com", "全球最大的代码托管平台",
                     "https://github.com/favicon.ico", devCategory, 0);
        createWebsite("Stack Overflow", "https://stackoverflow.com", "程序员问答社区",
                     "https://stackoverflow.com/favicon.ico", devCategory, 1);
        createWebsite("MDN", "https://developer.mozilla.org", "Web开发文档",
                     "https://developer.mozilla.org/favicon.ico", devCategory, 2);
        createWebsite("GitLab", "https://about.gitlab.com/", "Git代码托管和DevOps平台",
                     "https://about.gitlab.com/favicon.ico", devCategory, 3);
        createWebsite("Bitbucket", "https://bitbucket.org/", "代码托管平台",
                     "https://bitbucket.org/favicon.ico", devCategory, 4);
        createWebsite("Docker Hub", "https://hub.docker.com/", "Docker镜像仓库",
                     "https://hub.docker.com/favicon.ico", devCategory, 5);
        createWebsite("npm", "https://www.npmjs.com/", "Node.js包管理器",
                     "https://www.npmjs.com/static/images/touch-icons/favicon-32x32.png", devCategory, 6);
        createWebsite("PyPI", "https://pypi.org/", "Python包索引",
                     "https://pypi.org/static/images/favicon.401c5.png", devCategory, 7);

        // 创建Java开发分类
        Category javaCategory = createCategory("Java开发", "/images/icons/code-bracket.svg", 3);
        createWebsite("Oracle Java", "https://www.oracle.com/java/", "Java官方平台",
                     "https://www.oracle.com/favicon.ico", javaCategory, 0);
        createWebsite("OpenJDK", "https://openjdk.org/", "开源Java开发工具包",
                     "https://openjdk.org/favicon.ico", javaCategory, 1);
        createWebsite("Maven Central", "https://mvnrepository.com/", "Java Maven依赖仓库",
                     "https://mvnrepository.com/favicon.ico", javaCategory, 2);
        createWebsite("Spring Framework", "https://spring.io/projects/spring-framework", "Spring框架",
                     "https://spring.io/favicon.ico", javaCategory, 3);
        createWebsite("Spring Boot", "https://spring.io/projects/spring-boot", "Spring Boot框架",
                     "https://spring.io/favicon.ico", javaCategory, 4);
        createWebsite("Gradle", "https://gradle.org/", "自动化构建工具",
                     "https://gradle.org/favicon.ico", javaCategory, 5);
        createWebsite("Apache Maven", "https://maven.apache.org/", "项目管理和理解工具",
                     "https://maven.apache.org/favicon.ico", javaCategory, 6);
        createWebsite("IntelliJ IDEA", "https://www.jetbrains.com/idea/", "Java开发IDE",
                     "https://www.jetbrains.com/favicon.ico", javaCategory, 7);

        // 创建视频娱乐分类
        Category videoCategory = createCategory("视频娱乐", "/images/icons/play-circle.svg", 4);
        createWebsite("B站", "https://www.bilibili.com", "年轻人的文化社区",
                     "https://www.bilibili.com/favicon.ico", videoCategory, 0);
        createWebsite("YouTube", "https://www.youtube.com", "全球最大的视频网站",
                     "https://www.youtube.com/favicon.ico", videoCategory, 1);
        createWebsite("爱奇艺", "https://www.iqiyi.com", "在线视频平台",
                     "https://www.iqiyi.com/favicon.ico", videoCategory, 2);
        createWebsite("腾讯视频", "https://v.qq.com", "在线视频平台",
                     "https://v.qq.com/favicon.ico", videoCategory, 3);
        createWebsite("优酷", "https://www.youku.com", "在线视频平台",
                     "https://www.youku.com/favicon.ico", videoCategory, 4);
        createWebsite("Netflix", "https://www.netflix.com", "流媒体视频服务",
                     "https://assets.nflxext.com/us/ffe/siteui/common/icons/nficon2016.ico", videoCategory, 5);
        createWebsite("Hulu", "https://www.hulu.com", "流媒体视频服务",
                     "https://www.hulu.com/favicon.ico", videoCategory, 6);

        // 创建电商购物分类
        Category shoppingCategory = createCategory("电商购物", "/images/icons/shopping-cart.svg", 5);
        createWebsite("淘宝", "https://www.taobao.com", "亚洲最大的网上交易平台",
                     "https://www.taobao.com/favicon.ico", shoppingCategory, 0);
        createWebsite("京东", "https://www.jd.com", "中国自营式电商企业",
                     "https://www.jd.com/favicon.ico", shoppingCategory, 1);
        createWebsite("天猫", "https://www.tmall.com", "品质购物平台",
                     "https://www.tmall.com/favicon.ico", shoppingCategory, 2);
        createWebsite("拼多多", "https://www.pinduoduo.com", "社交电商平台",
                     "https://www.pinduoduo.com/favicon.ico", shoppingCategory, 3);
        createWebsite("亚马逊", "https://www.amazon.com", "全球电商平台",
                     "https://www.amazon.com/favicon.ico", shoppingCategory, 4);
        createWebsite("eBay", "https://www.ebay.com", "全球在线拍卖和购物网站",
                     "https://www.ebay.com/favicon.ico", shoppingCategory, 5);
        createWebsite("当当网", "https://www.dangdang.com", "综合性网上购物平台",
                     "https://www.dangdang.com/favicon.ico", shoppingCategory, 6);
    }

    private void ensureDefaultUser() {
        AppConfig.DefaultUser defaultUser = appConfig.getDefaultUser();

        // 检查用户是否已存在
        User existingUser = userRepository.findByUsername(defaultUser.getUsername()).orElse(null);
        if (existingUser == null) {
            log.info("创建默认用户...");
            User user = new User();
            user.setUsername(defaultUser.getUsername());
            user.setPassword(passwordEncoder.encode(defaultUser.getPassword())); // 默认密码
            user.setEmail(defaultUser.getEmail());
            userRepository.save(user);
            log.info("默认用户创建成功！");
        }
    }



    private Category createCategory(String name, String icon, int sortOrder) {
        // 获取默认用户
        User defaultUser = userService.getDefaultUser();

        Category category = new Category();
        category.setName(name);
        // 如果没有提供图标路径，则使用默认SVG图标
        category.setIcon(icon != null && !icon.trim().isEmpty() ? icon : "/images/icons/tag.svg");
        category.setSortOrder(sortOrder);
        category.setUser(defaultUser);
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
