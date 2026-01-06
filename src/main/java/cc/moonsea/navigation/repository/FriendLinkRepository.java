package cc.moonsea.navigation.repository;

import cc.moonsea.navigation.entity.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendLinkRepository extends JpaRepository<FriendLink, Long> {
    List<FriendLink> findByUserIdOrderBySortOrderAsc(Long userId);
    
    @Query("SELECT f FROM FriendLink f WHERE f.userId = :userId ORDER BY f.sortOrder ASC")
    List<FriendLink> findFriendLinksByUserId(@Param("userId") Long userId);
}