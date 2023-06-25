package com.tohant.om2d.service;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.tohant.om2d.storage.Cache.UI_ACTORS;

public abstract class ActorService {

    public abstract Array<Actor> getUiActors();

    public Array<Actor> getActorsByIdPrefix(String idPrefix) {
        try {
            return selectChildrenByIdPrefixRecursively(idPrefix, getUiActors());
        } catch (RuntimeException e) {
            System.out.println(Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n")));
        }
        return null;
    }

    private Array<Actor> selectChildrenByIdPrefixRecursively(String idPrefix, Array<Actor> actors) {
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
        Array<Actor> uiActors = getUiActors();
        for (int i = 0; i < uiActors.size; i++) {
            Actor a = uiActors.get(i);
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
