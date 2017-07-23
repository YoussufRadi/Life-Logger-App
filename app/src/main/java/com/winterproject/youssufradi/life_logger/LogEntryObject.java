package com.winterproject.youssufradi.life_logger;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by GUC on 24.07.17.
 */
class LogEntryObject implements Comparable<LogEntryObject>{
    private long id;
    private String highlights;
    private String location;
    private int day;
    private int month;
    private int year;
    private ArrayList<String> photos = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogEntryObject(long id, String highlights, String location, int day,
                          int month, int year, ArrayList<String> photos){
        this.id = id;
        this.highlights = highlights;
        this.location = location;
        this.day = day;
        this.month = month;
        this.year = year;
        for(int i = 0; i < photos.size(); i++)
            this.photos.add(photos.get(i)+"");

    }

    public void printLog(){

        String k = "";
        for(int i = 0; i < this.getPhotos().size(); i++) {
            k = k + "   " + this.getPhotos().get(i);
        }
        Log.e("The full Log Entry : ", this.getId() + "    " + this.getHighlights() + "    " + this.getDay() + "     " +  this.getMonth()
                + "    "  + this.getYear() + "    "  + this.getLocation() + "    "  + k);

    }

    public String getHighlights() {
        return highlights;
    }

    public String getLocation() {
        return location;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    @Override
    public int compareTo(@NonNull LogEntryObject a2) {
        if (this.getYear() < a2.getYear())
            return -1;
        else if (this.getYear() > a2.getYear())
            return 1;
        else if (this.getMonth() < a2.getMonth())
            return -1;
        else if (this.getMonth() > a2.getMonth())
            return 1;
        else if (this.getDay() < a2.getDay())
            return -1;
        else if (this.getDay() > a2.getDay())
            return 1;
        else return 0;
    }
}
