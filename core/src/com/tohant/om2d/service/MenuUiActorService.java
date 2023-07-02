package com.tohant.om2d.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.dropdown.AbstractDropDown;
import com.tohant.om2d.actor.ui.dropdown.VerticalInputDropDown;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.list.AbstractList;
import com.tohant.om2d.actor.ui.list.DefaultList;
import com.tohant.om2d.actor.ui.modal.AbstractModal;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.actor.ui.slide.AbstractSlideShow;
import com.tohant.om2d.actor.ui.slide.DefaultSlideShow;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.command.ui.ToggleCommand;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.model.entity.*;
import com.tohant.om2d.model.office.CompanyInfo;
import com.tohant.om2d.storage.database.*;
import com.tohant.om2d.util.AssetsUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.model.entity.CompanyEntity.Region.EUROPE;
import static com.tohant.om2d.service.MenuUiActorService.MenuUiComponentConstant.*;
import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.storage.Cache.*;

public class MenuUiActorService extends ActorService {

    private final Skin skin;

    private static MenuUiActorService instance;

    private MenuUiActorService() {
        this.skin = AssetsUtil.getDefaultSkin();
        initMenuScreen();
    }

    public static MenuUiActorService getInstance() {
        if (instance == null) {
            instance = new MenuUiActorService();
        }
        return instance;
    }

    private void initMenuScreen() {
        RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
        Array<Actor> menuUiActors = (Array<Actor>) runtimeCache.getObject(MENU_UI_ACTORS);
        menuUiActors.add(createNotificationModal());
        menuUiActors.add(createNewCompanyModal());
    }

    private DefaultModal createNotificationModal() {
        DefaultModal notification = new DefaultModal(MENU_NOTIFICATION_MODAL.name(), "",
                Array.with(new GameLabel(MENU_NOTIFICATION_INFO_LABEL.name(), "", skin)), createCloseNotificationButton(), skin);
        notification.setMovable(false);
        notification.setResizable(false);
        notification.addAction(sequence(delay(4f), fadeOut(3f)));
        notification.setWidth(Gdx.graphics.getWidth() / 4f);
        notification.setHeight(notification.getPrefHeight());
        notification.setPosition(Gdx.graphics.getWidth() / 2f
                - notification.getWidth() / 2f, Gdx.graphics.getHeight() - notification.getHeight() - DEFAULT_PAD);
        notification.setVisible(false);
        return notification;
    }

    private AbstractModal createNewCompanyModal() {
        AbstractSlideShow slideShow = new DefaultSlideShow(MENU_NEW_COMPANY_SLIDESHOW.name(), Array.with(createSetCompanyInfoSlide(),
                createDirectorHeroSlide()));
        AbstractModal modal = new DefaultModal(MENU_NEW_COMPANY_MODAL.name(), "New Company",
                Array.with(slideShow, createStartButton()), createCloseNewCompanyButton(), skin);
        modal.setMovable(false);
        modal.setResizable(false);
        modal.setWidth(Gdx.graphics.getWidth() / 3f);
        modal.setHeight(modal.getPrefHeight() + Gdx.graphics.getHeight() / 3f);
        modal.setPosition(Gdx.graphics.getWidth() / 2f
                - modal.getWidth() / 2f, Gdx.graphics.getHeight() - modal.getHeight() - modal.getHeight() / 4f);
        modal.setVisible(false);
        return modal;
    }

