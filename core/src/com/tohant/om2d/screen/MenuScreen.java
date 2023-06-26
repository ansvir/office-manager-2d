package com.tohant.om2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.list.AbstractList;
import com.tohant.om2d.actor.ui.list.DefaultList;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.MenuUiActorService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.storage.JsonDatabase;
import com.tohant.om2d.storage.database.CompanyJsonDatabase;
import com.tohant.om2d.util.AssetsUtil;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.exception.GameException.Code.E400;
import static com.tohant.om2d.service.MenuUiActorService.MenuUiComponentConstant.*;
import static com.tohant.om2d.storage.Cache.GAME_EXCEPTION;
import static com.tohant.om2d.storage.Cache.READY_TO_START;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class MenuScreen implements Screen {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int MENU_BUTTON_WIDTH = BUTTON_WIDTH + DEFAULT_PAD * 10;
    private static final int MENU_BUTTON_HEIGHT = BUTTON_HEIGHT * 3 + DEFAULT_PAD * 4;

    private Game game;
    private TextButton start;
    private TextButton load;
    private Skin skin;
    private Texture background;
    private Color bgColor;
    private SpriteBatch batch;
    private Stage stage;
    private Label title;
    private Label title2;
    private TextButton exit;
    private Table menuButtons;
    private Texture menuButtonsBg;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        skin = getDefaultSkin();
        batch = new SpriteBatch();
        stage = new Stage();
        start = new TextButton("NEW GAME", skin);
        start.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                AssetService.getInstance().getChooseSound().play();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                if (CompanyJsonDatabase.getInstance().getAll().size >= 3) {
                    Array<GameException> exceptions = (Array<GameException>) RuntimeCacheService.getInstance().getObject(GAME_EXCEPTION);
                    exceptions.add(new GameException(E400));
                    RuntimeCacheService.getInstance().setObject(GAME_EXCEPTION, exceptions);
                } else {
                    new ForceToggleCommand(MENU_NEW_COMPANY_MODAL.name(), true).execute();
                }
                return false;
            }

        });
        load = new TextButton("LOAD GAME", skin);
        AbstractList savedGames = createGamesList();
        savedGames.setVisible(false);
        load.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                AssetService.getInstance().getChooseSound().play();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                savedGames.setVisible(!savedGames.isVisible());
                return false;
            }
        });
        exit = new TextButton("X", skin);
        exit.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD - exit.getWidth(), Gdx.graphics.getHeight() - DEFAULT_PAD - exit.getHeight());
        exit.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                AssetService.getInstance().getChooseSound().play();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                Gdx.app.exit();
                return false;
            }

        });
        title = new Label("Office Manager", skin);
        title.setFontScale(1.5f);
        title2 = new Label("2D", skin);
        title2.setFontScale(1.5f);
        title2.setColor(Color.RED);
        menuButtons = new Table(skin);
        menuButtonsBg = createMenuButtonsBackground();
        menuButtons.setBackground(new SpriteDrawable(new Sprite(menuButtonsBg)));
        menuButtons.setSize(MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        menuButtons.add(title).pad(DEFAULT_PAD).right();
        menuButtons.add(title2).padTop(DEFAULT_PAD).padRight(DEFAULT_PAD).padBottom(DEFAULT_PAD).center().left();
        menuButtons.row();
        menuButtons.add(start).padLeft(DEFAULT_PAD).padRight(DEFAULT_PAD).padBottom(DEFAULT_PAD).grow().center().colspan(2);
        menuButtons.row();
        if (!JsonDatabase.checkFirstInit()) {
            if (!JsonDatabase.checkDatabaseIsEmpty()) {
                menuButtons.add(load).padLeft(DEFAULT_PAD).padRight(DEFAULT_PAD).padBottom(DEFAULT_PAD).grow().center().colspan(2);
                menuButtons.row();
                menuButtons.add(savedGames);
                JsonDatabase.clearDatabase();
            }
        } else {
            JsonDatabase.init();
        }
        menuButtons.setPosition(DEFAULT_PAD * 4, DEFAULT_PAD * 4);
        bgColor = Color.valueOf("a0dcef");
        background = new Texture("bg.png");
        stage.addActor(menuButtons);
        stage.addActor(exit);
        MenuUiActorService.getInstance().getUiActors().iterator().forEach(stage::addActor);
        Gdx.input.setInputProcessor(stage);
        AssetService.getInstance().getMenuScreenBgMusic().play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(bgColor);
        processExceptions();
        checkIsReadyToStart();
        batch.begin();
        batch.draw(background, Gdx.graphics.getWidth() / 2f - background.getWidth() / 2f, background.getHeight() / -10f);
        batch.end();
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        batch.dispose();
        background.dispose();
        stage.dispose();
        menuButtonsBg.dispose();
    }

    private Texture createMenuButtonsBackground() {
        Pixmap bg = new Pixmap(MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, Pixmap.Format.RGBA8888);
        bg.setColor(Color.WHITE);
        bg.fill();
        bg.setColor(Color.ORANGE);
        Texture bgTexture = new Texture(bg);
        bg.dispose();
        return bgTexture;
    }

    private AbstractList createGamesList() {
        Array<Actor> gamesButtons = new Array<>();
        CompanyJsonDatabase companyJsonDatabase = CompanyJsonDatabase.getInstance();
        companyJsonDatabase.getAll().forEach(c -> gamesButtons.add(new GameTextButton(c.getId() + "_GAME_BUTTON",
                () -> RuntimeCacheService.getInstance().setBoolean(READY_TO_START, true), c.getName(), AssetsUtil.getDefaultSkin())));
        return new DefaultList("MENU_SAVED_GAMES_LIST", gamesButtons);
    }

    private void processExceptions() {
        try {
            checkForExceptionsAndThrowIfExist(0);
        } catch (GameException e) {
            updateNotificationModal(e);
        }
    }

    private void updateNotificationModal(GameException e) {
        MenuUiActorService menuUiActorService = MenuUiActorService.getInstance();
        DefaultModal notification = (DefaultModal) menuUiActorService.getActorById(MENU_NOTIFICATION_MODAL.name());
        notification.getTitleLabel().setText(e.getCode().getType().getTitle());
        notification.getActions().forEach(Action::reset);
        notification.addAction(sequence(alpha(1.0f), delay(4f), fadeOut(3f)));
        notification.setVisible(true);
        GameLabel label = (GameLabel) menuUiActorService.getActorById(MENU_NOTIFICATION_INFO_LABEL.name());
        label.setText(e.getCode().getMessage());
        AssetService.getInstance().getNotificationSound().play();
    }

    private void checkForExceptionsAndThrowIfExist(int i) {
        Array<GameException> exceptions = (Array<GameException>) RuntimeCacheService.getInstance().getObject(GAME_EXCEPTION);
        if (exceptions.size > 0 && i < exceptions.size) {
            checkForExceptionsAndThrowIfExist(i + 1);
            GameException e = exceptions.get(i);
            exceptions.removeIndex(i);
            throw e;
        }
    }

    private void checkIsReadyToStart() {
        if (RuntimeCacheService.getInstance().getBoolean(READY_TO_START)) {
            RuntimeCacheService.getInstance().setBoolean(READY_TO_START, false);
            AssetService.getInstance().getMenuScreenBgMusic().stop();
            game.setScreen(new GameScreen(game));
        }
    }

}
