package com.tohant.om2d.model.entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "CELL")
public class CellEntity extends AbstractActorEntity {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private String roomId;
    @DatabaseField
    private String roomType;
    @DatabaseField
    private int x;
    @DatabaseField
    private int y;
    @ForeignCollectionField(eager = true, columnName = "cell_id")
    private Collection<ObjectCellEntity> objectCellEntities;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "room_id")
    private RoomEntity roomEntity;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "level_id")
    private LevelEntity levelEntity;

    public CellEntity(String id, String roomId, String roomType, int x, int y, List<ObjectCellEntity> objectCellEntities, RoomEntity roomEntity) throws SQLException {
        super(id);
        this.roomId = roomId;
        this.roomType = roomType;
        this.x = x;
        this.y = y;
        this.objectCellEntities = objectCellEntities;
        this.roomEntity = roomEntity;
    }

    public CellEntity(String id, String roomId, String roomType, int x, int y, List<ObjectCellEntity> objectCellEntities) {
        super(id);
        this.roomId = roomId;
        this.roomType = roomType;
        this.x = x;
        this.y = y;
        this.objectCellEntities = objectCellEntities;
    }

    public CellEntity(String id, String roomId, String roomType, int x, int y, Collection<ObjectCellEntity> objectCellEntities, RoomEntity roomEntity, LevelEntity levelEntity) {
        super(id);
        this.roomId = roomId;
        this.roomType = roomType;
        this.x = x;
        this.y = y;
        this.objectCellEntities = objectCellEntities;
        this.roomEntity = roomEntity;
        this.levelEntity = levelEntity;
    }

    public CellEntity() {
        super(null);
        // serialization constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Collection<ObjectCellEntity> getObjectCellEntities() {
        return objectCellEntities;
    }

    public void setObjectCellEntities(List<ObjectCellEntity> objectCellEntities) {
        this.objectCellEntities = objectCellEntities;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

    public void setObjectCellEntities(Collection<ObjectCellEntity> objectCellEntities) {
        this.objectCellEntities = objectCellEntities;
    }

    public LevelEntity getLevelEntity() {
        return levelEntity;
    }

    public void setLevelEntity(LevelEntity levelEntity) {
        this.levelEntity = levelEntity;
    }

}
