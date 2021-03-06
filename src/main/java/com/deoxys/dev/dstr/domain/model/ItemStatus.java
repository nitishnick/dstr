package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;

/**
 * Created by deoxys on 12.06.16.
 */

public class ItemStatus implements Serializable {
    private int stocked;
    private int reserved;
    private int sold;

    public ItemStatus() { }

    public ItemStatus(int stocked, int reserved, int sold) {
        this.stocked = stocked;
        this.reserved = reserved;
        this.sold = sold;
    }

    public int getStocked() {
        return stocked;
    }

    public void setStocked(int stocked) {
        this.stocked = stocked;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public void changeStocked(int quantity) {
        this.stocked += quantity;
    }

    public void changeReserved(int quantity) {
        this.reserved += quantity;
    }

    public void changeSold(int quantity) {
        this.sold += quantity;
    }

    @Override
    public String toString() {
        return "{ stocked=" + stocked +
                ", reserved=" + reserved +
                ", sold=" + sold + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemStatus that = (ItemStatus) o;

        if (reserved != that.reserved) return false;
        if (sold != that.sold) return false;
        if (stocked != that.stocked) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stocked;
        result = 31 * result + reserved;
        result = 31 * result + sold;
        return result;
    }
}