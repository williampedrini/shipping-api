package com.moteefe.shipping.adapter.repository;

import com.moteefe.shipping.domain.entity.SupplierProduct;
import com.moteefe.shipping.domain.port.SupplierProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SupplierProductJpaRepository extends JpaRepository<SupplierProduct, Long>, SupplierProductRepository {
    @Override
    List<SupplierProduct> findAllByProductName(String name);
}
