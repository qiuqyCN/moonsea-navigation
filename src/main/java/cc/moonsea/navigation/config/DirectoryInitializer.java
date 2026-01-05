package cc.moonsea.navigation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DirectoryInitializer implements ApplicationRunner {

    @Value("${app.directories.upload:./uploads}")
    private String uploadDir;

    @Value("${app.directories.config:./config}")
    private String configDir;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 此时Spring容器已初始化，但数据源可能尚未完全配置
        createDirectories();
    }

    private void createDirectories() {
        createDirectory(uploadDir);
        createDirectory(configDir);
    }

    private void createDirectory(String path) {
        try {
            // 将相对路径转换为绝对路径
            Path absolutePath = Paths.get(path).toAbsolutePath().normalize();
            File dir = absolutePath.toFile();

            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (created) {
                    System.out.println("Directory created: " + absolutePath);
                    // 设置目录权限（如果需要）
                    dir.setReadable(true);
                    dir.setWritable(true);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create directory: " + path, e);
        }
    }

}
