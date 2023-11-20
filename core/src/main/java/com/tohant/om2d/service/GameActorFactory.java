package com.tohant.om2d.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.*;
import com.tohant.om2d.actor.man.CaffeStaff;
import com.tohant.om2d.actor.man.CleaningStaff;
import com.tohant.om2d.actor.man.SecurityStaff;
import com.tohant.om2d.actor.man.WorkerStaff;
import com.tohant.om2d.actor.room.*;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.dropdown.HorizontalTriggerDropdown;
import com.tohant.om2d.actor.ui.grid.NamedItemGrid;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.label.GameStandaloneLabel;
import com.tohant.om2d.actor.ui.list.AbstractList;
import com.tohant.om2d.actor.ui.list.DefaultList;
import com.tohant.om2d.actor.ui.modal.AbstractModal;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.actor.ui.pane.AbstractPane;
import com.tohant.om2d.actor.ui.pane.DefaultPane;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.command.item.PickItemCommand;
import com.tohant.om2d.command.office.BuildNewOfficeCommand;
import com.tohant.om2d.command.room.ChooseRoomCommand;
import com.tohant.om2d.command.room.DestroyRoomCommand;
import com.tohant.om2d.command.ui.CreateWorldModalCommand;
import com.tohant.om2d.command.ui.ToggleGridCommand;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.entity.*;
import com.tohant.om2d.model.office.CompanyInfo;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.model.task.TimeLineDate;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.ProgressDao;
import com.tohant.om2d.util.AssetsUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.model.Region.EUROPE;
import static com.tohant.om2d.service.CommonService.buildRandomCompanyName;
import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.*;
import static com.tohant.om2d.storage.cache.GameCache.*;

@Component
@RequiredArgsConstructor
public class GameActorFactory extends AbstractActorFactory {

    private final GameCache gameCache;
    private final CommonService commonService;
    private final ProgressDao progressDao;
    private final GameActorSearchService gameActorSearchService;

    private final BuildNewOfficeCommand buildNewOfficeCommand;
    private final DestroyRoomCommand destroyRoomCommand;
    private final ChooseRoomCommand chooseRoomCommand;
    private final CreateWorldModalCommand createWorldModalCommand;
    private final PickItemCommand pickItemCommand;
    private final ToggleGridCommand toggleGridCommand;

    private Skin skin;

    @PostConstruct
    public void init() {
        this.skin = AssetsUtil.getDefaultSkin();
    }

    public void initGameScreen() {
        ProgressEntity progressEntity = progressDao.queryForId(UUID.fromString(gameCache.getValue(GameCache.CURRENT_PROGRESS_ID)));
        String officeId = gameCache.getValue(GameCache.CURRENT_OFFICE_ID);
        Array<Actor> uiActors = new Array<>();
        uiActors.add(createMap(progressEntity.getCompanyEntity().getOfficeEntities().stream()
                .filter(o -> o.getId().toString().equals(officeId)).findFirst().get()));
        uiActors.add(createBudgetLabel());
        uiActors.add(createTimeLabel());
        uiActors.add(createRoomInfoModal());
        uiActors.add(createOfficeInfoModal());
        uiActors.add(createPeopleInfoModal());
        uiActors.add(createEnvironmentModal());
        createWorldModalCommand.execute();
        uiActors.add(createNotificationModal());
        uiActors.add(createBottomPane());
        uiActors.add(createRoomsButtonsMenu());
        uiActors.add(createToggleGridButton());
        uiActors.add(createBuildNewOfficeModal());
        gameCache.setObject(GAME_ACTORS, uiActors);
        postInit(gameCache);
    }

    public GameStandaloneLabel createBudgetLabel() {
        GameStandaloneLabel budget = new GameStandaloneLabel(BUDGET_LABEL.name(), "", skin);
        budget.setPosition(DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3);
        budget.setSize(100 * budget.getFontScaleX(), 50);
        Color color = Color.GREEN;
        color.a = 1.0f;
        budget.setColor(color);
        return budget;
    }

    public GameStandaloneLabel createTimeLabel() {
        GameStandaloneLabel time = new GameStandaloneLabel(TIMELINE_LABEL.name(), "", skin);
        time.setSize(100 * time.getFontScaleX(), 50);
        time.setPosition(Gdx.graphics.getWidth() - time.getFontScaleX()
                * time.getWidth() - DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3);
        Color color = Color.BLACK;
        color.a = 1.0f;
        time.setColor(color);
        return time;
    }

