package com.winterproject.youssufradi.life_logger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LoggerFragment extends Fragment {


    View rootView;
    public static ArrayList<LogEntryObject> logEntries = new ArrayList<>();
    private FloatingActionButton add;
    private static TextView textField;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_logger, container, false);

        textField = (TextView) rootView.findViewById(R.id.log_text_view);

        add = (FloatingActionButton) rootView.findViewById(R.id.add_new);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Used Later to quickly add logs", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                NewLogFragment newFragment = NewLogFragment.newInstance();
                newFragment.show(ft, "newLog");
            }
        });

        return rootView;
    }

    public static void getData(String x){
        textField.setText(x);
    }
}

class LogEntryObject {
    private String highlights;
    private String location;
    private int day;
    private int month;
    private int year;
    private ArrayList<String> photos = new ArrayList<>();

    public LogEntryObject(String highlights, String location, int day,
                          int month, int year, ArrayList<String> photos){
        this.highlights = highlights;
        this.location = location;
        this.day = day;
        this.month = month;
        this.year = year;
        for(int i = 0; i < photos.size(); i++)
            this.photos.add(photos.get(i)+"");

    }

    public static void printLog(LogEntryObject x){

        String k = "";
        for(int i = 0; i < x.getPhotos().size(); i++) {
            k = k + "   " + x.getPhotos().get(i);
        }
        LoggerFragment.getData(x.getHighlights() + "    " + x.getDay() + "     " +  x.getMonth()
                + "    "  + x.getYear() + "    "  + x.getLocation() + "    "  + k);

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
}

