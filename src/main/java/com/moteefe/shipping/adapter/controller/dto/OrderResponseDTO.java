package com.moteefe.shipping.adapter.controller.dto;

import com.moteefe.shipping.domain.entity.Shipment;
import com.moteefe.shipping.domain.entity.ShipmentItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;

@Getter
@AllArgsConstructor
@ApiModel(description = "Represents an response for a requested order.")
public class OrderResponseDTO {

    @ApiModelProperty(position = 1, value = "The delivery date based on the highest order item delivery date.", required = true, example = "2020-01-01")
    private final LocalDate deliveryDate;

    @ApiModelProperty(position = 2, value = "All the items to be shipped.", required = true)
    private final Collection<OrderResponseShipmentDetailsDTO> shipments;

    public OrderResponseDTO(final Shipment shipment) {
        requireNonNull(shipment);
        this.deliveryDate = shipment.getDeliveryDate();
        this.shipments = shipment.getItems()
                .stream()
                .collect(groupingBy(ShipmentItem::getSupplier, toUnmodifiableList()))
                .values()
                .stream()
                .map(OrderResponseShipmentDetailsDTO::new)
                .collect(toUnmodifiableList());
    }
}
