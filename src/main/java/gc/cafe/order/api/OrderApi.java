package gc.cafe.order.api;

import static gc.cafe.order.api.OrderApi.PREFIX;

import gc.cafe.order.dto.OrderDto;
import gc.cafe.order.exception.BadRequestException;
import gc.cafe.order.model.Order;
import gc.cafe.order.service.OrderService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PREFIX)
public class OrderApi {

    protected static final String PREFIX = "/api/v1";
    protected static final String ORDERS = "/orders";

    private final OrderService orderService;

    @PostMapping( ORDERS)
    public ResponseEntity submitOrder(@RequestBody @Valid OrderDto orderDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException(result.getFieldError().getDefaultMessage());
        }
        Order order = orderService.addOrder(
                orderDto.getEmail(),
                orderDto.getAddress(),
                orderDto.getPostcode(),
                orderDto.getOrderItems());
        return ResponseEntity.created(URI.create(PREFIX + ORDERS)).body(order);
    }
}
