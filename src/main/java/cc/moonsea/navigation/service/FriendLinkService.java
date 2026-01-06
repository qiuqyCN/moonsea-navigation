package cc.moonsea.navigation.service;

import cc.moonsea.navigation.entity.FriendLink;
import cc.moonsea.navigation.repository.FriendLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendLinkService {

    private final FriendLinkRepository friendLinkRepository;

    @Transactional
    public FriendLink saveFriendLink(FriendLink friendLink) {
        return friendLinkRepository.save(friendLink);
    }

    public List<FriendLink> getFriendLinksByUserId(Long userId) {
        return friendLinkRepository.findByUserIdOrderBySortOrderAsc(userId);
    }

    @Transactional
    public FriendLink updateFriendLink(Long id, FriendLink friendLink) {
        friendLink.setId(id);
        return friendLinkRepository.save(friendLink);
    }

    @Transactional
    public void deleteFriendLink(Long id) {
        friendLinkRepository.deleteById(id);
    }

    @Transactional
    public void updateFriendLinksOrder(List<Long> friendLinkIds, Long userId) {
        for (int i = 0; i < friendLinkIds.size(); i++) {
            Long id = friendLinkIds.get(i);
            FriendLink friendLink = friendLinkRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("友链不存在，ID: " + id));
            
            // 验证用户权限
            if (!friendLink.getUserId().equals(userId)) {
                throw new RuntimeException("无权限修改此友链，ID: " + id);
            }
            
            friendLink.setSortOrder(i);
            friendLinkRepository.save(friendLink);
        }
    }
}