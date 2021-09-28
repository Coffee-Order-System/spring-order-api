package gc.cafe.order.dto;

import gc.cafe.order.model.OrderItem;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    @NotBlank
    @Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b")
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String postcode;

    private List<OrderItem> orderItems;

}
