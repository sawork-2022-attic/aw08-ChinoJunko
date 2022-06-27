package com.micropos.order.controller;

import com.micropos.api.OrdersApi;
import com.micropos.dto.CartDto;
import com.micropos.dto.OrderDto;
import com.micropos.order.mapper.OrderMapper;
import com.micropos.order.model.Cart;
import com.micropos.order.model.Order;
import com.micropos.order.service.OrderService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("orders-api")
@RestController
public class OrderController implements OrdersApi {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StreamBridge streamBridge;

    @Override
    public ResponseEntity<OrderDto> createOrder(CartDto cartDto) {
        Order order = orderService.createOrder(orderMapper.toCart(cartDto));
        if (order != null) {
            OrderDto orderDto = orderMapper.toOrderDto(order);
            streamBridge.send("order-out-0", orderDto);
            return new ResponseEntity<>(orderDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<OrderDto>> listOrders() {
        List<OrderDto> orderDtos = orderMapper.toOrderDtos(orderService.listOrders());
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDto> deliverOrder(Integer orderId) {
        OrderDto orderDto = orderMapper.toOrderDto(orderService.deliverById(orderId));
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }
}
