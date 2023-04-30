package com.tohant.om2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.tohant.om2d.screen.GameScreen;

public class OfficeManager2D extends Game {

	@Override
	public void create () {
		setScreen(new GameScreen(this));
	}

}
