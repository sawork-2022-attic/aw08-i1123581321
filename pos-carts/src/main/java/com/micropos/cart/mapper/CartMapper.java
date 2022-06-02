package com.micropos.cart.mapper;

import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.ProductDto;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public interface CartMapper {
    Collection<CartDto> toCartDtos(Collection<Cart> carts);

    Collection<Cart> toCarts(Collection<CartDto> cartDtos);

    default Cart toCart(CartDto cartDto) {
        return new Cart(
                toItems(cartDto.getItems()));
    }

    default CartDto toCartDto(Cart cart) {
        return new CartDto().id(cart.getId())
                .items(toItemDtos(cart.getItems()));
    }

    List<CartItemDto> toItemDtos(List<Item> items);

    List<Item> toItems(List<CartItemDto> itemDtos);

    default CartItemDto toItemDto(Item item) {

        return new CartItemDto().id(item.getId())
                .amount(item.getQuantity())
                .product(getProductDto(item));
    }

    default Item toItem(CartItemDto itemDto) {
        return new Item(
                itemDto.getProduct().getId(),
                itemDto.getProduct().getName(),
                itemDto.getProduct().getPrice(),
                itemDto.getAmount());
    }

    default ProductDto getProductDto(Item item) {
        return new ProductDto().id(item.getProductId())
                .name(item.getProductName())
                .price(item.getUnitPrice());
    }

}
