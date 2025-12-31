package cc.moonsea.navigation.repository;

import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    List<Category> findByUserOrderBySortOrderAsc(User user);
    
    List<Category> findByIsDefaultTrueOrderBySortOrderAsc();
    
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.websites WHERE c.user = :user ORDER BY c.sortOrder ASC")
    List<Category> findByUserWithWebsites(User user);
    
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.websites WHERE c.isDefault = true ORDER BY c.sortOrder ASC")
    List<Category> findDefaultCategoriesWithWebsites();
}
