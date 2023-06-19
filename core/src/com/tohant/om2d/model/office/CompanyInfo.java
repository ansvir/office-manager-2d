package com.tohant.om2d.model.office;

import java.util.List;

public class CompanyInfo {

    private String name;
    private List<String> workersIds;

    public CompanyInfo(String name, List<String> workersIds) {
        this.name = name;
        this.workersIds = workersIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getWorkersIds() {
        return workersIds;
    }

    public void setWorkersIds(List<String> workersIds) {
        this.workersIds = workersIds;
    }

}
