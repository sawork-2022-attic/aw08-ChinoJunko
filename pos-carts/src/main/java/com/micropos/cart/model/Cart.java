package com.micropos.cart.model;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(fluent = true, chain = true)
public class Cart implements Serializable {
    private Integer id;
    private List<Item> items = new ArrayList<>();

    public boolean addItem(Item item) {
        return items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

}
