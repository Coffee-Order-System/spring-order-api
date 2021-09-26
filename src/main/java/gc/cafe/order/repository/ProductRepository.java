package gc.cafe.order.repository;

import gc.cafe.order.model.Product;
import java.util.List;

public interface ProductRepository {

    void insert(Product product);

    List<Product> findAll();

    void deleteAll();

}
