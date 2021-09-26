package gc.cafe.order.controller;

import static gc.cafe.order.controller.ProductController.ADMIN;
import static gc.cafe.order.controller.ProductController.NEW_PRODUCT;
import static gc.cafe.order.controller.ProductController.PRODUCTS;
import static gc.cafe.order.controller.ProductController.UPDATE_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import gc.cafe.order.common.BaseControllerTest;
import gc.cafe.order.model.Category;
import gc.cafe.order.model.Product;
import gc.cafe.order.repository.ProductRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class ProductControllerTest extends BaseControllerTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("제품 조회 페이지 요청")
    void getProductsPage() throws Exception {
        mockMvc.perform(get(ADMIN + PRODUCTS))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/products"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @DisplayName("제품 등록 페이지 요청")
    void newProductPage() throws Exception {
        mockMvc.perform(get(ADMIN + NEW_PRODUCT))
                .andDo(print())
                .andExpect(model().attributeExists("product"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/new-product"));
    }

    @Test
    @DisplayName("제품 등록 요청")
    void addProduct() throws Exception {
        String productName = "에티오피아 원두 200g";
        String category = "COFFEE_BEAN_PACKAGE";
        String price = "15000";
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";

        mockMvc.perform(post(ADMIN + NEW_PRODUCT)
                .param("productName", productName)
                .param("category", category)
                .param("price", price)
                .param("description", description))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ADMIN + PRODUCTS));
    }

    @Test
    @DisplayName("제품 수정 페이지 요청")
    void updateProductPage() throws Exception {
        UUID productId = UUID.randomUUID();
        String productName = "에티오피아 원두 200g";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        Long price = 15000L;
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";

        Product product = Product.of(productId, productName, category, price, description);
        productRepository.insert(product);

        mockMvc.perform(get(ADMIN + UPDATE_PRODUCT + "/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("admin/update-product"));
    }

    @Test
    @DisplayName("제품 수정 요청")
    void updateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        String productName = "에티오피아 원두 200g";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        Long price = 15000L;
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";

        Product product = Product.of(productId, productName, category, price, description);
        productRepository.insert(product);

        mockMvc.perform(post(ADMIN + UPDATE_PRODUCT + "/{productId}", productId.toString())
                .param("productName", "에티오피아 원두 250g")
                .param("category", category.name())
                .param("price", "20000")
                .param("description", description))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ADMIN + PRODUCTS + "/" + productId.toString()));

        Product updateProduct = productRepository.findById(productId).get();

        assertThat(updateProduct.getProductName()).isEqualTo("에티오피아 원두 250g");
        assertThat(updateProduct.getPrice()).isEqualTo(20000L);
    }

}