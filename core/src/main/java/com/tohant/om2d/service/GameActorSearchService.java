package com.tohant.om2d.service;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.config.GameStage;
import com.tohant.om2d.config.GameUiStage;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameActorSearchService {

    private final GameStage gameStage;
    private final GameUiStage gameUiStage;
    private Array<Actor> actors;

    @PostConstruct
    public void init() {
        actors = new Array<>();
        actors.addAll(gameStage.getActors());
        actors.addAll(gameUiStage.getActors());
    }

    public Array<Actor> getActorsByIdPrefix(String idPrefix) {
        try {
            return selectChildrenByIdPrefixRecursively(idPrefix);
        } catch (RuntimeException e) {
            System.out.println(Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n")));
        }
        return null;
    }

    public Array<Actor> selectChildrenByIdPrefixRecursively(String idPrefix) {
        Array<Actor> result = new Array<>();
        for (int i = 0; i < actors.size; i++) {
            Actor actor = actors.get(i);
            if (actor.getName() != null && actor.getName().startsWith(idPrefix)) {
                result.add(actor);
            }
            if (actor instanceof Group) {
                Array.ArrayIterator<Actor> children = selectChildrenByIdPrefixRecursively(idPrefix, ((Group) actor).getChildren())
                        .iterator();
                for (Actor child : children) {
                    result.add(child);
                }
            }
        }
        return result;
    }

    public Actor getActorById(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < actors.size; i++) {
            Actor a = actors.get(i);
            if (a.getName() != null && a.getName().equals(id)) {
                return a;
            } else {
                if (a instanceof Group) {
                    Actor found = null;
                    try {
                        found = ((Group) a).findActor(id);
                    } catch (NullPointerException ignored) {
                    }
                    if (found != null) {
                        return found;
                    }
                }
            }
        }
        return null;
    }
}
