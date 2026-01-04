package cc.moonsea.navigation.service;

import cc.moonsea.navigation.dto.WebsiteRequest;
import cc.moonsea.navigation.entity.Category;
import cc.moonsea.navigation.entity.Website;
import cc.moonsea.navigation.repository.CategoryRepository;
import cc.moonsea.navigation.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebsiteService {

    private final WebsiteRepository websiteRepository;
    private final CategoryRepository categoryRepository;

    public List<Website> getWebsitesByCategory(Category category) {
        return websiteRepository.findByCategoryOrderBySortOrderAsc(category);
    }

    public List<Website> searchWebsites(String keyword) {
        return websiteRepository.searchByKeyword(keyword);
    }

    @Transactional
    public Website createWebsite(WebsiteRequest dto, Long userId) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("分类不存在"));

        if (category.getUser() == null || !category.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限在该分类下添加网址");
        }

        Website website = new Website();
        website.setName(dto.getName());
        website.setUrl(dto.getUrl());
        website.setDescription(dto.getDescription());
        website.setLogo(dto.getLogo());
        website.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        website.setCategory(category);

        return websiteRepository.save(website);
    }

    @Transactional
    public Website updateWebsite(Long id, WebsiteRequest dto, Long userId) {
        Website website = websiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("网址不存在"));

        if (website.getCategory().getUser() == null ||
            !website.getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限修改该网址");
        }

        website.setName(dto.getName());
        website.setUrl(dto.getUrl());
        website.setDescription(dto.getDescription());
        website.setLogo(dto.getLogo());
        website.setSortOrder(dto.getSortOrder());

        if (dto.getCategoryId() != null && !dto.getCategoryId().equals(website.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("目标分类不存在"));
            website.setCategory(newCategory);
        }

        return websiteRepository.save(website);
    }

    @Transactional
    public void deleteWebsite(Long id, Long userId) {
        Website website = websiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("网址不存在"));

        if (website.getCategory().getUser() == null ||
            !website.getCategory().getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限删除该网址");
        }

        websiteRepository.delete(website);
    }

    @Transactional
    public void updateWebsitesOrder(List<Long> websiteIds, Long userId) {
        for (int i = 0; i < websiteIds.size(); i++) {
            Long websiteId = websiteIds.get(i);
            Website website = websiteRepository.findById(websiteId)
                    .orElseThrow(() -> new RuntimeException("网址不存在"));

            if (website.getCategory().getUser() == null ||
                !website.getCategory().getUser().getId().equals(userId)) {
                continue;
            }

            website.setSortOrder(i);
            websiteRepository.save(website);
        }
    }
}
