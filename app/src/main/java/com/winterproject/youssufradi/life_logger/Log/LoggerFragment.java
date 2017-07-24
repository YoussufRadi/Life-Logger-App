package com.winterproject.youssufradi.life_logger.Log;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winterproject.youssufradi.life_logger.Event.NewEventFragment;
import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;
import com.winterproject.youssufradi.life_logger.gallery.GalleryFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class LoggerFragment extends DialogFragment {


    View rootView;
    public static ArrayList<LogEntryObject> logEntries = new ArrayList<>();
    private FloatingActionButton add;
    public static EntryAdapter logAdapter;
    public static LogEntryObject logDisplay;
    private ListView listView;
    public static boolean checkbox = false;
    public static boolean hasArray = false;
    public static ArrayList<LogEntryObject> selectedEntries = new ArrayList<>();
    public static ArrayList<String> passedEntries = new ArrayList<>();
    private FloatingActionButton log;


    public static LoggerFragment newInstance() {
        LoggerFragment f = new LoggerFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_logger, container, false);

        listView = (ListView) rootView.findViewById(R.id.log_list_view);
        if(hasArray){
            if(!passedEntries.isEmpty()) {
                getDataFromDB(getActivity());
                ArrayList<LogEntryObject> temp = new ArrayList<>();
                for (int i = 0; i < logEntries.size(); i++) {
                    for (int j = 0; j < passedEntries.size(); j++)
                        if (logEntries.get(i).getId() == Long.parseLong(passedEntries.get(j)))
                            temp.add(logEntries.get(i));
                }
                logEntries = temp;
            } else
                for (int i = 0; i < logEntries.size(); i++)
                    passedEntries.add(Long.toString(logEntries.get(i).getId()));
        }
        else
            getDataFromDB(getActivity());

        logAdapter = new EntryAdapter(getActivity(), logEntries, checkbox);
        listView.setAdapter(logAdapter);

        add = (FloatingActionButton) rootView.findViewById(R.id.log_add_new);
        log = (FloatingActionButton) rootView.findViewById(R.id.log_event_select_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Used Later to quickly add logs", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                NewLogFragment newFragment = NewLogFragment.newInstance();
                newFragment.show(ft, "newLog");
            }
        });


        if(hasArray) {
            add.setVisibility(View.GONE);
        }

        if(checkbox) {
            add.setVisibility(View.GONE);
            log.setVisibility(View.VISIBLE);
            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logEntries.clear();
                    for(int i = 0; i < selectedEntries.size(); i++)
                        logEntries.add(selectedEntries.get(i));
                    selectedEntries.clear();


                    LoggerFragment fragment = (LoggerFragment) getActivity().getSupportFragmentManager().findFragmentByTag("logSelector");
                    if (fragment != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }

                    NewEventFragment eventFragment = (NewEventFragment) getActivity().getSupportFragmentManager().findFragmentByTag("newEvent");
                    if(eventFragment != null)
                        eventFragment.reset();
                    else {
                        eventFragment = (NewEventFragment) getActivity().getSupportFragmentManager().findFragmentByTag("editEvent");
                        if(eventFragment != null)
                            eventFragment.reset();
                    }

                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(checkbox){
                CheckBox logCheckBox = (CheckBox) view.findViewById(R.id.log_selected);

                if(logCheckBox.isChecked()) {
                    logCheckBox.setChecked(false);
                    selectedEntries.remove(logEntries.get(position));
                }
                else {
                    logCheckBox.setChecked(true);
                    selectedEntries.add(logEntries.get(position));
                }
            }
            else {
                logDisplay = logEntries.get(position);
                GalleryFragment.photos = logDisplay.getPhotos();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                LogDetails newFragment = LogDetails.newInstance();
                newFragment.show(ft, "logDetails");
            }
            }
        });

        return rootView;
    }

    public static void getDataFromDB(Activity activity){
        logEntries.clear();
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();
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
        Collections.sort(logEntries);
    }

    public static void deleteEntryFromDB(LogEntryObject log, Activity activity){
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();

        if(db.delete(LoggerContract.LogEntry.TABLE_NAME, LoggerContract.LogEntry._ID + "=" + log.getId(), null) > 0)
            Toast.makeText(activity,"Log successfully removed !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity,"Error removing Log", Toast.LENGTH_SHORT).show();
        LoggerFragment.logEntries.remove(log);
        LoggerFragment.logAdapter.notifyDataSetChanged();
    }

}

