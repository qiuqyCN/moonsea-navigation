package cc.moonsea.navigation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "website_settings")
@Data
public class WebsiteSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "site_logo")
    private String siteLogo;

    @Column(name = "site_avatar")
    private String siteAvatar;

    @Column(name = "site_domain")
    private String siteDomain;

    @Column(name = "email")
    private String email;

    @Column(name = "wechat")
    private String wechat;

    @Lob
    @Column(name = "about_content")
    private String aboutContent;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}