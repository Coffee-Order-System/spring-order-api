package gc.cafe.order.service;

import gc.cafe.order.model.Email;
import gc.cafe.order.model.Order;
import gc.cafe.order.model.OrderItem;
import gc.cafe.order.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order addOrder(String email, String address, String postcode, List<OrderItem> orderItems) {
        Order order = Order.of(UUID.randomUUID(), new Email(email), address, postcode, orderItems);
        orderRepository.insert(order);
        return order;
    }
    
}
