package com.winterproject.youssufradi.life_logger.Event;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by GUC on 24.07.17.
 */
public class EventEntryObject implements Comparable<EventEntryObject>{
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndDay() {
        return endDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public ArrayList<String> getLogs() {
        return logs;
    }

    private final String title;
    private long id;
    private String description;
    private String location;
    private int startDay;
    private int startMonth;
    private int startYear;
    private int startHour;
    private int startMinute;
    private int endDay;
    private int endMonth;
    private int endYear;
    private int endHour;
    private int endMinute;
    private ArrayList<String> people = new ArrayList<>();
    private ArrayList<String> logs = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventEntryObject(long id, String title, String description, String location, int startDay,
                          int startMonth, int startYear, int startHour, int startMinute,
                          int endDay, int endMonth, int endYear, int endHour,
                          int endMinute, ArrayList<String> logs, ArrayList<String> people){
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startDay = startDay;
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endYear;
        this.endHour = endHour;
        this.endMinute = endMinute;
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
        Log.e("The full Event Entry : ", this.getId() + "    " + this.getDescription() + "    " + this.getStartDay() + "     " +  this.getStartMonth()
                + "    "  + this.getStartYear() + "    "  + this.getLocation() + "    "  + k + "    "  + k1);

    }

    @Override
    public int compareTo(@NonNull EventEntryObject a2) {
        if (this.getStartYear() < a2.getStartYear())
            return -1;
        else if (this.getStartYear() > a2.getStartYear())
            return 1;
        else if (this.getStartMonth() < a2.getStartMonth())
            return -1;
        else if (this.getStartMonth() > a2.getStartMonth())
            return 1;
        else if (this.getStartDay() < a2.getStartDay())
            return -1;
        else if (this.getStartDay() > a2.getStartDay())
            return 1;
        else if (this.getStartHour() < a2.getStartHour())
            return -1;
        else if (this.getStartHour() > a2.getStartHour())
            return 1;
        else if (this.getStartMinute() < a2.getStartMinute())
            return -1;
        else if (this.getStartMinute() > a2.getStartMinute())
            return 1;
        else return 0;
    }

}
