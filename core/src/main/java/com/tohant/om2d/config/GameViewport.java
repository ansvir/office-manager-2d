package com.tohant.om2d.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tohant.om2d.di.annotation.Component;

@Component
public class GameViewport extends FitViewport {

    public GameViewport() {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

}
