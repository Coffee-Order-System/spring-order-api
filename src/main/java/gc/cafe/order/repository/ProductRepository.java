package gc.cafe.order.repository;

import gc.cafe.order.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    void insert(Product product);

    List<Product> findAll();

    void deleteAll();

    void update(Product product);

    Optional<Product> findById(UUID productId);
}
