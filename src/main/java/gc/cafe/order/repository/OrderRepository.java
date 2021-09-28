package gc.cafe.order.repository;

import gc.cafe.order.model.Order;
import gc.cafe.order.model.OrderStatus;
import java.util.List;

public interface OrderRepository {

    void insert(Order order);

    void deleteAll();

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
