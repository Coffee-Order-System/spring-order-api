package gc.cafe.order.controller;

import static gc.cafe.order.controller.ProductController.ADMIN;
import static gc.cafe.order.controller.ProductController.NEW_PRODUCT;
import static gc.cafe.order.controller.ProductController.PRODUCTS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import gc.cafe.order.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

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
    @DisplayName("Product 등록 요청")
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

}