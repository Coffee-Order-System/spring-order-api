package gc.cafe.order.mapper;

import gc.cafe.order.dto.OrderDto;
import gc.cafe.order.model.Order;

public interface OrderMapper {

    static OrderDto orderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setEmail(order.getEmail().address());
        orderDto.setAddress(order.getAddress());
        orderDto.setPostcode(order.getPostcode());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setOrderItems(order.getOrderItems());
        return orderDto;
    }
}
