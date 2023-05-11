package com.tohant.om2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
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
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new GameScreen(game));
            }
        });
        load = new TextButton("LOAD GAME", skin);
        exit = new TextButton("X", skin);
        exit.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD - exit.getWidth(), Gdx.graphics.getHeight() - DEFAULT_PAD - exit.getHeight());
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.exit();
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
        menuButtons.add(load).padLeft(DEFAULT_PAD).padRight(DEFAULT_PAD).padBottom(DEFAULT_PAD).grow().center().colspan(2);
        menuButtons.setPosition(DEFAULT_PAD * 4, DEFAULT_PAD * 4);
        bgColor = Color.valueOf("a0dcef");
        background = new Texture("bg.png");
        stage.addActor(menuButtons);
        stage.addActor(exit);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(bgColor);
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
}
