package com.custodio.shipping.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.custodio.shipping.domain.entity.Order;
import com.custodio.shipping.domain.entity.Shipment;
import com.custodio.shipping.domain.entity.ShipmentItem;
import com.custodio.shipping.usecase.exception.OrderNotFulfilledException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.custodio.shipping.util.JSONUtil.fileToBean;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:shipping;MODE=MySQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.liquibase.enabled=false"
})
@RunWith(SpringRunner.class)
@Transactional
public class CreateShipmentTest {

    private static final String TEST_CASES_BASE_PATH = "/test-cases/use-case/create-shipment/%s";

    @Autowired
    private CreateShipment underTest;

    @Test
    @Sql("classpath:test-cases/use-case/create-shipment/when-productA-delivery-time-higher-than-productB/insert.sql")
    public void testWhenProductAWithDeliveryTimeOfThreeDaysAndProductBWithDeliveryTimeOfOneDay_shouldReturnDeliveryTimeAsThreeDaysFromNow() {
        test("when-productA-delivery-time-higher-than-productB");
    }

    @Test
    @Sql("classpath:test-cases/use-case/create-shipment/when-supplierA-delivery-time-lower-than-supplierB/insert.sql")
    public void testWhenSupplierADeliveryTimeIsLowerThanSupplierBDeliveryTime_shouldOptForSupplierA() {
        test("when-supplierA-delivery-time-lower-than-supplierB");
    }

    @Test
    @Sql("classpath:test-cases/use-case/create-shipment/when-delivery-demand-is-shared-by-suppliers/insert.sql")
    public void testWhenSupplierAAndSupplierBFulfillDemandTogether_shouldReturnBothSuppliers() {
        test("when-delivery-demand-is-shared-by-suppliers");
    }

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

    private void test(final String testResourcePath) {
        //when
        final var orderFullPath = format(TEST_CASES_BASE_PATH, testResourcePath + "/order.json");
        final var order = fileToBean(orderFullPath, Order.class);
        final var actual = underTest.create(order);

        //then
        final var expectedItemsFullPath = format(TEST_CASES_BASE_PATH, testResourcePath + "/expectedItems.json");
        final var expectedItems = fileToBean(expectedItemsFullPath, new TypeReference<List<ShipmentItem>>() {
        });
        final var expected = new Shipment(expectedItems);
        assertEquals(expected, actual);
    }
}
