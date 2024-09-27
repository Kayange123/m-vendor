package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.OrderItemRepository;
import dev.kayange.Multivendor.E.commerce.dao.OrderRepository;
import dev.kayange.Multivendor.E.commerce.dto.request.OrderItemRequest;
import dev.kayange.Multivendor.E.commerce.dto.request.OrderRequest;
import dev.kayange.Multivendor.E.commerce.entity.notification.Notification;
import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import dev.kayange.Multivendor.E.commerce.entity.payment.OrderItem;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import dev.kayange.Multivendor.E.commerce.enumeration.NotificationType;
import dev.kayange.Multivendor.E.commerce.events.NotificationEvent;
import dev.kayange.Multivendor.E.commerce.exception.ActionNotPermittedException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.service.NotificationService;
import dev.kayange.Multivendor.E.commerce.service.OrderService;
import dev.kayange.Multivendor.E.commerce.service.ProductService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import dev.kayange.Multivendor.E.commerce.utils.RandomUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void createAnOrder(OrderRequest request, Long userId) {
        var user = userService.findUserById(userId);
        var order = Order.builder()
                .orderDate(LocalDateTime.now())
                .customer(user)
                .taxAmount(request.getTaxAmount())
                .isShippingFree(request.isDeliveryAvailable())
                .orderNumber(RandomUtil.generateOrderNumber())
                .build();
        var savedOrder = orderRepository.saveAndFlush(order);

        for (OrderItemRequest item : request.getItems()){
            var product = productService.findProductById(item.productId());
            var orderItem = OrderItem.builder().order(savedOrder).product(product)
                    .price(item.price()).quantity(item.quantity()).build();
            orderItemRepository.saveAndFlush(orderItem);
        }
        var notification = Notification.builder().read(false)
                .message("Order has been placed successfully.")
                .recipient(user)
                .type(NotificationType.ORDER)
                .build();
        Notification note = notificationService.createNotification(notification);
        publisher.publishEvent(NotificationEvent.builder().id(note.getId()).build());
    }

    @Override
    public void deleteAnOrder(String orderNumber, long userId) {
        // TODO: 09/09/2024 We should not delete orders, rather mark them as INC
        var order = findOrderByOrderNumber(orderNumber);
        var user = userService.findUserById(userId);
        throw new ActionNotPermittedException("Manual deleting of an Order is prohibited");
    }

    @Override
    public Order findOrderByOrderNumber(String orderNumber) {
        var order = orderRepository.findFirstByOrderNumber(orderNumber);
        return order.orElseThrow(
                ()-> new ResourceNotFoundException("Order number " + orderNumber + " not found")
        );
    }

    @Override
    public Page<Order> findAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<Order> findUserOrders(long userId, int page, int size) {
        var user = userService.findUserById(userId);
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findUserOrders(user.getId(), pageable);
    }

    @Override
    public Page<Order> findProductOrders(long productId, int page, int size) {
        Product product = productService.findProductById(productId);
        Pageable pageable = PageRequest.of(page, size);
        return orderItemRepository.findProductOrders(product.getId(), pageable);
    }


}
