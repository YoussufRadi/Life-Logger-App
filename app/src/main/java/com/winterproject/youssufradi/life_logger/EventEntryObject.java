package com.winterproject.youssufradi.life_logger;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by GUC on 24.07.17.
 */
class EventEntryObject implements Comparable<EventEntryObject>{
    private long id;
    private String description;
    private String location;
    private int day;
    private int month;
    private int year;
    private ArrayList<String> people = new ArrayList<>();
    private ArrayList<String> logs = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventEntryObject(long id, String description, String location, int day,
                          int month, int year, ArrayList<String> people, ArrayList<String> logs){
        this.id = id;
        this.description = description;
        this.location = location;
        this.day = day;
        this.month = month;
        this.year = year;
        for(int i = 0; i < people.size(); i++)
            this.people.add(people.get(i)+"");
        for(int i = 0; i < logs.size(); i++)
            this.logs.add(logs.get(i)+"");

    }

    public void printEvent(){

        String k = "";
        for(int i = 0; i < this.getPeople().size(); i++) {
            k = k + "   " + this.getPeople().get(i);
        }
        String k1 = "";
        for(int i = 0; i < this.getLogs().size(); i++) {
            k = k + "   " + this.getLogs().get(i);
        }
        Log.e("The full Event Entry : ", this.getId() + "    " + this.getDescription() + "    " + this.getDay() + "     " +  this.getMonth()
                + "    "  + this.getYear() + "    "  + this.getLocation() + "    "  + k + "    "  + k1);

    }

    public String getDescription() {
        return description;
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

    public ArrayList<String> getPeople() {
        return people;
    }

    public ArrayList<String> getLogs() {
        return logs;
    }
    @Override
    public int compareTo(@NonNull EventEntryObject a2) {
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
