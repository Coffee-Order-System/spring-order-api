package gc.cafe.order.api;

import static gc.cafe.order.api.OrderApi.ORDERS;
import static gc.cafe.order.api.OrderApi.PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gc.cafe.order.common.BaseControllerTest;
import gc.cafe.order.dto.OrderDto;
import gc.cafe.order.model.Category;
import gc.cafe.order.model.Email;
import gc.cafe.order.model.Order;
import gc.cafe.order.model.OrderItem;
import gc.cafe.order.model.Product;
import gc.cafe.order.repository.OrderRepository;
import gc.cafe.order.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class OrderApiTest extends BaseControllerTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void beforeEach() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 등록 요청 API")
    void submitOrder() throws Exception {
        UUID productId = UUID.randomUUID();
        String productName = "에티오피아 원두 200g";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        Long price = 15000L;
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";

        Product product = Product.of(productId, productName, category, price, description);
        productRepository.insert(product);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(productId, category, price, 2));

        OrderDto orderDto = new OrderDto();
        orderDto.setEmail("tester@email.com");
        orderDto.setAddress("서울시 중구");
        orderDto.setPostcode("11011");
        orderDto.setOrderItems(orderItems);

        mockMvc.perform(post(PREFIX + ORDERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.postcode").exists())
                .andExpect(jsonPath("$.totalPrice").exists())
                .andExpect(jsonPath("$..orderItems").exists())
                .andExpect(jsonPath("$..category").exists());

    }

    @Test
    @DisplayName("올바르지 않은 이메일로 주문 등록 요청시 Bad Request")
    void submitOrderFailByEmail() throws Exception {
        UUID productId = UUID.randomUUID();
        String productName = "에티오피아 원두 200g";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        Long price = 15000L;
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";

        Product product = Product.of(productId, productName, category, price, description);
        productRepository.insert(product);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(productId, category, price, 2));

        OrderDto orderDto = new OrderDto();
        orderDto.setEmail("t@t");
        orderDto.setAddress("서울시 중구");
        orderDto.setPostcode("11011");
        orderDto.setOrderItems(orderItems);

        mockMvc.perform(post(PREFIX + ORDERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("같은 이메일로 주문 요청시 하나의 주문으로 처리")
    void addOrderExistEmail() throws Exception {
        UUID productId = UUID.randomUUID();
        String productName = "에티오피아 원두 200g";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        Long price = 15000L;
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";

        Product product = Product.of(productId, productName, category, price, description);
        productRepository.insert(product);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(productId, category, price, 2));

        orderRepository.insert(Order.of(UUID.randomUUID(), new Email("tester@email.com"), "서울시 중구", "11011", orderItems));

        OrderDto orderDto = new OrderDto();
        orderDto.setEmail("tester@email.com");
        orderDto.setAddress("서울시 중구");
        orderDto.setPostcode("11011");
        orderDto.setOrderItems(orderItems);

        mockMvc.perform(post(PREFIX + ORDERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.postcode").exists())
                .andExpect(jsonPath("$.totalPrice").exists())
                .andExpect(jsonPath("$..orderItems").exists())
                .andExpect(jsonPath("$..category").exists());
    }

}