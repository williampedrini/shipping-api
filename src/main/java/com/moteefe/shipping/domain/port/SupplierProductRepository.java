package com.moteefe.shipping.domain.port;

import com.moteefe.shipping.domain.entity.SupplierProduct;

import java.util.List;

/**
 * Represents a data access object used to access data related to {@link SupplierProduct}.
 *
 * @since 1.0.0
 */
public interface SupplierProductRepository {

    /**
     * Find all existing supplier's products by name.
     *
     * @param productName The product name.
     * @return All found products.
     */
    List<SupplierProduct> findAllByProductName(String productName);
}
