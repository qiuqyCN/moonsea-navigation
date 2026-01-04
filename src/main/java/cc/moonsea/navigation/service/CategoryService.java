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
    private final UserRepository userRepository;

    public List<Category> getDefaultCategories() {
        // 获取默认用户
        User defaultUser = getDefaultUser();
        return categoryRepository.findByUserWithWebsites(defaultUser);
    }

    private User getDefaultUser() {
        // 从配置中获取默认用户的用户名
        String defaultUsername = appConfig.getDefaultUser().getUsername();
        return userRepository.findByUsername(defaultUsername)
                .orElseThrow(() -> new RuntimeException("默认用户不存在"));
    }

    public List<Category> getUserCategories(User user) {
        return categoryRepository.findByUserWithWebsites(user);
    }

    @Transactional
    public Category createCategory(CategoryRequest dto, User user) {
        Category category = new Category();
        category.setName(dto.getName());
        // 如果没有提供图标路径，则使用默认SVG图标
        category.setIcon(dto.getIcon() != null && !dto.getIcon().trim().isEmpty() ? dto.getIcon() : "/images/icons/tag.svg");
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
        // 如果没有提供图标路径，则使用默认SVG图标
        category.setIcon(dto.getIcon() != null && !dto.getIcon().trim().isEmpty() ? dto.getIcon() : "/images/icons/tag.svg");
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
