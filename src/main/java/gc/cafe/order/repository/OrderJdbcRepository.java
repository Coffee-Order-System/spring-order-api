package gc.cafe.order.repository;

import static gc.cafe.order.repository.JdbcUtils.toLocalDateTime;
import static gc.cafe.order.repository.JdbcUtils.toUUID;

import gc.cafe.order.model.Email;
import gc.cafe.order.model.Order;
import gc.cafe.order.model.OrderItem;
import gc.cafe.order.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void insert(Order order) {
        jdbcTemplate.update("INSERT INTO orders (order_id, email, address, postcode, total_price, order_status, created_at, updated_at) "
                            + "VALUES(UUID_TO_BIN(:orderId), :email, :address, :postcode, :totalPrice, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order));
        order.getOrderItems()
                .forEach(
                        orderItem -> jdbcTemplate
                                .update("INSERT INTO order_items (order_id, product_id, category, price, quantity, created_at, updated_at) "
                                        + "VALUES(UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                                        toOrderItemParamMap(order.getOrderId(), orderItem, order.getCreatedAt(), order.getUpdatedAt()))
                );

    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM order_items", Collections.emptyMap());
        jdbcTemplate.update("DELETE FROM orders", Collections.emptyMap());
    }

    @Override
    public List<Order> findByOrderStatus(OrderStatus orderStatus) {
        return jdbcTemplate.query("SELECT * FROM orders WHERE order_status = :orderStatus",
                new MapSqlParameterSource("orderStatus", orderStatus.toString()),
                orderRowMapper);
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", order.getOrderId().toString().getBytes());
        map.put("email", order.getEmail().address());
        map.put("address", order.getAddress());
        map.put("postcode", order.getPostcode());
        map.put("totalPrice", order.getTotalPrice());
        map.put("orderStatus", order.getOrderStatus().toString());
        map.put("createdAt", order.getCreatedAt());
        map.put("updatedAt", order.getUpdatedAt());
        return map;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId,  OrderItem orderItem,  LocalDateTime createdAt, LocalDateTime updatedAt) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId.toString().getBytes());
        map.put("productId", orderItem.productId().toString().getBytes());
        map.put("category", orderItem.category().toString());
        map.put("price", orderItem.price());
        map.put("quantity", orderItem.quantity());
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        return map;
    }

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = resultSet.getString("email");
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        var totalPrice = resultSet.getLong("total_price");
        var orderStatus = resultSet.getString("order_status");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(orderId, new Email(email), address, postcode, totalPrice, OrderStatus.valueOf(orderStatus), createdAt, updatedAt);
    };
}
