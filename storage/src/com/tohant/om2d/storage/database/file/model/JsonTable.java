package com.tohant.om2d.storage.database.file.model;

public class JsonTable implements FileTable {

    private final String name;
    private final String content;

    public JsonTable(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

}
