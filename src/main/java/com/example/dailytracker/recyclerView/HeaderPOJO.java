package com.example.dailytracker.recyclerView;

public class HeaderPOJO extends ListItem{

    private String date;

    public HeaderPOJO(){}

    public String getHeader() {
        return date;
    }
    public void setHeader(String header) {
        this.date = header;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
