package com.tohant.om2d.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.*;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.dropdown.HorizontalDropdown;
import com.tohant.om2d.actor.ui.grid.NamedItemGrid;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.label.GameStandaloneLabel;
import com.tohant.om2d.actor.ui.list.AbstractList;
import com.tohant.om2d.actor.ui.list.DefaultList;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.actor.ui.pane.AbstractPane;
import com.tohant.om2d.actor.ui.pane.DefaultPane;
import com.tohant.om2d.command.room.ChooseRoomCommand;
import com.tohant.om2d.command.room.ChooseRoomTypeCommand;
import com.tohant.om2d.command.room.DestroyRoomCommand;
import com.tohant.om2d.command.ui.ToggleCommand;
import com.tohant.om2d.command.ui.ToggleGridCommand;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.CompanyEntity;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.model.entity.OfficeEntity;
import com.tohant.om2d.storage.database.CellJsonDatabase;
import com.tohant.om2d.storage.database.CompanyJsonDatabase;
import com.tohant.om2d.storage.database.LevelJsonDatabase;
import com.tohant.om2d.storage.database.OfficeJsonDatabase;
import com.tohant.om2d.util.AssetsUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.*;
import static com.tohant.om2d.storage.Cache.CURRENT_COMPANY_ID;
import static com.tohant.om2d.storage.Cache.UI_ACTORS;

public class UiActorService extends ActorService {

    private final Skin skin;

    private static UiActorService instance;

    private UiActorService() {
        this.skin = AssetsUtil.getDefaultSkin();
        initGameScreen();
    }

    public static UiActorService getInstance() {
        if (instance == null) {
            instance = new UiActorService();
        }
        return instance;
    }

    private void initGameScreen() {
        RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
        CompanyJsonDatabase companyJsonDatabase = CompanyJsonDatabase.getInstance();
        CompanyEntity companyEntity = companyJsonDatabase.getById(runtimeCache.getValue(CURRENT_COMPANY_ID)).get();
        OfficeJsonDatabase officeJsonDatabase = OfficeJsonDatabase.getInstance();
        Array<OfficeEntity> offices = officeJsonDatabase.getAllByCompanyId(companyEntity.getId());
        Array<Actor> uiActors = (Array<Actor>) runtimeCache.getObject(UI_ACTORS);
        uiActors.add(createMap(companyEntity.getId(), offices));
        uiActors.add(createBudgetLabel());
        uiActors.add(createTimeLabel());
        uiActors.add(createRoomInfoModal());
        uiActors.add(createOfficeInfoModal());
        uiActors.add(createPeopleInfoModal());
        uiActors.add(createEnvironmentModal());
        uiActors.add(createNotificationModal());
        uiActors.add(createBottomPane());
        uiActors.add(createRoomsButtonsMenu());
        uiActors.add(createToggleGridButton());
//        reorder(this.uiActors, 3, 1, 2, 0, 4, 5, 6, 7, 8, 9, 10, 11);
//        Array<AbstractUiActor> before = slice(this.uiActors, 0, 1);
//        Array<AbstractUiActor> after = slice(this.uiActors, 4, this.uiActors.size - 1);
//        this.uiActors = merge(before, after);
    }

    private GameStandaloneLabel createBudgetLabel() {
        GameStandaloneLabel budget = new GameStandaloneLabel(BUDGET_LABEL.name(), "", skin);
        budget.setPosition(DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3);
        budget.setSize(100 * budget.getFontScaleX(), 50);
        budget.setColor(Color.GREEN);
        return budget;
    }

    private GameStandaloneLabel createTimeLabel() {
        GameStandaloneLabel time = new GameStandaloneLabel(TIMELINE_LABEL.name(), "", skin);
        time.setSize(100 * time.getFontScaleX(), 50);
        time.setPosition(Gdx.graphics.getWidth() - time.getFontScaleX()
                * time.getWidth() - DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3);
        time.setColor(Color.BLACK);
        return time;
    }

