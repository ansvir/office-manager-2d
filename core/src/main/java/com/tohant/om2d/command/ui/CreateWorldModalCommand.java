package com.tohant.om2d.command.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.dropdown.AbstractDropDown;
import com.tohant.om2d.actor.ui.dropdown.HorizontalTriggerDropdown;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.list.AbstractList;
import com.tohant.om2d.actor.ui.list.DefaultList;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.command.office.ChooseBuildNewOfficeCommand;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.model.Region;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.util.AssetsUtil;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.WORLD_MODAL;
import static com.tohant.om2d.storage.cache.GameCache.CURRENT_CHOSEN_REGION;

@Component
@RequiredArgsConstructor
public class CreateWorldModalCommand implements Command {

    private final GameCache gameCache;
    private final ChooseBuildNewOfficeCommand chooseBuildNewOfficeCommand;
    private final GameActorFactory gameActorFactory;

    @Override
    public void execute() {
        Skin skin = AssetsUtil.getDefaultSkin();
        Texture worldMapTexture = AssetService.WORLD_MAP;
        Vector2 worldMapRatio = new Vector2(2f, 2f);
        Vector2 modalSize = new Vector2(MathUtils.clamp(worldMapTexture.getWidth() * 2f, 0, Gdx.graphics.getWidth()),
                MathUtils.clamp(worldMapTexture.getHeight() * 2f, 0, Gdx.graphics.getHeight()));
        GameLabel popularity = new GameLabel("WORLD_MAP_POPULARITY_LABEL", "My Popularity:", skin);
        Vector2 worldMapSize = new Vector2(modalSize.x, modalSize.y);
        Texture worldMap = AssetsUtil.resizeTexture(worldMapTexture, worldMapSize.x, worldMapSize.y);
        Table contentTable = new Table();
        contentTable.add(popularity).left().pad(DEFAULT_PAD);
        contentTable.row();
        Image image = new Image(worldMap);
        Group group = new Group();
        group.setSize(image.getWidth(), image.getHeight());
        group.addActor(image);
        Arrays.stream(Region.values()).forEach(r -> {
            gameCache.setValue(CURRENT_CHOSEN_REGION, r.name());
            ImageTextButton regionButton = getRegionButton(r, skin);
            GameTextButton buildOfficeOption = new GameTextButton("BUILD_OFFICE_" + r.name() + "_REGION_BUTTON", chooseBuildNewOfficeCommand, "Build office", skin);
            AbstractList regionDetailsList = new DefaultList(r.name() + "_REGION_WORLD_MAP_DETAILS_LIST", Array.with(buildOfficeOption));
            AbstractDropDown regionDetails = new HorizontalTriggerDropdown(r.name() + "_REGION_WORLD_MAP_DETAILS_DROPDOWN",
                    regionDetailsList, regionButton, HorizontalTriggerDropdown.TriggerButtonType.LEFT_TOP, false);
            switch (r) {
                case ASIA: {
                    regionDetails.setPosition(500 * worldMapRatio.x + buildOfficeOption.getWidth(), worldMapSize.y - 80 * worldMapRatio.y);
                    group.addActor(regionDetails);
                    break;
                }
                case AFRICA: {
                    regionDetails.setPosition(370 * worldMapRatio.x + buildOfficeOption.getWidth(), worldMapSize.y - 160 * worldMapRatio.y);
                    group.addActor(regionDetails);
                    break;
                }
                case EUROPE: {
                    regionDetails.setPosition(400 * worldMapRatio.x + buildOfficeOption.getWidth(), worldMapSize.y - 70 * worldMapRatio.y);
                    group.addActor(regionDetails);
                    break;
                }
                case AMERICA: {
                    regionDetails.setPosition(170 * worldMapRatio.x + buildOfficeOption.getWidth(), worldMapSize.y - 125 * worldMapRatio.y);
                    group.addActor(regionDetails);
                    break;
                }
                case OCEANIA: {
                    regionDetails.setPosition(570 * worldMapRatio.x + buildOfficeOption.getWidth(), worldMapSize.y - 170 * worldMapRatio.y);
                    group.addActor(regionDetails);
                    break;
                }
                case AUSTRALIA: {
                    regionDetails.setPosition(610 * worldMapRatio.x + buildOfficeOption.getWidth(), worldMapSize.y - 225 * worldMapRatio.y);
                    group.addActor(regionDetails);
                    break;
                }
            }
        });
        contentTable.add(group).bottom();
        DefaultModal modal = new DefaultModal(WORLD_MODAL.name(), "World", Array.with(contentTable), skin);
        modal.setSize(modalSize.x, modalSize.y + popularity.getHeight() + DEFAULT_PAD * 3f);
        modal.setPosition(Gdx.graphics.getWidth() / 2f - modal.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - modal.getHeight() / 2f);
        modal.forceToggle(false);
        replaceExistingModal(modal);
    }

    private ImageTextButton getRegionButton(Region region, Skin skin) {
        ImageTextButton button = new ImageTextButton(region.name().charAt(0)
                + region.name().replace("_", " ").substring(1).toLowerCase(), skin);
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                gameCache.setValue(GameCache.CURRENT_REGION, region.name());
                return false;
            }
        });
        return button;
    }

    private void replaceExistingModal(DefaultModal defaultModal) {
        gameActorFactory.getGameActors()
                .select(a -> a.getName().equals(WORLD_MODAL.name()))
                .forEach(Actor::remove);
        gameActorFactory.getGameActors().add(defaultModal);
    }

}
