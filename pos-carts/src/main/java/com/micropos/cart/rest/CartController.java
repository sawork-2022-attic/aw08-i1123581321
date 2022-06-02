package com.micropos.cart.rest;

import com.micropos.api.CartsApi;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.cart.service.CartService;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class CartController implements CartsApi {

    private final CartService cartService;

    private final CartMapper cartMapper;

    @Autowired
    public CartController(CartService cartService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
    }

    @Override
    public ResponseEntity<CartDto> addItemToCart(Integer cartId, CartItemDto cartItemDto) {
        var cart = cartService.getCart(cartId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cart not found"));
        var cartDto = cartMapper.toCartDto(cart);
        cartService.add(cart, cartMapper.toItem(cartItemDto));
        return ResponseEntity.ok(cartMapper.toCartDto(cart));
    }

    @Override
    public ResponseEntity<CartDto> createCart(CartDto cartDto) {
        Cart cart = cartMapper.toCart(cartDto);
        cart = cartService.saveCart(cart);
        return ResponseEntity.ok(cartMapper.toCartDto(cart));
    }

    @Override
    public ResponseEntity<List<CartDto>> listCarts() {
        return ResponseEntity.ok(new ArrayList<>(cartMapper.toCartDtos(cartService.getAllCarts())));
    }

    @Override
    public ResponseEntity<CartDto> showCartById(Integer cartId) {
        var cart = cartService.getCart(cartId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cart not found"));
        return ResponseEntity.ok(cartMapper.toCartDto(cart));
    }

    @Override
    public ResponseEntity<Double> showCartTotal(Integer cartId) {

        Double total = cartService.checkout(cartId);

        if (total == -1d) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(total);
    }
}