    private Table createSetCompanyInfoSlide() {
        Table table = new Table();
        GameLabel label = new GameLabel(MENU_NEW_COMPANY_NAME_LABEL.name(), "Name", skin);
        String placeholder = buildRandomCompanyName();
        TextField input = new TextField(placeholder, skin);
        RuntimeCacheService.getInstance().setValue(COMPANY_NAME, placeholder);
        input.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                RuntimeCacheService.getInstance().setValue(COMPANY_NAME, input.getText());
            }
        });
        input.setName(MENU_NEW_COMPANY_NAME_INPUT.name());
        Array<Actor> regionsButtons = new Array<>(Arrays.stream(CompanyEntity.Region.values())
                .map(r -> new GameTextButton("MENU_" + r.name() + "_BUTTON", () -> RuntimeCacheService.getInstance().setValue(CURRENT_REGION, r.name()),
                        r.name().replace("_", " ").charAt(0)
                                + r.name().replace("_", " ").substring(1).toLowerCase(), skin)).toArray(GameTextButton[]::new));
        RuntimeCacheService.getInstance().setValue(CURRENT_REGION, EUROPE.name());
        AbstractList regionsList = new DefaultList(MENU_NEW_COMPANY_NAME_REGIONS_LIST.name(), regionsButtons);
        AbstractDropDown regions = new VerticalInputDropDown(MENU_NEW_COMPANY_NAME_REGIONS_DROPDOWN.name(), regionsList);
        GameLabel regionLabel = new GameLabel(MENU_NEW_COMPANY_REGION_LABEL.name(), "Region", skin);
        table.add(label).left().padLeft(DEFAULT_PAD).padRight(DEFAULT_PAD);
        table.add(input).right().growX();
        table.row().padTop(DEFAULT_PAD);
        table.add(regionLabel).top().left().padLeft(DEFAULT_PAD).padRight(DEFAULT_PAD);
        table.add(regions).right();
        return table;
    }

    private Table createDirectorHeroSlide() {
        Table table = new Table();
        AssetService.Character ch = AssetService.getInstance().getCharacter();
        LinkedList<Texture> hairs = new LinkedList<>(List.of(ch.getHairIconBlondTexture(),
                ch.getHairIconDarkTexture(), ch.getHairIconBrownTexture(), ch.getHairIconGrayTexture()));
        LinkedList<AssetService.Character.Type> darkBodies = Arrays.stream(AssetService.Character.Type.values())
                .filter(b -> b.name().contains("DARK"))
                .collect(Collectors.toCollection(LinkedList::new));
        LinkedList<AssetService.Character.Type> mediumBodies = Arrays.stream(AssetService.Character.Type.values())
                .filter(b -> b.name().contains("MEDIUM"))
                .collect(Collectors.toCollection(LinkedList::new));
        LinkedList<AssetService.Character.Type> lightBodies = Arrays.stream(AssetService.Character.Type.values())
                .filter(b -> b.name().contains("LIGHT"))
                .collect(Collectors.toCollection(LinkedList::new));
        LinkedList<LinkedList<AssetService.Character.Type>> bodiesQueue = new LinkedList<>(List.of(darkBodies, mediumBodies, lightBodies));
        Texture hair = hairs.get(MathUtils.random(3));
        Texture body = bodiesQueue.get(MathUtils.random(2)).get(MathUtils.random(2)).getTexture();
        Image image = new Image();
        buildCharacterImage(hair, body, image);
        AtomicReference<Image> atomicImage = new AtomicReference<>(image);
        AtomicInteger currentIndex = new AtomicInteger(0);
        GameTextButton hairButton = new GameTextButton(MENU_NEW_COMPANY_DIRECTOR_HAIR_BUTTON.name(), () -> {
            Texture next = hairs.poll();
            buildCharacterImage(next, bodiesQueue.peek().get(currentIndex.get()).getTexture(), atomicImage.get());
            hairs.add(next);
        }, "Hair", skin);
        GameTextButton bodyButton = new GameTextButton(MENU_NEW_COMPANY_DIRECTOR_BODY_BUTTON.name(), () -> {
            if (currentIndex.get() + 1 >= bodiesQueue.size()) {
                currentIndex.set(0);
            } else {
                currentIndex.incrementAndGet();
            }
            buildCharacterImage(hairs.peek(), bodiesQueue.peek().get(currentIndex.get()).getTexture(), atomicImage.get());
        }, "Body", skin);
        GameTextButton skinButton = new GameTextButton(MENU_NEW_COMPANY_DIRECTOR_SKIN_BUTTON.name(), () -> {
            LinkedList<AssetService.Character.Type> next = bodiesQueue.poll();
            buildCharacterImage(hairs.peek(), bodiesQueue.peek().get(currentIndex.get()).getTexture(), atomicImage.get());
            bodiesQueue.add(next);
        }, "Skin", skin);
        table.add(atomicImage.get()).center().pad(DEFAULT_PAD);
        Table buttonsTable = new Table();
        buttonsTable.add(hairButton).center().grow();
        buttonsTable.row();
        buttonsTable.add(bodyButton).center().grow().padTop(DEFAULT_PAD).padBottom(DEFAULT_PAD);
        buttonsTable.row();
        buttonsTable.add(skinButton).center().grow();
        table.add(buttonsTable);
        return table;
    }

    private void buildCharacterImage(Texture hair, Texture body, Image image) {
        Pixmap result = new Pixmap(42, 134, Pixmap.Format.RGBA8888);
        if (!hair.getTextureData().isPrepared()) {
            hair.getTextureData().prepare();
        }
        if (!body.getTextureData().isPrepared()) {
            body.getTextureData().prepare();
        }
        result.drawPixmap(body.getTextureData().consumePixmap(), 0, 0);
        result.drawPixmap(hair.getTextureData().consumePixmap(), body.getWidth() / 2 - hair.getWidth() / 2 - 1, 2);
        Texture texture = new Texture(result);
        image.setDrawable(new TextureRegionDrawable(AssetsUtil.resizeTexture(texture, result.getWidth() * 3, result.getHeight() * 3)));
    }

    private AbstractTextButton createCloseNotificationButton() {
        return new GameTextButton(MENU_CLOSE_NOTIFICATION_BUTTON.name(), new ToggleCommand(MENU_NOTIFICATION_MODAL.name()), "X", skin);
    }

    private AbstractTextButton createCloseNewCompanyButton() {
        return new GameTextButton(MENU_CLOSE_NEW_COMPANY_BUTTON.name(), new ForceToggleCommand(MENU_NEW_COMPANY_MODAL.name(), false), "X", skin);
    }

    private GameTextButton createStartButton() {
        String officeId = UUID.randomUUID().toString();
        String companyId = UUID.randomUUID().toString();
        int level = 0;
        return new GameTextButton(MENU_NEW_COMPANY_START_BUTTON.name(), () -> {
            Array<CellEntity> cells = new Array<>(IntStream.range(0, GRID_HEIGHT).boxed()
                    .flatMap(r -> IntStream.range(0, GRID_WIDTH).boxed().map(c ->
                                    new CellEntity(getCellActorId(r, c, level, officeId, companyId), null, null, r, c, Array.with())))
                    .toArray(CellEntity[]::new));
            LevelEntity levelEntity = new LevelEntity(getGridActorId(level, officeId, companyId), level, cells);
            OfficeEntity officeEntity = new OfficeEntity(getOfficeActorId(officeId, companyId),
                    RuntimeCacheService.getInstance().getValue(COMPANY_NAME), 0.0f, 2000.0f, Array.with(levelEntity), Array.with());
            CompanyEntity companyEntity = new CompanyEntity(getCompanyActorId(companyId),
                    RuntimeCacheService.getInstance().getValue(COMPANY_NAME), Array.with(officeEntity),
                    CompanyEntity.Region.valueOf(RuntimeCacheService.getInstance().getValue(CURRENT_REGION)));
            ProgressEntity progressEntity = new ProgressEntity(UUID.randomUUID().toString(), companyEntity, officeEntity, levelEntity);
            ProgressJsonDatabase.getInstance().save(progressEntity);
            RuntimeCacheService.getInstance().setValue(CURRENT_PROGRESS_ID, progressEntity.getId());
            RuntimeCacheService.getInstance().setBoolean(READY_TO_START, true);
        }, "Start", skin);
    }

    @Override
    public Array<Actor> getUiActors() {
        return (Array<Actor>) RuntimeCacheService.getInstance().getObject(MENU_UI_ACTORS);
    }

    public enum MenuUiComponentConstant {
        MENU_NOTIFICATION_MODAL, MENU_NOTIFICATION_INFO_LABEL, MENU_CLOSE_NOTIFICATION_BUTTON,
        MENU_NEW_COMPANY_MODAL, MENU_CLOSE_NEW_COMPANY_BUTTON,
        MENU_NEW_COMPANY_SLIDESHOW,
        MENU_NEW_COMPANY_NAME_LABEL, MENU_NEW_COMPANY_NAME_INPUT, MENU_NEW_COMPANY_NAME_REGIONS_LIST, MENU_NEW_COMPANY_REGION_LABEL,
        MENU_NEW_COMPANY_NAME_REGIONS_DROPDOWN,
        MENU_NEW_COMPANY_DIRECTOR_IMAGE, MENU_NEW_COMPANY_DIRECTOR_HAIR_BUTTON, MENU_NEW_COMPANY_DIRECTOR_SKIN_BUTTON,
        MENU_NEW_COMPANY_DIRECTOR_BODY_BUTTON,
        MENU_NEW_COMPANY_START_BUTTON
    }

}
