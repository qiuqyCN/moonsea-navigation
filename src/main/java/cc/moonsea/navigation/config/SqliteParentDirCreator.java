//package cc.moonsea.navigation.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@Slf4j
//@Component
//public class SqliteParentDirCreator implements BeanFactoryPostProcessor, EnvironmentAware {
//
//    private Environment env;
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.env = environment;
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        // 2. 检查是否为SQLite数据源
//        String driverClass = env.getProperty("spring.datasource.driver-class-name");
//        String jdbcUrl = env.getProperty("spring.datasource.url");
//
//        boolean isSqlite = "org.sqlite.JDBC".equals(driverClass) ||
//                (jdbcUrl != null && jdbcUrl.startsWith("jdbc:sqlite:"));
//
//        if (!isSqlite) {
//            return; // 不是SQLite，直接返回
//        }
//
//        // 3. 解析并创建SQLite数据库目录
//        createSqliteDatabaseDirectory(jdbcUrl);
//    }
//
//    private void createSqliteDatabaseDirectory(String jdbcUrl) {
//        try {
//            if (jdbcUrl == null) {
//                return;
//            }
//
//            // 提取文件路径
//            String filePath = extractSqliteFilePath(jdbcUrl);
//
//            if (":memory:".equals(filePath)) {
//                return; // 内存数据库，无需创建目录
//            }
//
//            // 转换为绝对路径
//            Path dbPath = Paths.get(filePath).toAbsolutePath().normalize();
//            Path dbDir = dbPath.getParent();
//
//            if (dbDir != null && !Files.exists(dbDir)) {
//                // 创建数据库目录
//                Files.createDirectories(dbDir);
//                log.info("✅ SQLite目录创建成功: {}", dbDir);
//
//                // 设置目录权限
//                setDirectoryPermissions(dbDir);
//            }
//
//        } catch (Exception e) {
//            log.error("❌ 创建SQLite目录失败: {}", e.getMessage());
//            throw new IllegalStateException("无法创建SQLite数据库目录", e);
//        }
//    }
//
//    private String extractSqliteFilePath(String jdbcUrl) {
//        if (!jdbcUrl.startsWith("jdbc:sqlite:")) {
//            throw new IllegalArgumentException("非SQLite JDBC URL: " + jdbcUrl);
//        }
//
//        String path = jdbcUrl.substring("jdbc:sqlite:".length());
//
//        // 移除file:前缀（如果存在）
//        if (path.startsWith("file:")) {
//            path = path.substring(5);
//        }
//
//        // 处理特殊路径
//        if (path.startsWith("~/")) {
//            // 用户目录
//            return System.getProperty("user.home") + path.substring(1);
//        } else if (path.startsWith("./")) {
//            // 相对路径
//            return System.getProperty("user.dir") + path.substring(1);
//        } else if (path.startsWith("/") || path.contains(":\\")) {
//            // 绝对路径（Linux/Windows）
//            return path;
//        } else if (":memory:".equals(path)) {
//            // 内存数据库
//            return path;
//        } else {
//            // 默认视为相对路径
//            return "./" + path;
//        }
//    }
//
//    private void setDirectoryPermissions(Path directory) {
//        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
//            // Linux/Unix系统：设置755权限
//            File dir = directory.toFile();
//            dir.setReadable(true, false);
//            dir.setWritable(true, true);
//            dir.setExecutable(true, false);
//        }
//    }
//}
