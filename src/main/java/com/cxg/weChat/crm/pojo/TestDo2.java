package com.cxg.weChat.crm.pojo;

import java.io.Serializable;

public class TestDo2 implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemId;
    private String actualSales;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getActualSales() {
        return actualSales;
    }

    public void setActualSales(String actualSales) {
        this.actualSales = actualSales;
    }
}
