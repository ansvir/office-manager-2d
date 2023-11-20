package com.tohant.om2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.tohant.om2d.di.DIContainer;
import com.tohant.om2d.screen.MenuScreen;
import com.tohant.om2d.service.AssetService;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class OfficeManager2D extends Game {

    private MenuScreen startScreen;

    @SneakyThrows
    @Override
    public void create() {
        Gdx.graphics.setCursor(AssetService.DEFAULT_CURSOR);
        initBeanContainer();
        setScreen(startScreen);
    }

    private void initBeanContainer() throws IOException, InvocationTargetException, IllegalAccessException {
        DIContainer diContainer = new DIContainer(getClass());
        startScreen = diContainer.resolve(MenuScreen.class);
        startScreen.setGame(this);
    }

}
