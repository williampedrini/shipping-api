package com.custodio.shipping.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.custodio.shipping.domain.entity.SupplierProduct;
import com.custodio.shipping.util.JSONUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:shipping;MODE=MySQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.liquibase.enabled=false"
})
@RunWith(SpringRunner.class)
@Transactional
public class FindSupplierProductTest {

    private static final String TEST_CASES_BASE_PATH = "/test-cases/use-case/find-supplier-product/%s";

    @Autowired
    private FindSupplierProduct underTest;

    @Test
    @Sql("classpath:test-cases/use-case/find-supplier-product/when-there-are-products-for-name-and-region/insert.sql")
    public void testWhenThereAreSupplierProductsForAnOrderItemNameAndRegion_shouldReturnAllMatches() {
        //when
        final var actual = underTest.findAllByOrderItemNameAndRegionSortedByBestDelivery("tshirt", "eu")
                .stream()
                .sorted()
                .collect(toUnmodifiableList());
        //then
        final var expectedFullPath = format(TEST_CASES_BASE_PATH, "when-there-are-products-for-name-and-region/expected.json");
        final var expected = JSONUtil.fileToBean(expectedFullPath, new TypeReference<List<SupplierProduct>>() {
        })
                .stream()
                .sorted()
                .collect(toUnmodifiableList());
        Assert.assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:test-cases/use-case/find-supplier-product/when-there-are-products-for-name-and-region/insert.sql")
    public void testWhenThereIsJustOneSupplierProductForAnOrderItemNameWithASpecificRegion_shouldReturnListContainingOnlyOneSupplierProduct() {
        //when
        final var actual = underTest.findAllByOrderItemNameAndRegionSortedByBestDelivery("tshirt", "eu")
                .stream()
                .sorted()
                .collect(toUnmodifiableList());
        //then
        final var expectedFullPath = format(TEST_CASES_BASE_PATH, "when-there-are-products-for-name-and-region/expected.json");
        final var expected = JSONUtil.fileToBean(expectedFullPath, new TypeReference<List<SupplierProduct>>() {
        })
                .stream()
                .sorted()
                .collect(toUnmodifiableList());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWhenThereAreNoyAnySupplierProductsForAnOrderItemNameAndRegion_shouldReturnEmptyList() {
        //when
        final var actual = underTest.findAllByOrderItemNameAndRegionSortedByBestDelivery("tshirt", "eu");
        //then
        assertTrue(actual.isEmpty());
    }
}
