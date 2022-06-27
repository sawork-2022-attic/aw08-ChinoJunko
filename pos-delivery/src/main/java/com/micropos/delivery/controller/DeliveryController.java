package com.micropos.delivery.controller;

import com.micropos.api.DeliveryApi;
import com.micropos.delivery.mapper.DeliveryMapper;
import com.micropos.delivery.model.Order;
import com.micropos.delivery.service.DeliveryService;
import com.micropos.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery-api")
public class DeliveryController implements DeliveryApi {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Override
    public ResponseEntity<OrderDto> getDeliveryById(Integer deliveryId) {
        Order delivery = deliveryService.findDelivery(deliveryId);
        if (delivery != null) {
            return ResponseEntity.ok().body(deliveryMapper.toOrderDto(delivery));
        }
        return ResponseEntity.notFound().build();
    }
}
