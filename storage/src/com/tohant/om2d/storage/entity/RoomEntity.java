package com.tohant.om2d.storage.entity;

import java.io.Serializable;

public class RoomEntity implements Serializable {

    private String id;
    private String number;
    private int cellX;
    private int cellY;
    private float price;
    private float cost;
    private long buildTime;
    private String type;
    private long currentBuildTime;

    public RoomEntity(String id, String number, int cellX, int cellY, float price, float cost, long buildTime, String type, long currentBuildTime) {
        this.id = id;
        this.number = number;
        this.cellX = cellX;
        this.cellY = cellY;
        this.price = price;
        this.cost = cost;
        this.buildTime = buildTime;
        this.type = type;
        this.currentBuildTime = currentBuildTime;
    }

    public RoomEntity() {
        // for serialization
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCurrentBuildTime() {
        return currentBuildTime;
    }

    public void setCurrentBuildTime(long currentBuildTime) {
        this.currentBuildTime = currentBuildTime;
    }

}
