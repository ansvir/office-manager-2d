package com.tohant.om2d.command.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.modal.AbstractModal;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.service.MenuUiActorService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.storage.cache.Cache;
import com.tohant.om2d.storage.database.ProgressDao;

import java.util.List;
import java.util.Optional;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.service.MenuUiActorService.MenuUiComponentConstant.MENU_LOAD_GAME_MODAL;
import static com.tohant.om2d.service.MenuUiActorService.MenuUiComponentConstant.MENU_LOAD_GAME_MODAL_TABLE;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class UpdateLoadGameModalCommand implements Command {

    @Override
    public void execute() {
        Skin skin = getDefaultSkin();
        List<ProgressEntity> progresses = ProgressDao.getInstance().queryForAll();
        MenuUiActorService menuUiActorService = MenuUiActorService.getInstance();
        AbstractModal modal = (AbstractModal) menuUiActorService.getActorById(MENU_LOAD_GAME_MODAL.name());
        Table initTable = (Table) menuUiActorService.getActorById(MENU_LOAD_GAME_MODAL_TABLE.name());
        initTable.getCells().iterator().forEach(c -> {
            if (c.getActor() instanceof Table) {
                c.clearActor();
                for (int i = 0; i < progresses.size(); i++) {
                    int finalI = i;
                    Optional.ofNullable(progresses.get(i).getCompanyEntity()).ifPresent(c2 -> {
                        GameTextButton button = new GameTextButton(finalI + "_LOAD_GAME_BUTTON",
                                () -> {
                                    RuntimeCacheService.getInstance().setValue(Cache.CURRENT_PROGRESS_ID, progresses.get(finalI).getId().toString());
                                    RuntimeCacheService.getInstance().setBoolean(Cache.READY_TO_START, true);
                                }, c2.getName(), skin);
                        GameTextButton deleteButton = new GameTextButton("DELETE_" + finalI + "_GAME_BUTTON", () -> {
                            ProgressDao.getInstance().deleteById(progresses.get(finalI).getId());
                            Table table = new Table();
                            for (int j = 0; j < initTable.getCells().size; j += 2) {
                                if ((j != finalI) && ((j + 1) != (finalI + 1))) {
                                    table.add(initTable.getCells().get(j).getActor()).growX().padRight(DEFAULT_PAD);
                                    table.add(initTable.getCells().get(j + 1).getActor());
                                    table.row();
                                }
                            }
                            Table parent = (Table) initTable.getParent();
                            initTable.remove();
                            parent.add(table);
                        }, "X", skin);
                        deleteButton.setColor(Color.WHITE);
                        deleteButton.getLabel().getStyle().fontColor = Color.RED;
                        initTable.add(button).growX().padRight(DEFAULT_PAD);
                        initTable.add(deleteButton);
                        initTable.row();
                    });
                }
            }
        });
        initTable.setSize(initTable.getPrefWidth(), initTable.getPrefHeight());
    }
}
