package com.alanai.daric.Models;

public class Diary {
    private long timeStamp;
    private String title;
    private String content;
    private String dateTime;
    private String tag;
    private boolean bookmark;
    private int recalls;
    private boolean professional;

    public Diary() {
    }

    public Diary(long timeStamp, String title, String content, String dateTime, String tag, boolean bookmark, int recalls, boolean professional) {
        this.timeStamp = timeStamp;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.tag = tag;
        this.bookmark = bookmark;
        this.recalls = recalls;
        this.professional = professional;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public int getRecalls() {
        return recalls;
    }

    public void setRecalls(int recalls) {
        this.recalls = recalls;
    }

    public boolean isProfessional() {
        return professional;
    }

    public void setProfessional(boolean professional) {
        this.professional = professional;
    }
}
