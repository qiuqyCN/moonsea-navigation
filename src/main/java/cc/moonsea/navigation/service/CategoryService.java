package cc.moonsea.navigation.service;

import cc.moonsea.navigation.dto.CategoryDTO;
import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.User;
import cc.moonsea.navigation.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public List<Category> getDefaultCategories() {
        return categoryRepository.findDefaultCategoriesWithWebsites();
    }
    
    public List<Category> getUserCategories(User user) {
        return categoryRepository.findByUserWithWebsites(user);
    }
    
    @Transactional
    public Category createCategory(CategoryDTO dto, User user) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setIcon(dto.getIcon());
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        category.setIsDefault(false);
        category.setUser(user);
        
        return categoryRepository.save(category);
    }
    
    @Transactional
    public Category updateCategory(Long id, CategoryDTO dto, User user) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        if (category.getUser() == null || !category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("无权限修改该分类");
        }
        
        category.setName(dto.getName());
        category.setIcon(dto.getIcon());
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
