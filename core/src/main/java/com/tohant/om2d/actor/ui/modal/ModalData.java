package com.tohant.om2d.actor.ui.modal;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ModalData {

    private String title;
    private String text;
    private Array<Actor> actors;
    private Vector2 position;

    public ModalData(String title, String text, Array<Actor> actors, Vector2 position) {
        this.title = title;
        this.text = text;
        this.actors = actors;
        this.position = position;
    }

    public ModalData(String title, String text, Array<Actor> actors) {
        this.title = title;
        this.text = text;
        this.actors = actors;
    }

    public ModalData(String title, String text) {
        this.title = title;
        this.text = text;
        this.actors = new Array<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Array<Actor> getActors() {
        return actors;
    }

    public void setActors(Array<Actor> actors) {
        this.actors = actors;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

}
