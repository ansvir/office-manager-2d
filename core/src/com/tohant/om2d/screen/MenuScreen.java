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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.dropdown.AbstractDropDown;
import com.tohant.om2d.actor.ui.dropdown.HorizontalTriggerDropdown;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.list.AbstractList;
import com.tohant.om2d.actor.ui.list.DefaultList;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.MenuUiActorService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.storage.JsonDatabase;
import com.tohant.om2d.storage.database.CompanyJsonDatabase;
import com.tohant.om2d.storage.database.OfficeJsonDatabase;
import com.tohant.om2d.storage.database.ProgressJsonDatabase;
import com.tohant.om2d.util.AssetsUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.exception.GameException.Code.E400;
import static com.tohant.om2d.service.MenuUiActorService.MenuUiComponentConstant.*;
import static com.tohant.om2d.storage.Cache.*;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;
import static com.tohant.om2d.util.AssetsUtil.resizePixmap;

public class MenuScreen implements Screen {

    private static final int MENU_BUTTON_WIDTH = (int) (Gdx.graphics.getWidth() / 3f);
    private static final int MENU_BUTTON_HEIGHT = (int) (Gdx.graphics.getHeight() / 3f);

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
                if (ProgressJsonDatabase.getInstance().getAll().size >= 3) {
                    Array<GameException> exceptions = (Array<GameException>) RuntimeCacheService.getInstance().getObject(GAME_EXCEPTION);
                    exceptions.add(new GameException(E400));
                } else {
                    new ForceToggleCommand(MENU_NEW_COMPANY_MODAL.name(), true).execute();
                }
                return false;
            }

        });
        Table table = new Table();
        load = new TextButton("LOAD GAME", skin);
        AbstractList savedGames = createGamesList();
        savedGames.setVisible(false);
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
        menuButtons.add(title).pad(DEFAULT_PAD * 2f).right();
        menuButtons.add(title2).padTop(DEFAULT_PAD * 2f).padRight(DEFAULT_PAD * 2f).padBottom(DEFAULT_PAD * 2f).left();
        menuButtons.row();
        table.add(menuButtons);
        if (!JsonDatabase.checkFirstInit()) {
            if (!JsonDatabase.checkDatabaseIsEmpty()) {
                menuButtons.add(start).padLeft(DEFAULT_PAD * 2f).padRight(DEFAULT_PAD * 2f).padBottom(DEFAULT_PAD).grow().center().colspan(2);
                menuButtons.row();
                menuButtons.add(load).padLeft(DEFAULT_PAD * 2f).padRight(DEFAULT_PAD * 2f).padBottom(DEFAULT_PAD * 2f).grow().center().colspan(2);
                table.add(savedGames);
                table.setSize(menuButtons.getWidth() + savedGames.getWidth(), MENU_BUTTON_HEIGHT);
            }
        } else {
            menuButtons.add(start).padLeft(DEFAULT_PAD * 2f).padRight(DEFAULT_PAD * 2f).padBottom(DEFAULT_PAD * 2f).grow().center().colspan(2);
            menuButtons.row();
            table.setSize(MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        }
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
                if (savedGames.isVisible()) {
                    table.add(savedGames);
                } else {
                    table.getCells().removeIndex(1);
                }
                return false;
            }
        });
        table.setPosition(Gdx.graphics.getWidth() / 20f, Gdx.graphics.getHeight() / 15f);
        bgColor = Color.valueOf("a0dcef");
        background = new Texture("bg.png");
        stage.addActor(table);
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
        bg.setColor(Color.ORANGE);
        bg.fill();
        bg.setColor(Color.WHITE);
        bg.fillRectangle(10, 10, MENU_BUTTON_WIDTH - 20, MENU_BUTTON_HEIGHT - 20);
        Texture bgTexture = new Texture(bg);
        bg.dispose();
        return bgTexture;
    }

    private AbstractList createGamesList() {
        Array<Actor> gamesButtons = new Array<>();
        ProgressJsonDatabase progressJsonDatabase = ProgressJsonDatabase.getInstance();
        progressJsonDatabase.getAll().forEach(p -> Optional.ofNullable(p.getCompanyId()).ifPresent(id -> {
            CompanyJsonDatabase companyJsonDatabase = CompanyJsonDatabase.getInstance();
            companyJsonDatabase.getById(id).ifPresent(c ->
            gamesButtons.add(new GameTextButton(c.getName() + "_GAME_BUTTON",
                    () -> {
                        RuntimeCacheService.getInstance().setValue(CURRENT_COMPANY_ID, c.getId());
                        OfficeJsonDatabase officeJsonDatabase = OfficeJsonDatabase.getInstance();
                        RuntimeCacheService.getInstance().setValue(CURRENT_OFFICE_ID, officeJsonDatabase.getById(p.getOfficeId()).get().getId());
                        RuntimeCacheService.getInstance().setBoolean(READY_TO_START, true);
                    }, c.getName(), AssetsUtil.getDefaultSkin())));
        }));
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
        String message = e.getCode().getMessage();
        float maxWidth = Gdx.graphics.getWidth() / 2f;
        float maxHeight = Gdx.graphics.getHeight() / 4f;
        label.setText(e.getCode().getMessage());
        notification.setWidth(notification.getPrefWidth());
        notification.setHeight(notification.getPrefHeight());
        boolean isFitSize = notification.getWidth() <= maxWidth && notification.getHeight() <= maxHeight;
        while (!isFitSize) {
            label.setText(message.substring(0, message.length() / 2 - 1) + "\n" + message.substring(message.length() / 2 - 1));
            notification.setWidth(notification.getPrefWidth());
            notification.setHeight(notification.getPrefHeight());
            isFitSize = notification.getWidth() <= maxWidth && notification.getHeight() <= maxHeight;
        }
        notification.setPosition(Gdx.graphics.getWidth() / 2f
                - notification.getWidth() / 2f, Gdx.graphics.getHeight() - notification.getHeight() - DEFAULT_PAD);
        notification.setWidth(notification.getPrefWidth());
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
        RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
        if (runtimeCache.getBoolean(READY_TO_START)) {
            runtimeCache.setBoolean(READY_TO_START, false);
            AssetService.getInstance().getMenuScreenBgMusic().stop();
            game.setScreen(new GameScreen(game));
        }
    }

}
