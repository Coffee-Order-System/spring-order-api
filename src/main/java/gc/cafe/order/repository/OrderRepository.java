package gc.cafe.order.repository;

import gc.cafe.order.model.Email;
import gc.cafe.order.model.Order;
import gc.cafe.order.model.OrderItem;
import gc.cafe.order.model.OrderStatus;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    void insert(Order order);

    void deleteAll();

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    Optional<Order> findByEmailAndOrderStatus(Email email, OrderStatus orderStatus);

    void insertOrderItem(Order order, List<OrderItem> orderItem);

    List<Order> findAll();
}
