package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "ROOM")
public class RoomEntity implements Serializable  {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private String type;
    @DatabaseField
    private float price;
    @DatabaseField
    private float cost;
    @DatabaseField
    private String number;
    @DatabaseField
    private int days;
    @DatabaseField
    private int months;
    @DatabaseField
    private int years;
    @ForeignCollectionField(eager = true, columnName = "worker_id")
    private Collection<WorkerEntity> workerEntities;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "resident_id")
    private ResidentEntity residentEntity;

    public RoomEntity(String type, float price, float cost, String number, int days, int months, int years, List<WorkerEntity> workerEntities) {
        this.type = type;
        this.price = price;
        this.cost = cost;
        this.number = number;
        this.days = days;
        this.months = months;
        this.years = years;
        this.workerEntities = workerEntities;
    }

    public RoomEntity(String type, float price, float cost, String number, int days, int months, int years, Collection<WorkerEntity> workerEntities, ResidentEntity residentEntity) {
        this.type = type;
        this.price = price;
        this.cost = cost;
        this.number = number;
        this.days = days;
        this.months = months;
        this.years = years;
        this.workerEntities = workerEntities;
        this.residentEntity = residentEntity;
    }

    public RoomEntity() {
        // serialization constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public Collection<WorkerEntity> getWorkerEntities() {
        return workerEntities;
    }

    public void setWorkerEntities(List<WorkerEntity> workerEntities) {
        this.workerEntities = workerEntities;
    }

    public void setWorkerEntities(Collection<WorkerEntity> workerEntities) {
        this.workerEntities = workerEntities;
    }

    public ResidentEntity getResidentEntity() {
        return residentEntity;
    }

    public void setResidentEntity(ResidentEntity residentEntity) {
        this.residentEntity = residentEntity;
    }

}
