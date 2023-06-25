package com.tohant.om2d.model.office;

public class OfficeInfo {

    private String businessName;
    private float popularity;

    public OfficeInfo(String businessName) {
        this.businessName = businessName;
        this.popularity = 0.0f;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

}
