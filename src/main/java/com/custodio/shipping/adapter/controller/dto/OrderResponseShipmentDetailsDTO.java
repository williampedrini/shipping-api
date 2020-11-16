package com.custodio.shipping.adapter.controller.dto;

import com.custodio.shipping.domain.entity.ShipmentItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableList;

@Getter
@AllArgsConstructor
public class OrderResponseShipmentDetailsDTO {

    @ApiModelProperty(position = 1, value = "The name of the product's supplier.", required = true, example = "SupplierA")
    private final String supplier;

    @ApiModelProperty(position = 2, value = "The delivery date based on the highest delivery date.", required = true, example = "2020-01-01")
    private final LocalDate deliveryDate;

    @ApiModelProperty(position = 3, value = "All the items to be delivered by a supplier.", required = true)
    private final Collection<OrderResponseShipmentDetailsItemDTO> items;

    public OrderResponseShipmentDetailsDTO(final List<ShipmentItem> shipmentItems) {
        requireNonNull(shipmentItems);
        this.supplier = shipmentItems.stream()
                .map(ShipmentItem::getSupplier)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("The supplier name is mandatory."));
        this.deliveryDate = shipmentItems.stream()
                .map(ShipmentItem::getDeliveryTime)
                .map(LocalDate.now()::plusDays)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("The shipment delivery date is mandatory."));
        this.items = shipmentItems.stream()
                .map(OrderResponseShipmentDetailsItemDTO::new)
                .collect(toUnmodifiableList());
    }
}
