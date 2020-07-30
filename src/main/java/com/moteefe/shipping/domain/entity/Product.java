package com.moteefe.shipping.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import static java.util.Comparator.comparing;
import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@EqualsAndHashCode(exclude = "supplierProducts")
@Getter
@NoArgsConstructor
@ToString(exclude = "supplierProducts")
@Entity
@Table(name = "product")
public class Product implements Comparable<Product> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "product")
    private List<SupplierProduct> supplierProducts;

    @Override
    public int compareTo(final Product other) {
        return comparing(Product::getName, String::compareTo)
                .compare(this, other);
    }
}
