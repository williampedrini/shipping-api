package com.moteefe.shipping.usecase;

import com.moteefe.shipping.domain.entity.Order;
import com.moteefe.shipping.domain.entity.OrderItem;
import com.moteefe.shipping.domain.entity.ShipmentItem;
import com.moteefe.shipping.usecase.exception.OrderNotFulfilledException;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public final class VerifyOrderFulfillment {

    private final Order order;

    /**
     * Performs verifications against a certain order item information.
     * @param possibleShipmentItems All the possible shipment items found to support the order.
     * @throws OrderNotFulfilledException If any requirement was fulfilled.
     */
    public void verify(@NotNull final Collection<ShipmentItem> possibleShipmentItems) {
        final var shipmentItemsByProduct = possibleShipmentItems
                .stream()
                .collect(groupingBy(ShipmentItem::getProduct, toSet()));
        order.getItems()
                .forEach(orderItem -> {
                    final var shipmentItems = shipmentItemsByProduct.get(orderItem.getProductName());
                    verifyIfOrderItemAmountWasFulfilled(shipmentItems, orderItem);
                });
    }

    /**
     * Verifies whether an order item amount was fulfilled or not.
     *
     * @param possibleShipmentItems All the possible shipment items found to support the order item.
     * @param orderItem             The order item containing the information of the desired amount.
     * @throws OrderNotFulfilledException If the amount was not fulfilled.
     */
    private void verifyIfOrderItemAmountWasFulfilled(final Collection<ShipmentItem> possibleShipmentItems, final OrderItem orderItem) {
        final var totalPossibleShipmentAmount = ofNullable(possibleShipmentItems)
                .orElseGet(HashSet::new)
                .stream()
                .mapToInt(ShipmentItem::getAmount)
                .sum();
        if (totalPossibleShipmentAmount != orderItem.getAmount()) {
            final var errorMessage = String.format("It is not possible to fulfill the order item %s", orderItem);
            throw new OrderNotFulfilledException(errorMessage);
        }
    }
}
