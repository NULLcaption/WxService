package com.cxg.weChat.crm.pojo;


import java.io.Serializable;

public class TestDo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemId;
    private String miaDetailId;
    private String custId;
    private String actualSales;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMiaDetailId() {
        return miaDetailId;
    }

    public void setMiaDetailId(String miaDetailId) {
        this.miaDetailId = miaDetailId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getActualSales() {
        return actualSales;
    }

    public void setActualSales(String actualSales) {
        this.actualSales = actualSales;
    }

    @Override
    public String toString() {
        return "TestDo{" +
                "itemId='" + itemId + '\'' +
                ", miaDetailId='" + miaDetailId + '\'' +
                ", custId='" + custId + '\'' +
                ", actualSales='" + actualSales + '\'' +
                '}';
    }
}