    public HorizontalTriggerDropdown createRoomsButtonsMenu() {
        AbstractList roomsButtons = createRoomsButtons();
        HorizontalTriggerDropdown dropdown = new HorizontalTriggerDropdown(ROOMS_DROP_DOWN.name(), roomsButtons, createToggleRoomsMenuButton(), HorizontalTriggerDropdown.TriggerButtonType.RIGHT_BOTTOM, true);
        dropdown.setSize(dropdown.getOptions().getElements().get(0).getWidth() + dropdown.getTriggerButton().getWidth() + DEFAULT_PAD * 5f,
                dropdown.getOptions().getElements().get(0).getHeight() + DEFAULT_PAD * 3 * roomsButtons.getElements().size);
        dropdown.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD * 2f - dropdown.getWidth(), Gdx.graphics.getHeight() / 8f);
        return dropdown;
    }

    public AbstractList createRoomsButtons() {
        Array<Actor> roomsButtons = new Array<>();
        Room.Type[] rooms = Room.Type.values();
        for (int i = 0; i < rooms.length; i++) {
            final int finalI = i;
            Actor room = new GameTextButton(rooms[i].name() + ROOM_BUTTON_POSTFIX,
                    () -> gameCache.setValue(CURRENT_ROOM_TYPE, rooms[finalI].name()),
                    rooms[i].name(), skin);
            roomsButtons.add(room);
        }
        return new DefaultList(ROOMS_LIST.name(), roomsButtons);
    }

    public GameTextButton createToggleRoomsMenuButton() {
        return new GameTextButton(ROOMS_MENU_TOGGLE_BUTTON.name(), () -> ((ToggleActor) GameActorSearchService.getActorById(ROOMS_LIST.name())).toggle(), "<", skin);
    }

    public DefaultModal createRoomInfoModal() {
        DefaultModal modal = new DefaultModal(ROOM_INFO_MODAL.name(), "", Array.with(new GameLabel(ROOM_INFO_LABEL.name(), "", skin),
                createDestroyRoomButton()), skin);
        modal.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3 - DEFAULT_PAD);
        modal.forceToggle(false);
        return modal;
    }

    public GameTextButton createToggleOfficeInfoButton(AbstractModal modal) {
        return new GameTextButton(TOGGLE_OFFICE_INFO_BUTTON.name(), modal::toggle, "Office", skin);
    }

    public GameTextButton createToggleEnvironmentModalButton(AbstractModal modal) {
        return new GameTextButton(TOGGLE_ENVIRONMENT_MODAL_BUTTON.name(), modal::toggle, "Environment", skin);
    }

    public GameTextButton createTogglePeopleInfoButton(AbstractModal modal) {
        return new GameTextButton(TOGGLE_PEOPLE_INFO_BUTTON.name(), modal::toggle, "People", skin);
    }

    public GameTextButton createToggleWorldModalButton(AbstractModal modal) {
        return new GameTextButton(TOGGLE_WORLD_MODAL_BUTTON.name(), modal::toggle, "World", skin);
    }

    public GameTextButton createToggleAllWindowsButton() {
        return new GameTextButton(TOGGLE_ALL_WINDOWS_BUTTON.name(), () -> {
            Actor[] actors = new Actor[]{GameActorSearchService.getActorById(OFFICE_INFO_MODAL.name()),
                    GameActorSearchService.getActorById(ENVIRONMENT_MODAL.name()),
                    GameActorSearchService.getActorById(PEOPLE_INFO_MODAL.name()),
                    GameActorSearchService.getActorById(WORLD_MODAL.name())};
            boolean isVisible = Arrays.stream(actors).anyMatch(Actor::isVisible);
            Arrays.stream(actors).forEach(a -> a.setVisible(!isVisible));
        }, "VIEW", skin);
    }

    public AbstractPane createBottomPane() {
        AbstractModal officeInfo = (AbstractModal) GameActorSearchService.getActorById(OFFICE_INFO_MODAL.name());
        AbstractModal peopleInfo = (AbstractModal) GameActorSearchService.getActorById(PEOPLE_INFO_MODAL.name());
        AbstractModal environment = (AbstractModal) GameActorSearchService.getActorById(ENVIRONMENT_MODAL.name());
        AbstractModal world = (AbstractModal) GameActorSearchService.getActorById(WORLD_MODAL.name());
        AbstractPane pane = new DefaultPane(MAIN_PANE.name(), Array.with(createToggleOfficeInfoButton(officeInfo), createTogglePeopleInfoButton(peopleInfo),
                createToggleEnvironmentModalButton(environment), createToggleWorldModalButton(world), createToggleAllWindowsButton()),
                AbstractPane.Alignment.BOTTOM, "Office Manager 2D", skin);
        pane.setPosition(0, 0);
        return pane;
    }

    public DefaultModal createOfficeInfoModal() {
        DefaultModal modal = new DefaultModal(OFFICE_INFO_MODAL.name(), "Statistics", Array.with(new GameLabel(OFFICE_INFO_LABEL.name(), "", skin)), skin);
        modal.setPosition(DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3 - DEFAULT_PAD);
        modal.forceToggle(false);
        return modal;
    }

    public DefaultModal createPeopleInfoModal() {
        DefaultModal modal = new DefaultModal(PEOPLE_INFO_MODAL.name(), "Staff and Residents", Array.with(new GameLabel(PEOPLE_INFO_LABEL.name(), "", skin)), skin);
        modal.setPosition(DEFAULT_PAD, Gdx.graphics.getHeight() - DEFAULT_PAD * 3 - DEFAULT_PAD);
        modal.forceToggle(false);
        return modal;
    }

    public DefaultModal createEnvironmentModal() {
        NamedItemGrid namedItemGrid = new NamedItemGrid(ENVIRONMENT_MODAL_ITEM_GRID.name(), getItems());
        DefaultModal modal = new DefaultModal(ENVIRONMENT_MODAL.name(), "Environment",
                Array.with(namedItemGrid), skin);
        modal.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD * 2, Gdx.graphics.getHeight() / 2f);
        modal.setSize(modal.getPrefWidth(), modal.getPrefHeight());
        modal.forceToggle(false);
        setItemListener(namedItemGrid);
        return modal;
    }

    private void setItemListener(NamedItemGrid namedItemGrid) {
        Array<Item> items = namedItemGrid.getItems();
        int columns = (int) namedItemGrid.getGridSize(items.size).x;
        int rows = (int) namedItemGrid.getGridSize(items.size).y;
        for (int i = 1; i <= rows; i++) {
            for (int j = 0; j < columns && j * i < items.size; j++) {
                Table itemLabel = new Table();
                Item next = items.get(i * j);
                itemLabel.add(next);
                itemLabel.row().pad(DEFAULT_PAD);
                String itemName = next.getType().name().charAt(0)
                        + next.getType().name().substring(1).toLowerCase();
                String itemPrice = Math.round(next.getType().getPrice().floatValue()) + " $";
                GameLabel itemNameLabel = new GameLabel(next.getType().name() + "_ITEM_NAME_LABEL", itemName, AssetsUtil.getDefaultSkin());
                GameLabel itemPriceLabel = new GameLabel(next.getType().name() + "_ITEM_PRICE_LABEL", itemPrice, AssetsUtil.getDefaultSkin());
                itemLabel.add(itemNameLabel).center();
                itemLabel.row().pad(-DEFAULT_PAD);
                itemLabel.add(itemPriceLabel).center();
                next.addListener(new InputListener() {

                    private boolean isScaling;

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event, x, y, pointer, button);
                        gameCache.setObject(CURRENT_ITEM, next);
                        pickItemCommand.execute();
                        return true;
                    }

                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        super.enter(event, x, y, pointer, fromActor);
                        if (!isScaling) {
                            next.clearActions();
                            itemNameLabel.clearActions();
                            itemPriceLabel.clearActions();
                            next.addAction(Actions.parallel(Actions.moveBy(-next.getWidth() / 6f, -next.getHeight() / 6f, 0.01f), Actions.scaleBy(0.3f, 0.3f, 0.01f)));
                            itemNameLabel.addAction(Actions.parallel(Actions.moveBy(0f, -next.getHeight() / 6f, 0.01f)));
                            itemPriceLabel.addAction(Actions.parallel(Actions.moveBy(0f, -next.getHeight() / 6f, 0.01f)));
                            isScaling = true;
                        }
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        super.exit(event, x, y, pointer, toActor);
                        if (isScaling) {
                            next.clearActions();
                            itemNameLabel.clearActions();
                            itemPriceLabel.clearActions();
                            next.addAction(Actions.parallel(Actions.moveBy(next.getWidth() / 6f, next.getHeight() / 6f, 0.01f), Actions.scaleBy(-0.3f, -0.3f, 0.01f)));
                            itemNameLabel.addAction(Actions.parallel(Actions.moveBy(0f, next.getHeight() / 6f, 0.01f)));
                            itemPriceLabel.addAction(Actions.parallel(Actions.moveBy(0f, next.getHeight() / 6f, 0.01f)));
                            isScaling = false;
                        }
                    }
                });
                namedItemGrid.add(itemLabel).center().pad(DEFAULT_PAD);
            }
            namedItemGrid.row();
        }
    }

    private Array<Item> getItems() {
        return Array.with(new Item(Items.PLANT), new Item(Items.COOLER));
    }

    public DefaultModal createBuildNewOfficeModal() {
        Table table = new Table();
        GameLabel label = new GameLabel(BUILD_NEW_OFFICE_NAME_LABEL.name(), "Name", skin);
        String placeholder = buildRandomCompanyName();
        TextField input = new TextField(placeholder, skin);
        gameCache.setValue(COMPANY_NAME, placeholder);
        input.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameCache.setValue(COMPANY_NAME, input.getText());
            }
        });
        input.setName(BUILD_NEW_OFFICE_NAME_INPUT.name());
        gameCache.setValue(CURRENT_REGION, EUROPE.name());
        table.add(label).left().padLeft(DEFAULT_PAD).padRight(DEFAULT_PAD);
        table.add(input).right().growX();
        table.row().padTop(DEFAULT_PAD);
        table.add(createConfirmBuildNewOfficeButton()).center();
        table.row().padTop(DEFAULT_PAD);
        DefaultModal modal = new DefaultModal(BUILD_NEW_OFFICE_MODAL.name(), "New Office",
                Array.with(table), skin);
        modal.setPosition(Gdx.graphics.getWidth() - DEFAULT_PAD * 2, Gdx.graphics.getHeight() / 2f);
        modal.setSize(modal.getPrefWidth(), modal.getPrefHeight());
        modal.forceToggle(false);
        return modal;
    }

    public GameTextButton createConfirmBuildNewOfficeButton() {
        return new GameTextButton(BUILD_NEW_OFFICE_CONFIRM_BUTTON.name(), buildNewOfficeCommand, "Confirm", skin);
    }

    public Cell createCell(CellEntity cellEntity) {
        Cell cell = new Cell(cellEntity.getId().toString(),
                AssetService.ACTIVE_EMPTY_CELL, cellEntity.getX() * CELL_SIZE,
                cellEntity.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        if (cellEntity.getRoomEntity() != null) {
            Room room = createRoom(cellEntity.getRoomEntity());
            String items = cellEntity.getItems();
            commonService.addObjectCellsAndStaff(cell, room, items);
            cell.addActor(room);
        }
        cell.setEmpty(!commonService.isBuilt(cell));
        setCellListener(cell, chooseRoomCommand);
        return cell;
    }

    private void setCellListener(Cell cell, Command command) {
        cell.addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                cell.setActive(true);
                if (!cell.isEmpty() && cell.hasChildren()) {
                    cell.getChildren().iterator().forEach(c -> {
                        if (c instanceof ObjectCell) {
                            ((ObjectCell) c).setGridVisible(false);
                        }
                    });
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                cell.setActive(false);
                if (cell.isEmpty() && cell.hasChildren()) {
                    cell.getChildren().iterator().forEach(c -> {
                        if (c instanceof ObjectCell) {
                            ((ObjectCell) c).setGridVisible(false);
                        }
                    });
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                try {
                    gameCache.setValue(CURRENT_CELL, cell.getName());
                    command.execute();
                } catch (GameException e) {
                    Array<GameException> exceptions = (Array<GameException>) gameCache.getObject(GameCache.GAME_EXCEPTION);
                    exceptions.add(e);
                }
                return false;
            }
        });
    }

    public Room createRoom(RoomEntity roomEntity) {
        Room room;
        switch (Room.Type.valueOf(roomEntity.getType())) {
            case SECURITY:
                room = new SecurityRoom(roomEntity.getId().toString(),
                        new RoomInfo(roomEntity.getId().toString(), new Array<>(roomEntity.getWorkerEntities().stream()
                                .map(w -> new SecurityStaff(w.getId().toString(), w.getSalary())).toArray(SecurityStaff[]::new)),
                                roomEntity.getPrice(), roomEntity.getCost(),
                                new TimeLineDate(roomEntity.getDays(), roomEntity.getMonths(), roomEntity.getYears()),
                                Room.Type.valueOf(roomEntity.getType())), CELL_SIZE, CELL_SIZE); break;
            case HALL: room = new HallRoom(roomEntity.getId().toString(),
                    new RoomInfo(roomEntity.getId().toString(), Array.with(),
                            roomEntity.getPrice(), roomEntity.getCost(),
                            new TimeLineDate(roomEntity.getDays(), roomEntity.getMonths(), roomEntity.getYears()),
                            Room.Type.valueOf(roomEntity.getType())), CELL_SIZE, CELL_SIZE); break;
            case CLEANING: room = new CleaningRoom(roomEntity.getId().toString(),
                    new RoomInfo(roomEntity.getId().toString(), new Array<>(roomEntity.getWorkerEntities().stream()
                            .map(w -> new CleaningStaff(w.getId().toString(), w.getSalary())).toArray(CleaningStaff[]::new)),
                            roomEntity.getPrice(), roomEntity.getCost(),
                            new TimeLineDate(roomEntity.getDays(), roomEntity.getMonths(), roomEntity.getYears()),
                            Room.Type.valueOf(roomEntity.getType())), CELL_SIZE, CELL_SIZE); break;
            case OFFICE:
                ResidentEntity residentEntity = roomEntity.getResidentEntity();
                room = new OfficeRoom(roomEntity.getId().toString(),
                            new CompanyInfo(residentEntity.getBusinessName(), roomEntity.getWorkerEntities().stream().map(w -> w.getId().toString()).collect(Collectors.toList())),
                            new RoomInfo(roomEntity.getId().toString(), new Array<>(Arrays.stream(roomEntity.getWorkerEntities().toArray(WorkerEntity[]::new))
                                    .map(w -> new WorkerStaff(w.getId().toString())).toArray(WorkerStaff[]::new)),
                                    roomEntity.getPrice(), roomEntity.getCost(),
                                    new TimeLineDate(roomEntity.getDays(), roomEntity.getMonths(), roomEntity.getYears()),
                                    Room.Type.valueOf(roomEntity.getType())),
                            CELL_SIZE, CELL_SIZE); break;
            case CAFFE: room = new CaffeRoom(roomEntity.getId().toString(),
                    new RoomInfo(roomEntity.getId().toString(), new Array<>(roomEntity.getWorkerEntities().stream()
                            .map(w -> new CaffeStaff(w.getId().toString(), w.getSalary())).toArray(CaffeStaff[]::new)),
                            roomEntity.getPrice(), roomEntity.getCost(),
                            new TimeLineDate(roomEntity.getDays(), roomEntity.getMonths(), roomEntity.getYears()),
                            Room.Type.valueOf(roomEntity.getType())), CELL_SIZE, CELL_SIZE); break;
            default: room = new ElevatorRoom(roomEntity.getId().toString(),
                    new RoomInfo(roomEntity.getId().toString(), Array.with(),
                            roomEntity.getPrice(), roomEntity.getCost(),
                            new TimeLineDate(roomEntity.getDays(), roomEntity.getMonths(), roomEntity.getYears()),
                            Room.Type.valueOf(roomEntity.getType())), CELL_SIZE, CELL_SIZE); break;
        }
        return room;
    }

    public Map createMap(OfficeEntity officeEntity) {
        gameCache.setLong(CURRENT_OFFICE_CELLS_WIDTH, GRID_WIDTH);
        gameCache.setLong(CURRENT_OFFICE_CELLS_HEIGHT, GRID_HEIGHT);
        Map map = new Map(MAP.name());
        float width = 3000f;
        float height = 2500f;
        map.setSize(width, height);
        Background background = createBackground(width, height);
        map.addActor(background);
        map.addActor(createOffice(officeEntity, background.getWidth(), background.getHeight()));
        return map;
    }

    public Office createOffice(OfficeEntity officeEntity, float width, float height) {
        Array<Grid> levels = new Array<>(officeEntity.getLevelEntities().stream()
                .map(this::createLevel)
                .toArray(Grid[]::new));
        Office office = new Office(officeEntity.getId().toString(), levels);
        office.setPosition(Math.round(width / 2f - (GRID_WIDTH * CELL_SIZE) / 2f),
                Math.round(height / 2f - (GRID_HEIGHT * CELL_SIZE) / 2f));
        office.setSize(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        return office;
    }

    public Grid createLevel(LevelEntity levelEntity) {
        List<CellEntity> cells = new ArrayList<>(levelEntity.getCellEntities());
        Grid grid = new Grid(levelEntity.getId().toString());
        grid.setSize(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        grid.setPosition(0, 0);
        cells.forEach(c -> grid.addActor(createCell(c)));
        drawBorders(grid);
        return grid;
    }

    public Background createBackground(float width, float height) {
        return new Background(BACKGROUND.name(), width, height, gameActorSearchService);
    }

    public GameTextButton createDestroyRoomButton() {
        return new GameTextButton(DESTROY_ROOM_BUTTON.name(), destroyRoomCommand, "Destroy", skin);
    }

    public GameTextButton createToggleGridButton() {
        GameTextButton toggleGridButton = new GameTextButton(TOGGLE_GRID_BUTTON.name(), toggleGridCommand, "#", skin);
        toggleGridButton.getLabel().setFontScale(1.5f);
        toggleGridButton.setPosition(Gdx.graphics.getWidth() - toggleGridButton.getWidth() - DEFAULT_PAD * 2f, Gdx.graphics.getHeight() / 4.6f);
        return toggleGridButton;
    }

    public DefaultModal createNotificationModal() {
        DefaultModal notification = new DefaultModal(NOTIFICATION_MODAL.name(), "",
                Array.with(new GameLabel(NOTIFICATION_INFO_LABEL.name(), "", skin)), skin);
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
    public Array<Actor> getGameActors() {
        return (Array<Actor>) gameCache.getObject(GameCache.GAME_ACTORS);
    }

    public enum UiComponentConstant {
        ROOMS_DROP_DOWN, ROOMS_LIST, ROOM_BUTTON_POSTFIX, ROOMS_MENU_TOGGLE_BUTTON, ROOM_INFO_MODAL,
        CLOSE_ROOM_INFO_BUTTON, CLOSE_OFFICE_INFO_BUTTON, CLOSE_PEOPLE_INFO_BUTTON, CLOSE_ENVIRONMENT_MODAL_BUTTON, CLOSE_WORLD_MODAL_BUTTON,
        CLOSE_NOTIFICATION_BUTTON, DESTROY_ROOM_BUTTON, NOTIFICATION_MODAL, MAIN_PANE, COLLAPSE_BUTTON, OFFICE_INFO_MODAL, TOGGLE_OFFICE_INFO_BUTTON,
        ENVIRONMENT_MODAL, PEOPLE_INFO_MODAL, WORLD_MODAL,
        TOGGLE_ENVIRONMENT_MODAL_BUTTON, ENVIRONMENT_MODAL_ITEM_GRID, TOGGLE_PEOPLE_INFO_BUTTON, PEOPLE_INFO_LABEL,
        TOGGLE_WORLD_MODAL_BUTTON, TOGGLE_ALL_WINDOWS_BUTTON,
        ITEM, OBJECT_CELL, CELL, ROOM, MAP, OFFICE, GRID, OBJECT_GRID, BACKGROUND, STAFF, TOGGLE_GRID_BUTTON, ROOM_INFO_LABEL, OFFICE_INFO_LABEL,
        NOTIFICATION_INFO_LABEL, ROAD, CAR, BUDGET_LABEL, TIMELINE_LABEL,

        BUILD_NEW_OFFICE_MODAL, BUILD_NEW_OFFICE_NAME_LABEL, BUILD_NEW_OFFICE_NAME_INPUT,
        BUILD_NEW_OFFICE_CONFIRM_BUTTON, CLOSE_BUILD_NEW_OFFICE_MODAL_BUTTON;

        @Getter
        public enum Items {
            PLANT(BigDecimal.valueOf(15.0f)), COOLER(BigDecimal.valueOf(35.0f));

            private final BigDecimal price;

            Items(BigDecimal price) {
                this.price = price;
            }

        }

    }

}
