package com.winterproject.youssufradi.life_logger.Event;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;
import com.winterproject.youssufradi.life_logger.helpers.Contact;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class EventFragment extends Fragment {

    View rootView;
    public static ArrayList<EventEntryObject> eventEntries = new ArrayList<>();
    private FloatingActionButton add;
    public static EventAdapter eventAdapter;
    public static EventEntryObject eventDisplay;
    private ListView listView;
    public static boolean checkbox = false;
    public static boolean hasArray = false;
    public ArrayList<EventEntryObject> selectedEntries = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_event, container, false);

        listView = (ListView) rootView.findViewById(R.id.event_list_view);

        if(hasArray)
            eventEntries = selectedEntries;
        else
            getDataFromDB(getActivity());

        eventAdapter = new EventAdapter(getActivity(), eventEntries, checkbox);
        listView.setAdapter(eventAdapter);

        add = (FloatingActionButton) rootView.findViewById(R.id.event_add_new);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Used Later to quickly add logs", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                NewEventFragment newFragment = NewEventFragment.newInstance();
                newFragment.show(ft, "newEvent");
            }
        });


        if(checkbox)
            add.setVisibility(View.GONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(checkbox){
                    CheckBox eventCheckBox = (CheckBox) view.findViewById(R.id.event_selected);

                    if(eventCheckBox.isChecked()) {
                        eventCheckBox.setChecked(false);
                        selectedEntries.remove(eventEntries.get(position));
                    }
                    else {
                        eventCheckBox.setChecked(true);
                        selectedEntries.add(eventEntries.get(position));
                    }
                }
                else {
                    eventDisplay = eventEntries.get(position);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    EventDetails newFragment = EventDetails.newInstance();
                    newFragment.show(ft, "eventDetails");
                }
            }
        });

        return rootView;
    }
    public static void getDataFromDB(Activity activity){
        eventEntries.clear();
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();
        Cursor eventCursor = db.query(
                LoggerContract.EventEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        boolean cursor = eventCursor.moveToFirst();
        if(cursor){
            int eventID = eventCursor.getColumnIndex(LoggerContract.EventEntry._ID);
            int eventTitle = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_TITLE);
            int eventDescription = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_DESCRIPTION);
            int eventLocation = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_LOCATION);
            int eventStartDay = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_START_DAY);
            int eventStartMonth = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_START_MONTH);
            int eventStartYear = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_START_YEAR);
            int eventStartHour = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_START_HOUR);
            int eventStartMinute = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_START_MINUTE);
            int eventEndDay = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_END_DAY);
            int eventEndMonth = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_END_MONTH);
            int eventEndYear = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_END_YEAR);
            int eventEndHour = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_END_HOUR);
            int eventEndMinute = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_END_MINUTE);
            int eventPeopleName = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_PEOPLE_NAME);
            int eventPeopleNumber = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_PEOPLE_NUMBER);
            int eventLogs = eventCursor.getColumnIndex(LoggerContract.EventEntry.COLUMN_LOGS);
            Gson gson = new Gson();
            do {
                long COLUMN_ID = eventCursor.getLong(eventID);
                String COLUMN_TITLE = eventCursor.getString(eventTitle);
                String COLUMN_DESCRIPTION = eventCursor.getString(eventDescription);
                String COLUMN_LOCATION = eventCursor.getString(eventLocation);
                int COLUMN_START_DAY = eventCursor.getInt(eventStartDay);
                int COLUMN_START_MONTH = eventCursor.getInt(eventStartMonth);
                int COLUMN_START_YEAR = eventCursor.getInt(eventStartYear);
                int COLUMN_START_HOUR = eventCursor.getInt(eventStartHour);
                int COLUMN_START_MINUTE = eventCursor.getInt(eventStartMinute);
                int COLUMN_END_DAY = eventCursor.getInt(eventEndDay);
                int COLUMN_END_MONTH = eventCursor.getInt(eventEndMonth);
                int COLUMN_END_YEAR = eventCursor.getInt(eventEndYear);
                int COLUMN_END_HOUR = eventCursor.getInt(eventEndHour);
                int COLUMN_END_MINUTE = eventCursor.getInt(eventEndMinute);
                String COLUMN_PEOPLE_NAME = eventCursor.getString(eventPeopleName);
                String COLUMN_PEOPLE_NUMBER = eventCursor.getString(eventPeopleNumber);
                String COLUMN_LOGS = eventCursor.getString(eventLogs);

                Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                ArrayList<String>  finalOutputStringPeopleName = gson.fromJson(COLUMN_PEOPLE_NAME,  listType);
                ArrayList<String>  finalOutputStringPeopleNumber = gson.fromJson(COLUMN_PEOPLE_NUMBER,  listType);
                ArrayList<String>  finalOutputStringLogs = gson.fromJson(COLUMN_LOGS,  listType);

                ArrayList<Contact> array = new ArrayList<>();
                for(int i = 0; i < finalOutputStringPeopleName.size(); i++)
                    array.add(new Contact(finalOutputStringPeopleName.get(i),
                            finalOutputStringPeopleNumber.get(i)));
                eventEntries.add(new EventEntryObject(COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
                        COLUMN_LOCATION,COLUMN_START_DAY, COLUMN_START_MONTH,COLUMN_START_YEAR,
                        COLUMN_START_HOUR, COLUMN_START_MINUTE, COLUMN_END_DAY, COLUMN_END_MONTH,
                        COLUMN_END_YEAR, COLUMN_END_HOUR, COLUMN_END_MINUTE, finalOutputStringLogs,
                        array));

            } while(eventCursor.moveToNext());
        }
        eventCursor.close();
        db.close();
        Collections.sort(eventEntries);
    }

    public static void deleteEntryFromDB(EventEntryObject event, Activity activity){
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();

        if(db.delete(LoggerContract.EventEntry.TABLE_NAME, LoggerContract.EventEntry._ID + "=" + event.getId(), null) > 0)
            Toast.makeText(activity,"Event successfully removed !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity,"Error removing Event", Toast.LENGTH_SHORT).show();
        EventFragment.eventEntries.remove(event);
        EventFragment.eventAdapter.notifyDataSetChanged();
    }

}

