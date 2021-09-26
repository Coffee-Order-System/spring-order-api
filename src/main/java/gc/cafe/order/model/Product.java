package gc.cafe.order.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "productId")
@ToString
@AllArgsConstructor
public class Product {

    private UUID productId;

    private String productName;

    private Category category;

    private Long price;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Product(UUID productId, String productName, Category category, Long price, String description) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Product of(UUID productId, String productName, Long price, Category category, String description) {
        Product product = new Product(productId, productName, category, price, description);
        return product;
    }
}
