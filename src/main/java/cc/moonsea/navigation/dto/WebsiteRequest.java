package cc.moonsea.navigation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteRequest {
    private Long id;
    private String name;
    private String url;
    private String description;
    private String logo;
    private Integer sortOrder;
    private Long categoryId;
}
