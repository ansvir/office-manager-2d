package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class ResidentEntity implements Serializable {

    private String id;
    private String businessName;
    private Array<WorkerEntity> workerEntities;

    public ResidentEntity(String id, String businessName, Array<WorkerEntity> workerEntities) {
        this.id = id;
        this.businessName = businessName;
        this.workerEntities = workerEntities;
    }

    public ResidentEntity() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Array<WorkerEntity> getWorkerEntities() {
        return workerEntities;
    }

    public void setWorkerEntities(Array<WorkerEntity> workerEntities) {
        this.workerEntities = workerEntities;
    }

}
