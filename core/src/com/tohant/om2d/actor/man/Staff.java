package com.tohant.om2d.actor.man;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.UUID;

public abstract class Staff extends Actor {

    private static final String DEFAULT_NAME = "John Silvia";

    private final String id;
    private final String fullName;
    private float salary;

    public Staff(float salary) {
        this.id = UUID.randomUUID().toString();
        this.fullName = DEFAULT_NAME;
        this.salary = salary;
    }

    public Staff(String id, float salary) {
        this.id = id;
        this.fullName = DEFAULT_NAME;
        this.salary = salary;
    }

    public Staff(String id, String fullName, float salary) {
        this.id = id;
        this.fullName = fullName;
        this.salary = salary;
    }

    public abstract Type getType();

    public enum Type {
        SECURITY(1200.0f), WORKER(0.0f), CLEANING(500.0f), ADMINISTRATION(1500.0f);

        private final float salary;

        Type(float salary) {
            this.salary = salary;
        }

        public float getSalary() {
            return salary;
        }

    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

}
