package gc.cafe.order.model;

import java.util.UUID;
import org.springframework.util.Assert;

public record OrderItem(UUID productId, Category category, Long price, int quantity) {

    public OrderItem {
        Assert.notNull(productId, "product Id is not null");
        Assert.notNull(category, "category Id is not null");
        Assert.notNull(price, "price Id is not null");
        Assert.notNull(quantity, "quantity Id is not null");
    }
}
