package cc.moonsea.navigation.repository;

import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {
    
    List<Website> findByCategoryOrderBySortOrderAsc(Category category);
    
    @Query("SELECT w FROM Website w WHERE w.category.id IN :categoryIds ORDER BY w.sortOrder ASC")
    List<Website> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
    
    @Query("SELECT w FROM Website w WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(w.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Website> searchByKeyword(@Param("keyword") String keyword);
}
