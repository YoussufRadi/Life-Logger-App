package com.winterproject.youssufradi.life_logger;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class LoggerFragment extends Fragment {


    View rootView;
    public static ArrayList<LogEntryObject> logEntries = new ArrayList<>();
    private FloatingActionButton add;
    public static EntryAdapter logAdapter;
    public static LogEntryObject logDisplay;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_logger, container, false);

        listView = (ListView) rootView.findViewById(R.id.log_list_view);

        getDataFromDB();

        Collections.sort(logEntries);

        logAdapter = new EntryAdapter(getActivity(), logEntries);
        listView.setAdapter(logAdapter);


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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                logDisplay = logEntries.get(position);
                GalleryFragment.selectedPhotos = logDisplay.getPhotos();
                Toast.makeText(getActivity(),"Item Clicked", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                LogDetails newFragment = LogDetails.newInstance();
                newFragment.show(ft, "logDetails");
            }
        });

        return rootView;
    }

    public void getDataFromDB(){
        SQLiteDatabase db = new LoggerDBHelper(getActivity()).getWritableDatabase();
        Cursor logCursor = db.query(
                LoggerContract.LogEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        boolean cursor = logCursor.moveToFirst();
        if(cursor){
            int logID = logCursor.getColumnIndex(LoggerContract.LogEntry._ID);
            int logHighlight = logCursor.getColumnIndex(LoggerContract.LogEntry.COLUMN_HIGHLIGHT);
            int logLocation = logCursor.getColumnIndex(LoggerContract.LogEntry.COLUMN_LOCATION);
            int logDay = logCursor.getColumnIndex(LoggerContract.LogEntry.COLUMN_DAY);
            int logMonth = logCursor.getColumnIndex(LoggerContract.LogEntry.COLUMN_MONTH);
            int logYear = logCursor.getColumnIndex(LoggerContract.LogEntry.COLUMN_YEAR);
            int logPhotos = logCursor.getColumnIndex(LoggerContract.LogEntry.COLUMN_PHOTOS);
            Gson gson = new Gson();
            do {
                long COLUMN_ID = logCursor.getLong(logID);
                String COLUMN_HIGHLIGHT = logCursor.getString(logHighlight);
                String COLUMN_LOCATION = logCursor.getString(logLocation);
                int COLUMN_DAY = logCursor.getInt(logDay);
                int COLUMN_MONTH = logCursor.getInt(logMonth);
                int COLUMN_YEAR = logCursor.getInt(logYear);
                String COLUMN_PHOTOS = logCursor.getString(logPhotos);

                Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                ArrayList<String>  finalOutputString = gson.fromJson(COLUMN_PHOTOS,  listType);

                logEntries.add(new LogEntryObject(COLUMN_ID, COLUMN_HIGHLIGHT,COLUMN_LOCATION,COLUMN_DAY,
                        COLUMN_MONTH,COLUMN_YEAR,finalOutputString));

            } while(logCursor.moveToNext());
        }
        logCursor.close();
        db.close();


    }

}

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
        this.printLog();
        a2.printLog();
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

