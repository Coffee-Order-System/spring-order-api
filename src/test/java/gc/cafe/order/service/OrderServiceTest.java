package gc.cafe.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import gc.cafe.order.model.Category;
import gc.cafe.order.model.Email;
import gc.cafe.order.model.Order;
import gc.cafe.order.model.OrderItem;
import gc.cafe.order.model.OrderStatus;
import gc.cafe.order.model.Product;
import gc.cafe.order.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Test
    @DisplayName("주문 저장시 동일 이메일이 존재할 경우 같은 주문으로 처리")
    void addOrderEqualToEmail() {
        UUID productId = UUID.randomUUID();

        OrderItem orderItem = new OrderItem(productId, Category.COFFEE_BEAN_PACKAGE, 15000L, 1);
        Email email = new Email("tester@email.com");
        UUID orderId = UUID.randomUUID();
        String address = "서울시";
        String postcode = "11111";
        Long price = 15000L;
        Order order = new Order(orderId, email, address, postcode, price, List.of(orderItem),
                OrderStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());

        given(orderRepository.findByEmailAndOrderStatus(email, OrderStatus.ACCEPTED))
                .willReturn(Optional.of(order));

        List<OrderItem> newOrderItems = new ArrayList<>();
        newOrderItems.add(new OrderItem(productId, Category.COFFEE_BEAN_PACKAGE, 15000L, 2));

        orderService.addOrder("tester@email.com", address, postcode, newOrderItems);

        then(orderRepository).should().insertOrderItem(any(), any());
    }

}