package com.tohant.om2d.actor.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.model.office.CompanyInfo;
import com.tohant.om2d.model.room.RoomInfo;

public class OfficeRoom extends Room {

    private final Type TYPE = Type.OFFICE;
    private final CompanyInfo companyInfo;

    public OfficeRoom(String id, CompanyInfo companyInfo, RoomInfo roomInfo, float width, float height) {
        super(id, roomInfo, width, height);
        this.companyInfo = companyInfo;
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

}
