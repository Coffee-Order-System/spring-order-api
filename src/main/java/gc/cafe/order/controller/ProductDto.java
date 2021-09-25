package gc.cafe.order.controller;

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

}
