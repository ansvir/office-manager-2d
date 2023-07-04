package com.tohant.om2d.model.man;

public class BodyInfo {

    private final Hair hair;
    private final Body body;
    private final Skin skin;

    public BodyInfo(Hair hair, Body body, Skin skin) {
        this.hair = hair;
        this.body = body;
        this.skin = skin;
    }

    public enum Hair {
        BLOND, BROWN, DARK, GRAY
    }

    public enum Body {
        FAT, MIDDLE, THIN
    }

    public enum Skin {
        DARK, MEDIUM, LIGHT
    }

    public Hair getHair() {
        return hair;
    }

    public Body getBody() {
        return body;
    }

    public Skin getSkin() {
        return skin;
    }

}
