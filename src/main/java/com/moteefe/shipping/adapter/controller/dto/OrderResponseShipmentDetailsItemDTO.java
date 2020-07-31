package com.moteefe.shipping.adapter.controller.dto;

import com.moteefe.shipping.domain.entity.ShipmentItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Getter
@AllArgsConstructor
public class OrderResponseShipmentDetailsItemDTO {

    @ApiModelProperty(position = 1, value = "The name of the product.", required = true, example = "black_mug")
    private final String title;

    @ApiModelProperty(position = 2, value = "The amount of product to be delivered.", required = true, example = "10")
    private final Integer count;

    public OrderResponseShipmentDetailsItemDTO(final ShipmentItem shipmentItem) {
        requireNonNull(shipmentItem);
        this.title = shipmentItem.getProduct();
        this.count = shipmentItem.getAmount();
    }
}
