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
import com.tohant.om2d.util.AssetsUtil;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.*;

public class UiActorService {

    private final Array<Actor> uiActors;
    private final Skin skin;

    private static UiActorService instance;

    private UiActorService() {
        this.uiActors = new Array<>();
        this.skin = AssetsUtil.getDefaultSkin();
        init();
    }

    public static UiActorService getInstance() {
        if (instance == null) {
            instance = new UiActorService();
        }
        return instance;
    }

    private void init() {
//        this.uiActors.add(createLevel(0));
//        this.uiActors.add(createBackground());
//        this.uiActors.add(createOffice());
        this.uiActors.add(createMap());
        this.uiActors.add(createBudgetLabel());
        this.uiActors.add(createTimeLabel());
        this.uiActors.add(createRoomInfoModal());
        this.uiActors.add(createOfficeInfoModal());
        this.uiActors.add(createNotificationModal());
        this.uiActors.add(createBottomPane());
        this.uiActors.add(createRoomsButtonsMenu());
        this.uiActors.add(createToggleGridButton());
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

    private AbstractTextButton createCloseNotificationButton() {
        return new GameTextButton(CLOSE_NOTIFICATION_BUTTON.name(), new ToggleCommand(NOTIFICATION_MODAL.name()), "X", skin);
    }

    private AbstractTextButton createToggleOfficeInfoButton() {
        return new GameTextButton(TOGGLE_OFFICE_INFO_BUTTON.name(), new ToggleCommand(OFFICE_INFO_MODAL.name()), "Office", skin);
    }

    private AbstractTextButton createCollapsePaneButton() {
        return new GameTextButton(COLLAPSE_BUTTON.name(), new ToggleCommand(MAIN_PANE.name()), "-", skin);
    }
    private AbstractPane createBottomPane() {
        AbstractPane pane = new DefaultPane(MAIN_PANE.name(), Array.with(createToggleOfficeInfoButton()), createCollapsePaneButton(),
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

    private Cell createCell(int r, int c, int level, int x, int y) {
        return new Cell(CELL.name() + "#" + r + "#" + c + "#" + level, new ChooseRoomCommand(r, c), x, y, CELL_SIZE, CELL_SIZE);
    }

    private Map createMap() {
        Map map = new Map(MAP.name());
        map.setPosition(- (GRID_WIDTH * CELL_SIZE) / 2f, - (GRID_HEIGHT * CELL_SIZE) / 2f);
        float width = 2 * Gdx.graphics.getWidth() - (CELL_SIZE * GRID_WIDTH);
        float height = 2 * Gdx.graphics.getHeight() - (CELL_SIZE * GRID_HEIGHT);
        map.setSize(width, height);
        Background background = createBackground(width, height);
        map.addActor(background);
        map.addActor(createOffice(background.getWidth(), background.getHeight()));
        return map;
    }

    private Office createOffice(float width, float height) {
        Grid grid = createLevel(0);
        Office office = new Office(OFFICE.name(), Array.with(grid));
        office.setPosition(Math.round(width / 2f - (GRID_WIDTH * CELL_SIZE) / 2f),
                Math.round(height / 2f - (GRID_HEIGHT * CELL_SIZE) / 2f));
        office.setSize(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        return office;
    }

    private Grid createLevel(int index) {
        Array<Cell> cells = new Array<>();
        for (int w = 0; w < GRID_WIDTH; w++) {
            for (int h = 0; h < GRID_HEIGHT; h++) {
                cells.add(createCell(h, w, index, h * CELL_SIZE, w * CELL_SIZE));
//                Cell cell = new Cell(h * cellSize, w * cellSize, cellSize, cellSize);
//                cell.setName("Cell#" + h + "#" + w + "#" + level);
//                addCellEventHandling(cell);
//                actor.addActor(cell);
            }
        }
        Grid grid = new Grid(GRID.name() + "#" + index);
        grid.setSize(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        grid.setPosition(0, 0);
        for (Cell c : cells) {
            grid.addActor(c);
        }
        drawBorders(grid);
        return grid;
    }

    private Background createBackground(float width, float height) {
        return new Background(BACKGROUND.name(), width, height);
    }

    private GameTextButton createDestroyRoomButton() {
        return new GameTextButton(DESTROY_ROOM_BUTTON.name(), new DestroyRoomCommand(), "Destroy", skin);
    }

    private GameTextButton createToggleGridButton() {
        GameTextButton toggleGridButton = new GameTextButton(TOGGLE_GRID_BUTTON.name(), new ToggleGridCommand(), "#", skin);
        toggleGridButton.getLabel().setFontScale(1.5f);
        toggleGridButton.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD * 3.68f, Gdx.graphics.getHeight() / 4.08f);
        return toggleGridButton;
    }

    private DefaultModal createNotificationModal() {
//        AbstractUiActor notification = new DefaultModal(new ModalData(e.getCode().getType().getTitle(), e.getCode().getMessage(), Array.with()));
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

    public Array<Actor> getUiActors() {
        return this.uiActors;
    }

    public Actor getActorById(String id) {
        for (Actor c : getUiActors()) {
            if (c.getName() != null && c.getName().equals(id)) {
                return c;
            } else {
                if (c instanceof Group) {
                    Actor found = null;
                    try {
                        found = ((Group) c).findActor(id);
                    } catch (NullPointerException i) {
                    }
                    if (found != null) {
                        return found;
                    }
                }
            }
        }
        return null;
    }

    public enum UiComponentConstant {
        ROOMS_DROP_DOWN, ROOMS_LIST, ROOM_BUTTON_POSTFIX, ROOMS_MENU_TOGGLE_BUTTON, ROOM_INFO_MODAL,
        CLOSE_ROOM_INFO_BUTTON, CLOSE_OFFICE_INFO_BUTTON, CLOSE_NOTIFICATION_BUTTON,
        DESTROY_ROOM_BUTTON, NOTIFICATION_MODAL, MAIN_PANE, COLLAPSE_BUTTON, OFFICE_INFO_MODAL, TOGGLE_OFFICE_INFO_BUTTON,
        CELL, ROOM, MAP, OFFICE, GRID, BACKGROUND, STAFF, TOGGLE_GRID_BUTTON, ROOM_INFO_LABEL, OFFICE_INFO_LABEL,
        NOTIFICATION_INFO_LABEL, ROAD, CAR, BUDGET_LABEL, TIMELINE_LABEL
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