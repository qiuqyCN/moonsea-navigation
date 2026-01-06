package cc.moonsea.navigation.dto;

import cc.moonsea.navigation.entity.WebsiteSetting;
import lombok.Data;

@Data
public class WebsiteSettingResponse {
    private String siteName;

    private String siteLogo;

    private String siteAvatar;

    private String siteDomain;

    private String email;

    private String wechat;

    private String description;

    private String keywords;

    public static WebsiteSettingResponse fromEntity(WebsiteSetting entity) {
        WebsiteSettingResponse response = new WebsiteSettingResponse();
        response.setSiteName(entity.getSiteName());
        response.setSiteLogo(entity.getSiteLogo());
        response.setSiteAvatar(entity.getSiteAvatar());
        response.setSiteDomain(entity.getSiteDomain());
        response.setEmail(entity.getEmail());
        response.setWechat(entity.getWechat());
        response.setDescription(entity.getDescription());
        response.setKeywords(entity.getKeywords());
        return response;
    }
}
