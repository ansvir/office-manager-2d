package com.tohant.om2d.actor.ui.grid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Item;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.command.item.PickItemCommand;
import com.tohant.om2d.util.AssetsUtil;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.actor.constant.Constant.TEXTURE_SIZE;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.Items.PLANT;

public class NamedItemGrid extends AbstractItemGrid {

    public NamedItemGrid(String id, Array<Item> items) {
        super(id, items);
    }

    @Override
    protected void populateGrid(Array<Item> items) {
        int columns = (int) getGridSize(items.size).x;
        int rows = (int) getGridSize(items.size).y;
        for (int i = 1; i <= rows; i++) {
            for (int j = 0; j < columns && j * i < items.size; j++) {
                Table itemLabel = new Table();
                Item next = items.get(i * j);
                itemLabel.add(next);
                itemLabel.row();
                String labelString = next.getType().name().charAt(0)
                        + next.getType().name().substring(1).toLowerCase() + "\n"
                        + Math.round(next.getType().getPrice().floatValue()) + " $";
                itemLabel.add(new GameLabel(PLANT.name() + "_ITEM_LABEL", labelString, AssetsUtil.getDefaultSkin()));
                itemLabel.addListener(new InputListener() {

                    private final Action enterAction = Actions.scaleBy(1.5f, 1.5f, 0.7f);

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event, x, y, pointer, button);
                        new PickItemCommand(next.getName()).execute();
                        return true;
                    }

                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        super.enter(event, x, y, pointer, fromActor);
                        itemLabel.addAction(enterAction);
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        super.exit(event, x, y, pointer, toActor);
                        itemLabel.removeAction(enterAction);
                    }
                });
                add(itemLabel).center().pad(DEFAULT_PAD).width(TEXTURE_SIZE).height(TEXTURE_SIZE + DEFAULT_PAD * 2);
            }
            row();
        }
        pad(DEFAULT_PAD);
    }

}
