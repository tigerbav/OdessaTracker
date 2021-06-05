package com.smirnova.odesatracker;

public class EventInfo {
    private String category;
    private String cost;
    private String date;
    private String description;
    private boolean like;
    private String link;
    private String location;
    private String raiting;
    private String type;
    private String time;
    private String event;

    public EventInfo(String category, String cost, String date,
                     String description, boolean like, String link,
                     String location, String raiting, String type,
                     String time, String event) {
        this.category = category;
        this.cost = cost;
        this.date = date;
        this.description = description;
        this.like = like;
        this.link = link;
        this.location = location;
        this.raiting = raiting;
        this.type = type;
        this.time = time;
        this.event = event;
    }

    public String getCategory() {
        return category;
    }

    public String getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getLink() {
        return link;
    }

    public String getLocation() {
        return location;
    }

    public String getRaiting() {
        return raiting;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getEvent() {
        return event;
    }
}