    private HorizontalDropdown createRoomsButtonsMenu() {
        AbstractList roomsButtons = createRoomsButtons();
        HorizontalDropdown dropdown = new HorizontalDropdown(ROOMS_DROP_DOWN.name(), roomsButtons, createToggleRoomsMenuButton());
        dropdown.setSize(DEFAULT_PAD * 7.45f,
                200 + DEFAULT_PAD * 2 * roomsButtons.getElements().size);
        dropdown.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD * 3.9f - dropdown.getWidth(), Gdx.graphics.getHeight() / 7f);
        return dropdown;
    }

    private AbstractList createRoomsButtons() {
        Array<Actor> roomsButtons = new Array<>();
        Room.Type[] rooms = Room.Type.values();
        for (int i = 0; i < rooms.length; i++) {
            Actor room = new GameTextButton(rooms[i].name() + ROOM_BUTTON_POSTFIX,
                    new ChooseRoomTypeCommand(i), rooms[i].name(), skin);
//            int iCopy = i;
//            room.addListener(new InputListener() {
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    super.touchDown(event, x, y, pointer, button);
//                    runtimeCacheService.setValue(CURRENT_ROOM_TYPE, rooms[iCopy].name());
//                    return false;
//                }
//            });
            roomsButtons.add(room);
        }
        return new DefaultList(ROOMS_LIST.name(), roomsButtons);
    }

    private AbstractTextButton createToggleRoomsMenuButton() {
        return new GameTextButton(ROOMS_MENU_TOGGLE_BUTTON.name(), new ToggleCommand(ROOMS_LIST.name()), "<", skin);
    }

    private DefaultModal createRoomInfoModal() {
        DefaultModal modal = new DefaultModal(ROOM_INFO_MODAL.name(), "", Array.with(new GameLabel(ROOM_INFO_LABEL.name(), "", skin),
                createDestroyRoomButton()), createCloseRoomInfoButton(), skin);
        modal.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3 - DEFAULT_PAD);
        modal.toggle();
        return modal;
    }

    private AbstractTextButton createCloseRoomInfoButton() {
        return new GameTextButton(CLOSE_ROOM_INFO_BUTTON.name(), new ToggleCommand(ROOM_INFO_MODAL.name()), "X", skin);
    }

    private AbstractTextButton createCloseOfficeInfoButton() {
        return new GameTextButton(CLOSE_OFFICE_INFO_BUTTON.name(), new ToggleCommand(OFFICE_INFO_MODAL.name()), "X", skin);
    }

    private AbstractTextButton createClosePeopleInfoButton() {
        return new GameTextButton(CLOSE_PEOPLE_INFO_BUTTON.name(), new ToggleCommand(PEOPLE_INFO_MODAL.name()), "X", skin);
    }

    private AbstractTextButton createCloseEnvironmentModalButton() {
        return new GameTextButton(CLOSE_ENVIRONMENT_MODAL_BUTTON.name(), new ToggleCommand(ENVIRONMENT_MODAL.name()), "X", skin);
    }

    private AbstractTextButton createCloseNotificationButton() {
        return new GameTextButton(CLOSE_NOTIFICATION_BUTTON.name(), new ToggleCommand(NOTIFICATION_MODAL.name()), "X", skin);
    }

    private AbstractTextButton createToggleOfficeInfoButton() {
        return new GameTextButton(TOGGLE_OFFICE_INFO_BUTTON.name(), new ToggleCommand(OFFICE_INFO_MODAL.name()), "Office", skin);
    }

    private AbstractTextButton createToggleEnvironmentModalButton() {
        return new GameTextButton(TOGGLE_ENVIRONMENT_MODAL_BUTTON.name(), new ToggleCommand(ENVIRONMENT_MODAL.name()), "Environment", skin);
    }

    private AbstractTextButton createTogglePeopleInfoButton() {
        return new GameTextButton(TOGGLE_PEOPLE_INFO_BUTTON.name(), new ToggleCommand(PEOPLE_INFO_MODAL.name()), "People", skin);
    }

    private AbstractTextButton createCollapsePaneButton() {
        return new GameTextButton(COLLAPSE_BUTTON.name(), new ToggleCommand(MAIN_PANE.name()), "-", skin);
    }

    private AbstractPane createBottomPane() {
        AbstractPane pane = new DefaultPane(MAIN_PANE.name(), Array.with(createToggleOfficeInfoButton(), createTogglePeopleInfoButton(), createToggleEnvironmentModalButton()), createCollapsePaneButton(),
                AbstractPane.Alignment.BOTTOM, "Office Manager 2D", skin);
        pane.setPosition(0,0);
        return pane;
    }

    private DefaultModal createOfficeInfoModal() {
        DefaultModal modal = new DefaultModal(OFFICE_INFO_MODAL.name(), "Statistics", Array.with(new GameLabel(OFFICE_INFO_LABEL.name(), "", skin)),
                createCloseOfficeInfoButton(), skin);
        modal.setPosition(DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3 - DEFAULT_PAD);
        modal.toggle();
        return modal;
    }

    private DefaultModal createPeopleInfoModal() {
        DefaultModal modal = new DefaultModal(PEOPLE_INFO_MODAL.name(), "Staff and Residents", Array.with(new GameLabel(PEOPLE_INFO_LABEL.name(), "", skin)),
                createClosePeopleInfoButton(), skin);
        modal.setPosition(DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3 - DEFAULT_PAD);
        modal.toggle();
        return modal;
    }

    private DefaultModal createEnvironmentModal() {
        DefaultModal modal = new DefaultModal(ENVIRONMENT_MODAL.name(), "Environment",
                Array.with(new NamedItemGrid(ENVIRONMENT_MODAL_ITEM_GRID.name(), getItems())), createCloseEnvironmentModalButton(), skin);
        modal.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD * 2, Gdx.graphics.getHeight() / 2f);
        modal.setSize(modal.getPrefWidth(), modal.getPrefHeight());
        modal.toggle();
        return modal;
    }

    private Array<Item> getItems() {
        return Array.with(new Item(Items.PLANT), new Item(Items.COOLER));
    }

    private Cell createCell(int r, int c, int level, String officeId, String companyId, int x, int y) {
        return new Cell(getCellActorId(r, c, level, officeId, companyId), new ChooseRoomCommand(r, c), x, y, CELL_SIZE, CELL_SIZE);
    }

    private Map createMap(String companyId, Array<OfficeEntity> offices) {
        Map map = new Map(MAP.name());
        float width = 3000f;
        float height = 2500f;
        map.setSize(width, height);
        Background background = createBackground(width, height);
        map.addActor(background);
        map.addActor(createOffice(companyId, offices.get(0), background.getWidth(), background.getHeight()));
        return map;
    }

    private Office createOffice(String companyId, OfficeEntity officeEntity, float width, float height) {
        LevelJsonDatabase levelJsonDatabase = LevelJsonDatabase.getInstance();
        Array<LevelEntity> levels = levelJsonDatabase.getAllLevelsByOfficeId(officeEntity.getId());
        Grid grid = createLevel(levels.get(0), companyId, officeEntity.getId(), 0);
        Office office = new Office(getOfficeActorId(officeEntity.getId(), companyId), Array.with(grid));
        office.setPosition(Math.round(width / 2f - (GRID_WIDTH * CELL_SIZE) / 2f),
                Math.round(height / 2f - (GRID_HEIGHT * CELL_SIZE) / 2f));
        office.setSize(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        return office;
    }

    private Grid createLevel(LevelEntity levelEntity, String companyId, String officeId, int index) {
        CellJsonDatabase cellJsonDatabase = CellJsonDatabase.getInstance();
        Array<CellEntity> cells = cellJsonDatabase.getAllByLevelId(levelEntity.getId());
//        for (int w = 0; w < GRID_WIDTH; w++) {
//            for (int h = 0; h < GRID_HEIGHT; h++) {
//                cells.add(createCell(h, w, index, h * CELL_SIZE, w * CELL_SIZE));
//            }
//        }
        Grid grid = new Grid(getGridActorId(index, officeId, companyId));
        grid.setSize(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        grid.setPosition(0, 0);
        cells.forEach(c -> grid.addActor(createCell(c.getX(), c.getY(), index, officeId, companyId, c.getX() * CELL_SIZE, c.getY() * CELL_SIZE)));
        drawBorders(grid);
        return grid;
    }

    private Background createBackground(float width, float height) {
        return new Background(BACKGROUND.name(), width, height);
    }

    private GameTextButton createDestroyRoomButton() {
        return new GameTextButton(DESTROY_ROOM_BUTTON.name(), new DestroyRoomCommand(), "Destroy", skin);
    }

    private GameTextButton createToggleGridButton() {;
        GameTextButton toggleGridButton = new GameTextButton(TOGGLE_GRID_BUTTON.name(), new ToggleGridCommand(), "#", skin);
        toggleGridButton.getLabel().setFontScale(1.5f);
        toggleGridButton.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD * 3.68f, Gdx.graphics.getHeight() / 4.08f);
        return toggleGridButton;
    }

    private DefaultModal createNotificationModal() {
        DefaultModal notification = new DefaultModal(NOTIFICATION_MODAL.name(), "",
                Array.with(new GameLabel(NOTIFICATION_INFO_LABEL.name(), "", skin)), createCloseNotificationButton(), skin);
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

    private void drawBorders(Grid grid) {
        Pixmap pixmap = new Pixmap((int) grid.getWidth(), (int) grid.getHeight(), Pixmap.Format.RGBA8888);
        Color borderColor = new Color(Color.GRAY);
        borderColor.a = 0.5f;
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(1, 1, (int) grid.getWidth() - 1, (int) grid.getHeight() - 1);
        for (int i = 1; i <= GRID_HEIGHT; i++) {
            pixmap.drawLine(i * CELL_SIZE, 1, i * CELL_SIZE, CELL_SIZE * GRID_HEIGHT);
        }
        for (int i = 1; i <= GRID_WIDTH; i++) {
            pixmap.drawLine(1, i * CELL_SIZE, CELL_SIZE * GRID_WIDTH, i * CELL_SIZE);
        }
        grid.setTexture(new Texture(pixmap));
        pixmap.dispose();
    }

    @Override
    public Array<Actor> getUiActors() {
        return (Array<Actor>) RuntimeCacheService.getInstance().getObject(UI_ACTORS);
    }

    public enum UiComponentConstant {
        ROOMS_DROP_DOWN, ROOMS_LIST, ROOM_BUTTON_POSTFIX, ROOMS_MENU_TOGGLE_BUTTON, ROOM_INFO_MODAL,
        CLOSE_ROOM_INFO_BUTTON, CLOSE_OFFICE_INFO_BUTTON, CLOSE_PEOPLE_INFO_BUTTON, CLOSE_ENVIRONMENT_MODAL_BUTTON,
        CLOSE_NOTIFICATION_BUTTON, DESTROY_ROOM_BUTTON, NOTIFICATION_MODAL, MAIN_PANE, COLLAPSE_BUTTON, OFFICE_INFO_MODAL, TOGGLE_OFFICE_INFO_BUTTON,
        ENVIRONMENT_MODAL, PEOPLE_INFO_MODAL,
        TOGGLE_ENVIRONMENT_MODAL_BUTTON, ENVIRONMENT_MODAL_ITEM_GRID, TOGGLE_PEOPLE_INFO_BUTTON, PEOPLE_INFO_LABEL,
        OBJECT_CELL, CELL, ROOM, MAP, OFFICE, GRID, OBJECT_GRID, BACKGROUND, STAFF, TOGGLE_GRID_BUTTON, ROOM_INFO_LABEL, OFFICE_INFO_LABEL,
        NOTIFICATION_INFO_LABEL, ROAD, CAR, BUDGET_LABEL, TIMELINE_LABEL;

        public enum Items {
            PLANT(BigDecimal.valueOf(15.0f)), COOLER(BigDecimal.valueOf(35.0f));

            private final BigDecimal price;

            Items(BigDecimal price) {
                this.price = price;
            }

            public BigDecimal getPrice() {
                return price;
            }

        }

    }

//    private void reorder(Array<Actor> elements, int ...indices) {
//        assert elements.size == indices.length;
//        for (int i = 0; i < indices.length; i++) {
//            if (indices[i] >= i) {
//                elements.swap(i, indices[i]);
//            }
//        }
//    }
//
//    private Array<Actor> slice(Array<AbstractUiActor> elements, int from, int to) {
//        assert to < elements.size && from <= to;
//        Array<AbstractUiActor> result = new Array<>();
//        for (int i = from; i <= to; i++) {
//            result.add(elements.get(i));
//        }
//        return result;
//    }
//
//    private Array<AbstractUiActor> merge(Array<AbstractUiActor> first, Array<AbstractUiActor> second) {
//        Array<AbstractUiActor> result = new Array<>(first.size + second.size);
//        for (int i = 0; i < first.size; i++) {
//            result.add(first.get(i));
//        }
//        for (int i = 0; i < second.size; i++) {
//            result.add(second.get(i));
//        }
//        return result;
//    }

}
