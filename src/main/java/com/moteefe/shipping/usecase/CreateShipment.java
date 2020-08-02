package com.moteefe.shipping.usecase;

import com.moteefe.shipping.domain.entity.Order;
import com.moteefe.shipping.domain.entity.OrderItem;
import com.moteefe.shipping.domain.entity.Shipment;
import com.moteefe.shipping.domain.entity.ShipmentItem;
import com.moteefe.shipping.domain.entity.SupplierProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * Represents an use case responsible for creating a shipment based on an {@link Order} information.
 *
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public final class CreateShipment {

    private final FindSupplierProduct findSupplierProduct;

    /**
     * Perform the creation of a shipment information based on the order's products, availability of suppliers and also delivery time.
     *
     * @param order The order containing the products to be processed.
     * @return The shipment details with the best fit scenario where the delivery time will be as quick as possible.
     */
    @NotNull
    public Shipment create(@NotNull final Order order) {
        final var shipmentItems = order.getItems()
                .stream()
                .map(orderItem -> getShipmentItemsByOrderRegionAndOrderItem(order.getRegion(), orderItem))
                .flatMap(Collection::stream)
                .collect(toUnmodifiableList());
        verifyShipmentInformation(order, shipmentItems);
        return new Shipment(shipmentItems);
    }

    /**
     * Build a {@link ShipmentItem} based on the order region, a supplier product and an order item amount.
     *
     * @param shipmentOrderInformation The object representing all the information used to build the shipment item.
     * @return The shipment item containing the possible amount provided by a supplier.
     */
    private ShipmentItem buildShipmentItem(final ShipmentOrderInformation shipmentOrderInformation) {

        final var product = shipmentOrderInformation.getSupplierProduct();
        final var orderRegion = shipmentOrderInformation.getOrderRegion();
        final var orderItem = shipmentOrderInformation.getOrderItem();

        final var deliveryTime = product
                .getDeliveryTimes()
                .get(orderRegion);
        final var currentTotalAmount = getTotalAmountPerOrderItem(shipmentOrderInformation.getShipmentItems(), orderItem);
        final var missingAmount = orderItem.getAmount() - currentTotalAmount;
        final var amountOfProductsForShipment = getAmountOfProductsForShipment(product.getInStock(), missingAmount);

        return ShipmentItem
                .builder()
                .amount(amountOfProductsForShipment)
                .supplier(product.getSupplier().getName())
                .product(product.getProduct().getName())
                .deliveryTime(deliveryTime)
                .build();
    }

    /**
     * Verifies whether an amount of product was already fulfilled or not.
     *
     * @param orderItem     The order item used as base for the comparision.
     * @param shipmentItems All the current shipment items to be delivered.
     * @return {@code true}: The shipping amount was fulfilled. <br>
     * {@code false}: The shipping amount was not fulfilled.
     */
    private boolean isShippingAmountNotFulfilled(final OrderItem orderItem, final List<ShipmentItem> shipmentItems) {
        final var desiredAmount = orderItem.getAmount();
        final var currentTotalAmount = getTotalAmountPerOrderItem(shipmentItems, orderItem);
        final var missingAmount = desiredAmount - currentTotalAmount;
        return missingAmount != 0;
    }

    /**
     * Get the amount of products to be shipped based on the relation of desired amount and stock amount.
     *
     * @param amountInStock The current amount in stock.
     * @param desiredAmount The desired amount of products to be shipped.
     * @return The final amount to be shipped.
     */
    private int getAmountOfProductsForShipment(final int amountInStock, final int desiredAmount) {
        final var productsInStockAfterShipment = amountInStock - desiredAmount;
        return productsInStockAfterShipment < 0 ? amountInStock : desiredAmount;
    }

    /**
     * Get all the shipment items used to satisfy a certain defined quantity by an order item.
     *
     * @param orderRegion The region upon which the order should be delivered.
     * @param orderItem   The object defining the quantity to be fulfilled and the product name.
     * @return All shipment items used to fulfill the order.
     */
    private Collection<ShipmentItem> getShipmentItemsByOrderRegionAndOrderItem(final String orderRegion, final OrderItem orderItem) {
        final var items = new ArrayList<ShipmentItem>();
        final var products = findSupplierProduct.findAllByOrderItemNameAndRegionSortedByBestDelivery(orderItem.getProductName(), orderRegion);

        for (var index = 0; isShippingAmountNotFulfilled(orderItem, items) && index < products.size(); index++) {
            final var product = products.get(index);
            final var shipmentOrderInformation = ShipmentOrderInformation.builder()
                    .supplierProduct(product)
                    .orderRegion(orderRegion)
                    .orderItem(orderItem)
                    .shipmentItems(items)
                    .build();
            final var shipmentItem = buildShipmentItem(shipmentOrderInformation);
            items.add(shipmentItem);
        }
        return items;
    }

    /**
     * Get the current total amount per order item.
     *
     * @param shipmentItems All current shipment items.
     * @param orderItem     The order item to be searched.
     * @return The current amount of an order item.
     */
    private int getTotalAmountPerOrderItem(final Collection<ShipmentItem> shipmentItems, final OrderItem orderItem) {
        return shipmentItems.stream()
                .filter(shipmentItem -> shipmentItem.getProduct().equals(orderItem.getProductName()))
                .mapToInt(ShipmentItem::getAmount)
                .sum();
    }

    /**
     * Performs verification against of the shipment items information to identify possible errors.
     *
     * @param order         The order containing the products to be processed.
     * @param shipmentItems The shipment items that will be used to fulfill the order requirements.
     * @throws RuntimeException If any error occurs while performing the verification.
     */
    private void verifyShipmentInformation(final Order order, final Collection<ShipmentItem> shipmentItems) {
        final var verifyOrderFulfillmentUseCase = new VerifyOrderFulfillment(order);
        verifyOrderFulfillmentUseCase.verify(shipmentItems);
    }

    /**
     * Internal helper class used to get together all information used to identify the shipment information per order item.
     */
    @Getter
    @Builder
    private static class ShipmentOrderInformation {
        private final SupplierProduct supplierProduct;
        private final String orderRegion;
        private final OrderItem orderItem;
        private final Collection<ShipmentItem> shipmentItems;
    }
}
