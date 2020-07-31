package com.moteefe.shipping.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Data
@ApiModel(description = "Represents a basket which holds the items part of an order.")
public class OrderRequestBasketDTO {

    @NotEmpty(message = "At least one item is mandatory to create an order.")
    @ApiModelProperty(position = 1, value = "The items part of the order.", required = true)
    private Collection<OrderRequestBasketItemDTO> items;
}
