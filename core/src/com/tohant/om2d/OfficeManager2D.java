package com.tohant.om2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.screen.GameScreen;
import com.tohant.om2d.screen.MenuScreen;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.storage.CacheImpl;
import com.tohant.om2d.storage.CacheProxy;
import com.tohant.om2d.storage.CachedEventListener;

import static com.tohant.om2d.storage.CacheImpl.*;

public class OfficeManager2D extends Game {

	private AsyncExecutor executor;
	private AsyncResult<Boolean> result;
	private AssetService assetService;

	@Override
	public void create () {
		executor = new AsyncExecutor(1);
		result = executor.submit(CachedEventListener.getInstance());
		assetService = AssetService.getInstance();
		Gdx.graphics.setCursor(assetService.getDefaultCursor());
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
