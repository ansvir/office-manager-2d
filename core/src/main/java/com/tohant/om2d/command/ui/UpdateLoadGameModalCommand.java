package com.tohant.om2d.command.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.service.GameMenuActorFactory;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.ProgressDao;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.service.GameMenuActorFactory.MenuUiComponentConstant.MENU_LOAD_GAME_MODAL_TABLE;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

@Component
@RequiredArgsConstructor
public class UpdateLoadGameModalCommand implements Command {

    private final ProgressDao progressDao;
    private final GameMenuActorFactory gameMenuActorService;
    private final GameCache gameCache;

    @Override
    public void execute() {
        Skin skin = getDefaultSkin();
        List<ProgressEntity> progresses = progressDao.queryForAll();
        Table initTable = (Table) GameActorSearchService.getActorById(MENU_LOAD_GAME_MODAL_TABLE.name());
        initTable.getCells().iterator().forEach(c -> {
            if (c.getActor() instanceof Table) {
                c.clearActor();
                for (int i = 0; i < progresses.size(); i++) {
                    int finalI = i;
                    Optional.ofNullable(progresses.get(i).getCompanyEntity()).ifPresent(c2 -> {
                        GameTextButton button = new GameTextButton(finalI + "_LOAD_GAME_BUTTON",
                                () -> {
                                    gameCache.setValue(GameCache.CURRENT_PROGRESS_ID, progresses.get(finalI).getId().toString());
                                    gameCache.setBoolean(GameCache.READY_TO_START, true);
                                }, c2.getName(), skin);
                        GameTextButton deleteButton = new GameTextButton("DELETE_" + finalI + "_GAME_BUTTON", () -> {
                            progressDao.deleteById(progresses.get(finalI).getId());
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
