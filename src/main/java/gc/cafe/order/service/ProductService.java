package gc.cafe.order.service;

import gc.cafe.order.model.Category;
import gc.cafe.order.model.Product;
import gc.cafe.order.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
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
        Product product = Product.of(UUID.randomUUID(), productName, category, price, description);
        productRepository.insert(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void updateProduct(String productId, String productName, Long price, Category category, String description) {
        Optional<Product> optionalProduct = productRepository.findById(UUID.fromString(productId));
        optionalProduct.ifPresent(p -> {
            p.update(productName, price, category, description);
            productRepository.update(p);
        });
    }

    public Optional<Product> getProduct(String productId) {
        return productRepository.findById(UUID.fromString(productId));
    }
}
