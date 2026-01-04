package cc.moonsea.navigation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private Long id;
    private String name;
    private String icon;
    private Integer sortOrder;
}
