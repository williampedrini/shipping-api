package com.custodio.shipping.adapter.repository;

import com.custodio.shipping.domain.entity.SupplierProduct;
import com.custodio.shipping.domain.port.SupplierProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SupplierProductJpaRepository extends JpaRepository<SupplierProduct, Long>, SupplierProductRepository {
    @Override
    List<SupplierProduct> findAllByProductName(String name);
}
