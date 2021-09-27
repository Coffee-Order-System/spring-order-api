package gc.cafe.order.repository;

import gc.cafe.order.model.Order;

public interface OrderRepository {

    void insert(Order order);

    void deleteAll();
}
