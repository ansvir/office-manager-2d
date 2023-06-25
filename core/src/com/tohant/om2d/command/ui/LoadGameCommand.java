package com.tohant.om2d.command.ui;

import com.badlogic.gdx.Game;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.screen.GameScreen;

public class LoadGameCommand extends AbstractCommand {

    private final Game game;

    public LoadGameCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.setScreen(new GameScreen(game));
    }

}
