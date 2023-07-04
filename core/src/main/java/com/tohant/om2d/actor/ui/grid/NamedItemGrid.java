package com.tohant.om2d.actor.ui.grid;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Item;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.command.item.PickItemCommand;
import com.tohant.om2d.util.AssetsUtil;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public class NamedItemGrid extends AbstractItemGrid {

    private boolean isScaling;

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

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event, x, y, pointer, button);
                        new PickItemCommand(next.getName()).execute();
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
                add(itemLabel).center().pad(DEFAULT_PAD);
            }
            row();
        }
        debug();
    }

}
