package com.tohant.om2d.model;

import java.util.UUID;

public abstract class Identifiable {

    private final String id;

    public Identifiable() {
        this.id = UUID.randomUUID().toString();
    }

    public Identifiable(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
