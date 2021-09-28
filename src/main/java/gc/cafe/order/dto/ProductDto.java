package gc.cafe.order.dto;

import gc.cafe.order.model.Category;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ProductDto {

    private String productId;

    @NotNull
    @Length(min = 1, max = 20)
    private String productName;

    @NotNull
    private Category category;

    @Min(100)
    @Max(1_000_000)
    private Long price;

    private String description;

    private String createdAt;

    private String updatedAt;

}
