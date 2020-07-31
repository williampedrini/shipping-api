package com.moteefe.shipping.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.moteefe.shipping.domain.entity.Order;
import com.moteefe.shipping.domain.entity.ShipmentItem;
import com.moteefe.shipping.usecase.exception.OrderNotFulfilledException;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.moteefe.shipping.util.JSONUtil.fileToBean;
import static java.lang.String.format;
import static org.junit.Assert.assertThrows;

public class VerifyOrderFulfillmentTest {

    private static final String TEST_CASES_BASE_PATH = "/test-cases/use-case/verify-order-fulfillment/%s";

    @Test
    public void testWhenThereIsAOrderItemWithMissingAmount_shouldThrowAnOrderNotFulfilledException() {
        //then
        assertThrows(OrderNotFulfilledException.class, () -> {
            //when
            final var orderFullPath = format(TEST_CASES_BASE_PATH, "when-there-is-missing-amount/order.json");
            final var order = fileToBean(orderFullPath, Order.class);

            final var possibleShipmentItems = Collections.<ShipmentItem>emptyList();
            new VerifyOrderFulfillment(order).verify(possibleShipmentItems);
        });
    }

    @Test
    public void testWhenTheOrderWasFulfilled_shouldNotThrowAnOrderNotFulfilledException() {
        //when
        final var orderFullPath = format(TEST_CASES_BASE_PATH, "when-there-is-not-missing-amount/order.json");
        final var order = fileToBean(orderFullPath, Order.class);

        final var possibleShipmentItemsFullPath = format(TEST_CASES_BASE_PATH, "when-there-is-not-missing-amount/possibleShipmentItems.json");
        final var possibleShipmentItems = fileToBean(possibleShipmentItemsFullPath, new TypeReference<List<ShipmentItem>>() {
        });
        new VerifyOrderFulfillment(order).verify(possibleShipmentItems);
    }
}
