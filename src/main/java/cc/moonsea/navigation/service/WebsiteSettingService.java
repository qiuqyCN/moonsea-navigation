package cc.moonsea.navigation.service;

import cc.moonsea.navigation.entity.WebsiteSetting;
import cc.moonsea.navigation.repository.WebsiteSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebsiteSettingService {

    private final WebsiteSettingRepository websiteSettingRepository;

    @Transactional
    public WebsiteSetting saveWebsiteSetting(WebsiteSetting websiteSetting) {
        return websiteSettingRepository.save(websiteSetting);
    }

    public Optional<WebsiteSetting> getWebsiteSettingByUserId(Long userId) {
        return websiteSettingRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteWebsiteSettingByUserId(Long userId) {
        websiteSettingRepository.findByUserId(userId).ifPresent(websiteSetting -> {
            websiteSettingRepository.deleteById(websiteSetting.getId());
        });
    }
}