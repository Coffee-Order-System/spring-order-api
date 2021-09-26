package gc.cafe.order.repository;

import static gc.cafe.order.repository.JdbcUtils.toLocalDateTime;
import static gc.cafe.order.repository.JdbcUtils.toUUID;

import gc.cafe.order.model.Category;
import gc.cafe.order.model.Product;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJdbcRepositoryImpl implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(Product product) {
        jdbcTemplate.update("INSERT INTO products (product_id, product_name, category, price, description, created_at, updated_at) "
                            + "VALUES (UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)",
                paramMap(product));
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products", productRowMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM products", Collections.emptyMap());
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update(
                "UPDATE products SET product_name = :productName, category = :category, "
                + "price = :price, description = :description, created_at = :createdAt, updated_at = :updatedAt "
                + "WHERE product_id = UUID_TO_BIN(:productId)",
                paramMap(product));
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        List<Product> products = jdbcTemplate.query("select * from products where product_id = UUID_TO_BIN(:productId)",
                Collections.singletonMap("productId", productId.toString().getBytes()), productRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(products));
    }

    private Map<String, Object> paramMap(Product product) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", product.getProductId().toString().getBytes());
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().name());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreatedAt());
        paramMap.put("updatedAt", product.getUpdatedAt());
        return paramMap;
    }

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var description = resultSet.getString("description");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Product(productId, productName, category, price, description, createdAt, updatedAt);
    };
}
