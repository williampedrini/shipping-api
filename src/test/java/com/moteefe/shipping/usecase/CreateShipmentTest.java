package com.moteefe.shipping.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.moteefe.shipping.domain.entity.Order;
import com.moteefe.shipping.domain.entity.Shipment;
import com.moteefe.shipping.domain.entity.ShipmentItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.moteefe.shipping.util.JSONUtil.fileToBean;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:delivery;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL",
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
