package com.moteefe.shipping.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class OrderItem {
    private String productName;
    private Integer amount;
}
