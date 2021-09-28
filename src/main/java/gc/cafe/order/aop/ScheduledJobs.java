package gc.cafe.order.aop;

import gc.cafe.order.model.Order;
import gc.cafe.order.model.OrderStatus;
import gc.cafe.order.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledJobs {

    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 0 14 * * *")
    public void readyForDelivery() {
        List<Order> orders =  orderRepository.findByOrderStatus(OrderStatus.ACCEPTED);

        orders.forEach(order -> {
            order.changeOrderStatusToDelivery();
            orderRepository.insert(order);
            log.info("{} this order status change accepted to delivery", order.getOrderId());
        });
    }
}
