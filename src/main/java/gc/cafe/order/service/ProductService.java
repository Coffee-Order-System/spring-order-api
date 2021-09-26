package gc.cafe.order.service;

import gc.cafe.order.model.Category;
import gc.cafe.order.model.Product;
import gc.cafe.order.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void addProduct(String productName, Long price, Category category, String description) {
        Product product = Product.of(UUID.randomUUID(), productName, price, category, description);
        productRepository.insert(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
