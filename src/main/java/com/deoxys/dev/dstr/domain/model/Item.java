package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;
import java.util.Currency;
import java.util.Map;

public class Item implements Serializable {
    private String id;
    private String category;
    private double price;
    private Currency currency;
    private ItemStatus status;
    private Map<String, String> extendedFields;

    public Item() { }

    public Item(String id) {
        this.id = id;
    }

    public Item(String category, double price, Currency currency, ItemStatus status) {
        this.category = category;
        this.price = price;
        this.currency = currency;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setCurrency(String currencyCode) {
        currency = Currency.getInstance(currencyCode.toUpperCase());
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public boolean enoughInStock(int required) {
        return required <= status.getStocked();
    }

    public int stocked() {
        return status.getStocked();
    }

    public int reserved() {
        return status.getReserved();
    }

    public int sold() {
        return status.getSold();
    }

    public Map<String, String> getExtendedFields() {
        return extendedFields;
    }

    public void setExtendedFields(Map<String, String> extendedFields) {
        this.extendedFields = extendedFields;
    }

    @Override
    public String toString() {
        return  "{ id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", currency=" + currency +
                ", status=" + status +
                ", extendedFields=" + extendedFields + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != null ? !id.equals(item.id) : item.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
