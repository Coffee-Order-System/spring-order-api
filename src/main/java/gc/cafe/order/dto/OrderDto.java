package gc.cafe.order.dto;

import gc.cafe.order.model.OrderItem;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    private String email;

    private String address;

    private String postcode;

    private List<OrderItem> orderItems;

}
