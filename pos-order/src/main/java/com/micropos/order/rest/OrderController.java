package com.micropos.order.rest;

import com.micropos.api.OrdersApi;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.OrderDto;
import com.micropos.order.mapper.OrderMapper;
import com.micropos.order.model.Order;
import com.micropos.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api")
public class OrderController implements OrdersApi {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final StreamBridge streamBridge;

    @Autowired
    public OrderController(OrderRepository orderRepository, OrderMapper orderMapper, StreamBridge streamBridge) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.streamBridge = streamBridge;
    }


    private Double total(List<CartItemDto> items){
        double total = 0;
        for (CartItemDto item : items) {
            total += item.getAmount() * item.getProduct().getPrice();
        }
        return total;
    }

    @Override
    public ResponseEntity<OrderDto> createOrder(CartDto cartDto) {
        Order order = new Order(orderMapper.toItems(cartDto.getItems()), total(cartDto.getItems()));
        order = orderRepository.save(order);
        streamBridge.send("order", order);
        return ResponseEntity.ok(orderMapper.toOrderDto(order));
    }

    @Override
    public ResponseEntity<List<OrderDto>> listOrders() {
        var orders = Streamable.of(orderRepository.findAll()).toList();
        var orderDtos = Streamable.of(orderMapper.toOrderDtos(orders)).toList();
        return ResponseEntity.ok(orderDtos);
    }

    @Override
    public ResponseEntity<OrderDto> showOrderById(Integer orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(orderMapper.toOrderDto(order));
    }
}
