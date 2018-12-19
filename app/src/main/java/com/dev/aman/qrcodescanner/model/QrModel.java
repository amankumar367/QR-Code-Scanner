package com.dev.aman.qrcodescanner.model;

public class QrModel {
    String id, contents, date, time;

    public QrModel(String id, String contents, String date, String time) {
        this.id = id;
        this.contents = contents;
        this.date = date;
        this.time = time;
    }

    public QrModel(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
