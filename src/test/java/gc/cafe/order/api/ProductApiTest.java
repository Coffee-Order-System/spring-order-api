package gc.cafe.order.api;

import static gc.cafe.order.api.ProductApi.PREFIX;
import static gc.cafe.order.api.ProductApi.PRODUCTS;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gc.cafe.order.common.BaseControllerTest;
import gc.cafe.order.model.Category;
import gc.cafe.order.model.Product;
import gc.cafe.order.repository.ProductRepository;
import java.util.UUID;
import java.util.stream.IntStream;
import org.apache.el.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class ProductApiTest extends BaseControllerTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("제품 전체 조회 API")
    void getAllProducts() throws Exception {
        IntStream.range(0, 5)
                .forEach((i) -> {
                            UUID productId = UUID.randomUUID();
                            String productName = "에티오피아 원두 200g";
                            Category category = Category.COFFEE_BEAN_PACKAGE;
                            Long price = 15000L;
                            String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";

                            Product product = Product.of(productId, productName, category, price, description);
                            productRepository.insert(product);
                        });

        mockMvc.perform(get(PREFIX + PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..productName").exists())
                .andExpect(jsonPath("$..category").exists())
                .andExpect(jsonPath("$..description").exists())
                .andExpect(jsonPath("$..createdAt").exists())
                .andExpect(jsonPath("$..updatedAt").exists());

    }

}