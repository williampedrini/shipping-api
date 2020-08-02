package com.moteefe.shipping.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "Represents an item part of an order.")
public class OrderRequestBasketItemDTO {

    @NotEmpty(message = "The product name is mandatory.")
    @ApiModelProperty(position = 1, value = "The name of the product associated to an item.", required = true, example = "black_mug")
    private String product;

    @NotNull(message = "The number of products is mandatory.")
    @Min(message = "At least one product is mandatory.", value = 1)
    @ApiModelProperty(position = 2, value = "The amount of products.", required = true, example = "1")
    private Integer count;
}
