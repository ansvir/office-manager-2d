package com.tohant.om2d.storage.file.impl;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.model.json.EventJson;
import com.tohant.om2d.storage.file.JsonFileDatabase;

import java.util.Iterator;
import java.util.Optional;

public class EventJsonFileDatabase extends JsonFileDatabase<EventJson> {

    @Override
    public Optional<EventJson> getById(String id) {
        Iterator<EventJson> iterator = getAll().select(e -> e.getId().equals(id)).iterator();
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }

    @Override
    public Array<EventJson> getAll() {
        Array<EventJson> events = new Array<>();
        Array<String> jsons = getJsonFileDbFile(JSON_FILE_DB_EVENTS_KEY);
        for (String json : jsons) {
            events.add(getJson().fromJson(EventJson.class, json));
        }
        return events;
    }

    @Override
    public void save(EventJson eventJson) {
        throw new UnsupportedOperationException("Json files are stored and created by developer.");
    }

    @Override
    public void saveAll(Array<EventJson> t) {
        throw new UnsupportedOperationException("Json files are stored and created by developer.");
    }

    @Override
    public void deleteById(String id) {
        throw new UnsupportedOperationException("Json files are stored and created by developer.");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Json files are stored and created by developer.");
    }

}
