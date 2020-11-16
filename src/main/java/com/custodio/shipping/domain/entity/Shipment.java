package com.custodio.shipping.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;

@EqualsAndHashCode
@Getter
@ToString
public class Shipment {
    private final LocalDate deliveryDate;
    private final Collection<ShipmentItem> items;

    public Shipment(final Collection<ShipmentItem> items) {
        final var highestDeliveryTime = items
                .stream()
                .mapToLong(ShipmentItem::getDeliveryTime)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("The delivery time is mandatory."));
        this.deliveryDate = LocalDate.now()
                .plusDays(highestDeliveryTime);
        this.items = items;
    }
}
