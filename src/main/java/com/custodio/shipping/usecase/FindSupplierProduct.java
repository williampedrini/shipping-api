package com.custodio.shipping.usecase;

import com.custodio.shipping.domain.port.SupplierProductRepository;
import com.custodio.shipping.domain.entity.Supplier;
import com.custodio.shipping.domain.entity.SupplierProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toUnmodifiableList;

@Component
@RequiredArgsConstructor
public final class FindSupplierProduct {

    private final SupplierProductRepository supplierProductRepository;

    /**
     * Search for all suppliers that delivery a certain order item for a specific region.
     *
     * @param orderItemName The order item name.
     * @param orderRegion   The order region to be delivered.
     * @return A list containing all found products per supplier.
     */
    @NotNull
    public List<SupplierProduct> findAllByOrderItemNameAndRegionSortedByBestDelivery(@NotNull final String orderItemName, @NotNull final String orderRegion) {
        final var comparator = Comparator.comparing(
                SupplierProduct::getDeliveryTimes, (deliveryTimes1, deliveryTimes2) -> {
                    final var deliveryTime1 = deliveryTimes1.get(orderRegion);
                    final var deliveryTime2 = deliveryTimes2.get(orderRegion);
                    return deliveryTime1.compareTo(deliveryTime2);
                })
                .thenComparing(SupplierProduct::getInStock, Integer::compareTo)
                .thenComparing(SupplierProduct::getSupplier, Comparator.comparing(Supplier::getName));
        return supplierProductRepository
                .findAllByProductName(orderItemName)
                .stream()
                .filter(supplierProduct -> {
                    final var deliveryTimes = supplierProduct.getDeliveryTimes();
                    return nonNull(deliveryTimes.get(orderRegion));
                })
                .sorted(comparator)
                .collect(toUnmodifiableList());
    }
}
