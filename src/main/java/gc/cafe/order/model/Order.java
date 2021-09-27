package gc.cafe.order.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Order {

    private UUID orderId;

    private Email email;

    private String address;

    private String postcode;

    private Long totalPrice;

    private List<OrderItem> orderItems;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Order of(UUID orderId, Email email, String address, String postcode, List<OrderItem> orderItems) {
        return new Order(orderId, email, address, postcode, sumPrice(orderItems), orderItems,
                OrderStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
    }

    private static Long sumPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(orderItem -> orderItem.price() * orderItem.quantity())
                .sum();
    }
}
