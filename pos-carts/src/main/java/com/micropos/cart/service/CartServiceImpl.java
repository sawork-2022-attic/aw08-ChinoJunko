package com.micropos.cart.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {


    private final String COUNTER_URL = "http://POS-COUNTER/counter-api";

    private CartMapper cartMapper;

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Cart create() {
        return new Cart();
    }

    @Override
    public Double checkout(Cart cart) {
        CartDto cartDto = cartMapper.toCartDto(cart);
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = null;
        try {
            request = new HttpEntity<>(mapper.writeValueAsString(cartDto), headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Double total = restTemplate.postForObject(COUNTER_URL+ "/counter/checkout", request, Double.class);
        return total;
    }

    public Integer test() {

        Integer test = restTemplate.getForObject(COUNTER_URL + "/test", Integer.class);
        return test;
    }

    @Override
    public Cart add(Cart cart, Item item) {
        if ( cart!=null && cart.addItem(item)){
            return cart;
        }
        return null;
    }

}
