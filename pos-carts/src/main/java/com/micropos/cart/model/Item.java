package com.micropos.cart.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
public class Item implements Serializable {
    private Integer id;
    private Integer cartId;
    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;
}
