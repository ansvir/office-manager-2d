package com.tohant.om2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.screen.GameScreen;
import com.tohant.om2d.screen.MenuScreen;
import com.tohant.om2d.storage.CachedEventListener;

public class OfficeManager2D extends Game {

	private AsyncExecutor executor;
	private AsyncResult<Boolean> result;

	@Override
	public void create () {
		executor = new AsyncExecutor(1);
		result = executor.submit(CachedEventListener.getInstance());
		setScreen(new GameScreen(this));
	}

	@Override
	public void dispose() {
		CachedEventListener.getInstance().stop();
		if (result.get()) {
			System.out.println("[LOG] EVENT LISTENER SUCCESSFULLY STOPPED");
		}
		executor.dispose();
	}

}
