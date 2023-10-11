package com.tohant.om2d.model.json;

public abstract class EventJson {

    private String id;
    private String content;
    private String condition;
    private String timeLineDate;
    private EventType type;

    public EventJson(String id, String content, String condition, String timeLineDate, EventType type) {
        this.id = id;
        this.content = content;
        this.condition = condition;
        this.timeLineDate = timeLineDate;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTimeLineDate() {
        return timeLineDate;
    }

    public void setTimeLineDate(String timeLineDate) {
        this.timeLineDate = timeLineDate;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public enum EventType {
        LOCAL, GLOBAL
    }

}
