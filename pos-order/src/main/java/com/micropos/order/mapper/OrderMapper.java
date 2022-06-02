package com.micropos.order.mapper;

import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.OrderDto;
import com.micropos.dto.ProductDto;
import com.micropos.order.model.Item;
import com.micropos.order.model.Order;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public interface OrderMapper {
    Collection<OrderDto> toOrderDtos(Collection<Order> orders);

    Collection<Order> toOrders(Collection<OrderDto> orderDtos);

    default Order toOrder(OrderDto orderDto){
        return new Order(
                toItems(orderDto.getItems()),
                orderDto.getTotal()
        );
    }

    default OrderDto toOrderDto(Order order){
        return new OrderDto().id(order.getId()).items(toItemDtos(order.getItems())).total(order.getTotal());
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
