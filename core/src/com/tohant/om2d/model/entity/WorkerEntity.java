package com.tohant.om2d.model.entity;

import java.io.Serializable;

public class WorkerEntity implements Serializable {

    private String id;
    private String name;
    private float mood;
    private String type;
    private float salary;

    public WorkerEntity(String id, String name, float mood, String type, float salary) {
        this.id = id;
        this.name = name;
        this.mood = mood;
        this.type = type;
        this.salary = salary;
    }

    public WorkerEntity() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMood() {
        return mood;
    }

    public void setMood(float mood) {
        this.mood = mood;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

}
