package com.custodio.shipping.adapter.controller.dto;

import com.custodio.shipping.domain.entity.Order;
import com.custodio.shipping.domain.entity.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static java.util.stream.Collectors.toUnmodifiableList;

@Data
@ApiModel(description = "Represents an order performed by an user.")
public class OrderRequestDTO {

    @NotEmpty(message = "The order region is mandatory.")
    @ApiModelProperty(position = 1, value = "The region upon which the item will be delivered.", required = true, example = "us")
    private String region;

    @Valid
    @NotNull(message = "The order basket cannot be empty.")
    @ApiModelProperty(position = 2, value = "The basket containing the items to be delivered.", required = true)
    private OrderRequestBasketDTO basket;

    /**
     * Converts the current request into a {@link Order}.
     *
     * @return The object containing the order data.
     */
    public Order toOrder() {
        final var orderItems = basket.getItems()
                .stream()
                .map(basketItem -> new OrderItem(basketItem.getProduct(), basketItem.getCount()))
                .collect(toUnmodifiableList());
        return new Order(region, orderItems);
    }
}
