package cc.moonsea.navigation.service;

import cc.moonsea.navigation.config.AppConfig;
import cc.moonsea.navigation.dto.CategoryRequest;
import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.repository.CategoryRepository;
import cc.moonsea.navigation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AppConfig appConfig;
    private final UserService userService;

    public List<Category> getDefaultCategories() {
        // 获取默认用户
        User defaultUser = userService.getDefaultUser();
        return categoryRepository.findByUserWithWebsites(defaultUser);
    }

    public String processIconValue(String iconInput) {
        if (iconInput == null || iconInput.trim().isEmpty()) {
            // 如果没有提供图标，使用默认图标
            return "/images/icons/tag.svg";
        }

        String trimmedIcon = iconInput.trim();

        // 检查是否为SVG文本内容
        if (trimmedIcon.startsWith("<svg") && trimmedIcon.contains("</svg>")) {
            // 这是SVG文本内容，直接返回
            return trimmedIcon;
        } else {
            // 这是图标路径，检查是否以/images/icons/开头
            if (trimmedIcon.startsWith("/images/icons/")) {
                return trimmedIcon;
            } else {
                // 如果不是标准路径，添加/images/icons/前缀
                return trimmedIcon.startsWith("/") ? trimmedIcon : "/images/icons/" + trimmedIcon;
            }
        }
    }

    public Category getCategoryById(Long id, User user) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        if (category.getUser() == null || !category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("无权限访问该分类");
        }

        return category;
    }

    public List<Category> getUserCategories(User user) {
        return categoryRepository.findByUserWithWebsites(user);
    }

    @Transactional
    public Category createCategory(CategoryRequest dto, User user) {
        Category category = new Category();
        category.setName(dto.getName());
        // 处理图标：如果是SVG文本内容则直接存储，否则存储图标路径
        String iconValue = processIconValue(dto.getIcon());
        category.setIcon(iconValue);
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);

        category.setUser(user);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, CategoryRequest dto, User user) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        if (category.getUser() == null || !category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("无权限修改该分类");
        }

        category.setName(dto.getName());
        // 处理图标：如果是SVG文本内容则直接存储，否则存储图标路径
        String iconValue = processIconValue(dto.getIcon());
        category.setIcon(iconValue);
        category.setSortOrder(dto.getSortOrder());

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id, User user) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        if (category.getUser() == null || !category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("无权限删除该分类");
        }

        categoryRepository.delete(category);
    }

    @Transactional
    public void updateCategoriesOrder(List<Long> categoryIds, User user) {
        for (int i = 0; i < categoryIds.size(); i++) {
            Long categoryId = categoryIds.get(i);
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("分类不存在"));

            if (category.getUser() == null || !category.getUser().getId().equals(user.getId())) {
                continue;
            }

            category.setSortOrder(i);
            categoryRepository.save(category);
        }
    }
}
