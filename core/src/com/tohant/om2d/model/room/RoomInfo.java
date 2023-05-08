package com.tohant.om2d.model.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.man.Staff;

public class RoomInfo {

    private Array<Staff> staff;
    private String number;
    private float price;
    private float cost;
    private long buildTime;

    public RoomInfo(Array<Staff> staff, String number, float price, float cost, long buildTime) {
        this.staff = staff;
        this.number = number;
        this.price = price;
        this.cost = cost;
        this.buildTime = buildTime;
    }

    public RoomInfo(Array<Staff> staff, float price, float cost, long buildTime) {
        this.staff = staff;
        this.number = "";
        this.price = price;
        this.cost = cost;
        this.buildTime = buildTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public Array<Staff> getStaff() {
        return staff;
    }

    public void setStaff(Array<Staff> staff) {
        this.staff = staff;
    }
}
