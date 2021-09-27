package gc.cafe.order.dto;

import gc.cafe.order.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private String productId;

    private String productName;

    private Category category;

    private Long price;

    private String description;

    private String createdAt;

    private String updatedAt;

}
