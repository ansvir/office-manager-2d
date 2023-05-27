package com.tohant.om2d.common;

import java.util.UUID;

public abstract class Identifiable {

    private final UUID ID;

    public Identifiable(UUID id) {
        this.ID = id;
    }

    public Identifiable() {
        this.ID = UUID.randomUUID();
    }

    public UUID getId() {
        return ID;
    }

}
