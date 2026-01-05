package cc.moonsea.navigation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteRequest {
    private Long id;
    
    @NotBlank(message = "网站名称不能为空")
    @Size(max = 100, message = "网站名称长度不能超过100个字符")
    private String name;
    
    @NotBlank(message = "网站地址不能为空")
    @Size(max = 255, message = "网站地址长度不能超过255个字符")
    private String url;
    
    private String description;
    private String logo;
    private Integer sortOrder;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
}