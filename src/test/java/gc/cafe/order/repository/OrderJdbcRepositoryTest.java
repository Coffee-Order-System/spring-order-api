package gc.cafe.order.repository;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import com.wix.mysql.distribution.Version;
import gc.cafe.order.model.Category;
import gc.cafe.order.model.Email;
import gc.cafe.order.model.Order;
import gc.cafe.order.model.OrderItem;
import gc.cafe.order.model.OrderStatus;
import gc.cafe.order.model.Product;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderJdbcRepositoryTest {

    static EmbeddedMysql embeddedMysql;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @BeforeAll
    static void setUp() {
        var config = aMysqldConfig(Version.v8_0_11)
                .withCharset(Charset.UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        embeddedMysql = anEmbeddedMysql(config)
                .addSchema("test-order_mgmt", ScriptResolver.classPathScripts("schema.sql"))
                .start();
    }

    @AfterAll
    static void cleanUp() {
        embeddedMysql.stop();
    }

    @BeforeEach
    void beforeEach() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 상태로 조회")
    void findByOrderStatus() {
        UUID productId = UUID.randomUUID();
        String productName = "에티오피아 원두 200g";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        Long price = 15000L;
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";
        productRepository.insert(Product.of(productId, productName, category, price, description));

        OrderItem orderItem = new OrderItem(productId, Category.COFFEE_BEAN_PACKAGE, 15000L, 1);
        IntStream.range(0, 5)
                .forEach(i ->
                        orderRepository.insert(new Order(UUID.randomUUID(), new Email("tester@email.com"),
                                "서울시", "11111", 15000L, List.of(orderItem), OrderStatus.ACCEPTED,
                                LocalDateTime.now(), LocalDateTime.now())));

        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.ACCEPTED);

        assertThat(orders.size()).isEqualTo(5);

    }

    @Test
    @DisplayName("이메일과 주문상태로 조회")
    void findByEmailAndOrderStatus() {
        UUID productId = UUID.randomUUID();
        String productName = "에티오피아 원두 200g";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        Long price = 15000L;
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";
        productRepository.insert(Product.of(productId, productName, category, price, description));

        OrderItem orderItem = new OrderItem(productId, Category.COFFEE_BEAN_PACKAGE, 15000L, 1);
        Email email = new Email("tester@email.com");
        orderRepository.insert(new Order(UUID.randomUUID(), email,
                "서울시", "11111", 15000L, List.of(orderItem), OrderStatus.ACCEPTED,
                LocalDateTime.now(), LocalDateTime.now()));

        Optional<Order> order = orderRepository.findByEmailAndOrderStatus(email, OrderStatus.ACCEPTED);

        assertThat(order.get()).isNotNull();
        assertThat(order.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("주문 목록 추가")
    void updateOrder() {
        UUID productId = UUID.randomUUID();
        String productName = "에티오피아 원두 200g";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        Long price = 15000L;
        String description = "최상급 원두를 사용해 과일 잼과 오렌지 꽃의 산뜻한 향을 선사하는 프리미엄 원두입니다.";
        productRepository.insert(Product.of(productId, productName, category, price, description));

        OrderItem orderItem = new OrderItem(productId, Category.COFFEE_BEAN_PACKAGE, 15000L, 1);
        Email email = new Email("tester@email.com");
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, email,
                "서울시", "11111", 15000L, List.of(orderItem), OrderStatus.ACCEPTED,
                LocalDateTime.now(), LocalDateTime.now());
        orderRepository.insert(order);

        orderRepository.insertOrderItem(order, List.of(new OrderItem(productId, Category.COFFEE_BEAN_PACKAGE, 15000L, 2)));

        List<Order> orders = orderRepository.findAll();

        assertThat(orders.size()).isEqualTo(1);

    }
}