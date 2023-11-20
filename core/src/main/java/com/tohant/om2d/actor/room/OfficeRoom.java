package com.tohant.om2d.actor.room;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tohant.om2d.model.office.CompanyInfo;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.service.AssetService;
import lombok.Getter;

public class OfficeRoom extends Room {

    private final Type TYPE = Type.OFFICE;
    @Getter
    private final CompanyInfo companyInfo;

    public OfficeRoom(String id, CompanyInfo companyInfo, RoomInfo roomInfo, float width, float height) {
        super(id, roomInfo, width, height);
        this.companyInfo = companyInfo;
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!getRoomInfo().isUnderConstruction()) {
            batch.draw(AssetService.OFFICE_ROOM, getX(), getY());
        }
    }
}
