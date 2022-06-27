package com.micropos.cart.rest;

import com.micropos.api.CartsApi;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.cart.service.CartService;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("carts-api")
public class CartController implements CartsApi {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public ResponseEntity<CartDto> addItemToCart(CartItemDto cartItemDto) {
        Cart cart = (Cart) httpServletRequest.getSession().getAttribute("cart");
        httpServletRequest.getSession().setAttribute("cart", cartService.add(cart, cartMapper.toItem(cartItemDto,cart.id())));
        return showCartById();
    }

    @Override
    public ResponseEntity<CartDto> createCart() {
        httpServletRequest.getSession().setAttribute("cart",cartService.create().id(httpServletRequest.getSession().getId().hashCode()));
        return ResponseEntity.ok(cartMapper.toCartDto((Cart) httpServletRequest.getSession().getAttribute("cart")));
    }

    @Override
    public ResponseEntity<CartDto> showCartById() {
        return ResponseEntity.ok(cartMapper.toCartDto((Cart) httpServletRequest.getSession().getAttribute("cart")));
    }

    @Override
    public ResponseEntity<Double> showCartTotal() {

        Double total = cartService.checkout((Cart) httpServletRequest.getSession().getAttribute("cart"));

        if (total == -1d) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(total);
    }
}
