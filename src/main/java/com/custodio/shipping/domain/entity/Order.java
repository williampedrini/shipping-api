package com.custodio.shipping.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class Order {
    private String region;
    private Collection<OrderItem> items;
}
