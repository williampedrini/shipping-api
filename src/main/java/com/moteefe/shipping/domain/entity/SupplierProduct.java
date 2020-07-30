package com.moteefe.shipping.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Map;

import static java.util.Comparator.comparing;
import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "supplier_product",
        uniqueConstraints = @UniqueConstraint(name = "supplier_product_uk", columnNames = {"supplier_id", "product_id"}))
public class SupplierProduct implements Comparable<SupplierProduct> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Lob
    @Column(name = "delivery_times", nullable = false)
    private String deliveryTimes;

    @Column(name = "in_stock", nullable = false)
    private Integer inStock;

    public Map<String, Integer> getDeliveryTimes() {
        try {
            final var mapper = new ObjectMapper();
            return mapper.readValue(this.deliveryTimes, new TypeReference<>() {
            });
        } catch (final JsonProcessingException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public int compareTo(final SupplierProduct other) {
        return comparing(SupplierProduct::getId, Long::compareTo)
                .thenComparing(SupplierProduct::getProduct, comparing(Product::getName))
                .compare(this, other);
    }
}
