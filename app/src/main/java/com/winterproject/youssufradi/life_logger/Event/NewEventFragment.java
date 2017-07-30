package com.winterproject.youssufradi.life_logger.Event;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.winterproject.youssufradi.life_logger.Log.LoggerFragment;
import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;
import com.winterproject.youssufradi.life_logger.helpers.Contact;
import com.winterproject.youssufradi.life_logger.helpers.ContactAdapter;
import com.winterproject.youssufradi.life_logger.helpers.DatePickerFragment;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewEventFragment extends DialogFragment {

    View rootView;
    private Button startDatePickerButton;
    private EditText startDay;
    private EditText startMonth;
    private EditText startYear;
    private Button logSelectButton;
    public LoggerFragment logFragment;
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 36123;
    public static int PICK_CONTACT = 35111;
    private EditText descriptionText;
    private Button locationPickerButton;
    public static EditText locationField;
    private Button submit;
    public static EventEntryObject currentEvent;
    private EditText endDay;
    private EditText endMonth;
    private EditText endYear;
    private Button endDatePickerButton;
    private EditText startMinute;
    private EditText startHour;
    private EditText endHour;
    private EditText endMinute;
    private boolean end = false;
    private EditText eventName;
    private ListView liContact;
    public static ArrayList<Contact> contacts = new ArrayList<>();
    public static ContactAdapter contactAdapter;

    public static NewEventFragment newInstance() {
        NewEventFragment f = new NewEventFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_new_event, container, false);
        super.onViewCreated(rootView, savedInstanceState);


        logSelectButton = (Button) rootView.findViewById(R.id.event_log_select_fragment_opener);

        eventName = (EditText) rootView.findViewById(R.id.event_name);
        descriptionText = (EditText) rootView.findViewById(R.id.event_details);
        locationPickerButton = (Button) rootView.findViewById(R.id.event_location_select_intent_opener);
        locationField = (EditText) rootView.findViewById(R.id.event_location_auto_complete_field);

        startDatePickerButton = (Button) rootView.findViewById(R.id.event_start_date_picker);
        startDay = (EditText) rootView.findViewById(R.id.event_start_day_picked);
        startMonth = (EditText) rootView.findViewById(R.id.event_start_month_picked);
        startYear = (EditText) rootView.findViewById(R.id.event_start_year_picked);
        startHour = (EditText) rootView.findViewById(R.id.event_start_hour_picked);
        startMinute = (EditText) rootView.findViewById(R.id.event_start_minutes_picked);

        endDatePickerButton = (Button) rootView.findViewById(R.id.event_end_date_picker);
        endDay = (EditText) rootView.findViewById(R.id.event_end_day_picked);
        endMonth = (EditText) rootView.findViewById(R.id.event_end_month_picked);
        endYear = (EditText) rootView.findViewById(R.id.event_end_year_picked);
        endHour = (EditText) rootView.findViewById(R.id.event_end_hour_picked);
        endMinute = (EditText) rootView.findViewById(R.id.event_end_minutes_picked);


        submit = (Button) rootView.findViewById(R.id.add_new_event);

        liContact = (ListView) rootView.findViewById(R.id.event_contact_list);
        contactAdapter = new ContactAdapter(getActivity(),contacts,false);
        liContact.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();

        Calendar c = Calendar.getInstance();
        startDay.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
        startMonth.setText(Integer.toString(c.get(Calendar.MONTH)+1));
        startYear.setText(Integer.toString(c.get(Calendar.YEAR)));
        startHour.setText(Integer.toString(c.get(Calendar.HOUR_OF_DAY)));
        startMinute.setText(Integer.toString(c.get(Calendar.MINUTE)));

        endDay.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
        endMonth.setText(Integer.toString(c.get(Calendar.MONTH)+1));
        endYear.setText(Integer.toString(c.get(Calendar.YEAR)));
        endHour.setText(Integer.toString(c.get(Calendar.HOUR_OF_DAY)+1));
        endMinute.setText(Integer.toString(c.get(Calendar.MINUTE)));



        NewEventFragment fragment = (NewEventFragment) getActivity().getSupportFragmentManager().findFragmentByTag("editEvent");
        if (fragment != null) {
            eventName.setText(currentEvent.getDescription());
            descriptionText.setText(currentEvent.getDescription());
            startDay.setText(Integer.toString(currentEvent.getStartDay()));
            startMonth.setText(Integer.toString(currentEvent.getStartMonth()));
            startYear.setText(Integer.toString(currentEvent.getStartYear()));
            startHour.setText(Integer.toString(currentEvent.getStartHour()));
            startMinute.setText(Integer.toString(currentEvent.getStartMinute()));
            endDay.setText(Integer.toString(currentEvent.getEndDay()));
            endMonth.setText(Integer.toString(currentEvent.getEndMonth()));
            endYear.setText(Integer.toString(currentEvent.getEndYear()));
            endHour.setText(Integer.toString(currentEvent.getEndHour()));
            endMinute.setText(Integer.toString(currentEvent.getEndMinute()));
            locationField.setText(currentEvent.getLocation());
            contacts = currentEvent.getPeople();
            contactAdapter = new ContactAdapter(getActivity(),contacts,false);
            liContact.setAdapter(contactAdapter);
            LoggerFragment.passedEntries = currentEvent.getLogs();
            submit.setText("Edit");
        }

        //Loading Log Fragment for selected Images
        FragmentManager fm = getChildFragmentManager();
        logFragment = (LoggerFragment) fm.findFragmentByTag("logFragment");
        if (logFragment == null) {
            LoggerFragment.logEntries.clear();
            LoggerFragment.checkbox = false;
            LoggerFragment.hasArray = true;
            LoggerFragment.displaymood = false;
            logFragment = new LoggerFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.event_log_fragment_container, logFragment, "logFragment");
            ft.commit();
            fm.executePendingTransactions();
        }


        logSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                LoggerFragment newFragment = LoggerFragment.newInstance();
                LoggerFragment.checkbox = true;
                LoggerFragment.hasArray = false;
                newFragment.show(ft, "logSelector");
            }
        });

        locationPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    getActivity().startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                    Toast.makeText(getActivity(),"Following Error in launching intent Repairable: "+ e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    Toast.makeText(getActivity(),"Following Error in launching intent Not Available: "+ e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sH = Integer.parseInt(startHour.getText().toString().trim());
                int sM = Integer.parseInt(startMinute.getText().toString().trim());
                int eH = Integer.parseInt(endHour.getText().toString().trim());
                int eM = Integer.parseInt(endMinute.getText().toString().trim());
                if(sH < 0){
                    Toast.makeText(getActivity(),"Starting hour is less than 0",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sH > 23){
                    Toast.makeText(getActivity(),"Starting hour is greater than 23",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sM < 0){
                    Toast.makeText(getActivity(),"Starting minute is less than 0",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sM > 59){
                    Toast.makeText(getActivity(),"Starting minute is greater than 59",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(eH < 0){
                    Toast.makeText(getActivity(),"Ending hour is less than 0",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(eH > 23){
                    Toast.makeText(getActivity(),"Ending hour is greater than 23",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(eM < 0){
                    Toast.makeText(getActivity(),"Ending minute is less than 0",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(eM > 59){
                    Toast.makeText(getActivity(),"Ending minute is greater than 59",Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<String> temp = new ArrayList<>();
                for(int i = 0; i< LoggerFragment.logEntries.size(); i++)
                    temp.add(Long.toString(LoggerFragment.logEntries.get(i).getId()));

                EventEntryObject newEntry = new EventEntryObject(0, eventName.getText().toString().trim(),
                        descriptionText.getText().toString().trim(),locationField.getText().toString().trim(),
                        Integer.parseInt(startDay.getText().toString().trim()),
                        Integer.parseInt(startMonth.getText().toString().trim()),
                        Integer.parseInt(startYear.getText().toString().trim()),
                        Integer.parseInt(startHour.getText().toString().trim()),
                        Integer.parseInt(startMinute.getText().toString().trim()),
                        Integer.parseInt(endDay.getText().toString().trim()),
                        Integer.parseInt(endMonth.getText().toString().trim()),
                        Integer.parseInt(endYear.getText().toString().trim()),
                        Integer.parseInt(endHour.getText().toString().trim()),
                        Integer.parseInt(endMinute.getText().toString().trim()),
                        temp, contacts);

                NewEventFragment fragment = (NewEventFragment) getActivity().getSupportFragmentManager().findFragmentByTag("newEvent");
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                } else {
                    fragment = (NewEventFragment) getActivity().getSupportFragmentManager().findFragmentByTag("editEvent");
                    if (fragment != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        EventFragment.deleteEntryFromDB(currentEvent,getActivity());
                        EventFragment.eventEntries.remove(currentEvent);
                    }
                }
                newEntry.setId(insertInDB(newEntry));
                EventFragment.eventEntries.add(newEntry);
                EventFragment.eventAdapter.notifyDataSetChanged();
            }
        });

        Button contact = (Button) rootView.findViewById(R.id.event_contact_select_fragment_opener);

        contact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                getActivity().startActivityForResult(intent, PICK_CONTACT);
            }
        });



        return rootView;
    }

    public void reset() {

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.remove(logFragment);
        logFragment = (LoggerFragment) fm.findFragmentByTag("logFragment");
        logFragment.checkbox = false;
        logFragment.hasArray = true;
        logFragment.passedEntries = new ArrayList<>();
        logFragment = new LoggerFragment();
        ft.add(R.id.event_log_fragment_container, logFragment, "logFragment");
        ft.commit();
        fm.executePendingTransactions();

        logFragment.logAdapter.notifyDataSetChanged();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public long insertInDB(EventEntryObject newEntry){
        SQLiteDatabase db = new LoggerDBHelper(getActivity()).getWritableDatabase();
        ContentValues movie = createMovieValues(newEntry.getTitle(),newEntry.getDescription(), newEntry.getLocation(),
                newEntry.getStartDay(), newEntry.getStartMonth(), newEntry.getStartYear(), newEntry.getStartHour(),
                newEntry.getStartMinute(), newEntry.getEndDay(), newEntry.getEndMonth(),
                newEntry.getEndYear(), newEntry.getEndHour(), newEntry.getEndMinute(),
                newEntry.getPeople(), newEntry.getLogs());

        long eventID = db.insert(LoggerContract.EventEntry.TABLE_NAME, null, movie);
        if(eventID != -1)
            Toast.makeText(getActivity(),"Congrats on your new Event!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(),"Error adding Event", Toast.LENGTH_SHORT).show();
        db.close();
        Collections.sort(EventFragment.eventEntries);
        return eventID;
    }

    static ContentValues createMovieValues(String title, String description, String location, int startDay,
                                           int startMonth, int startYear, int startHour, int startMinute,
                                           int endDay, int endMonth, int endYear, int endHour,
                                           int endMinute, ArrayList<Contact> people, ArrayList<String> logs) {
        Gson gson = new Gson();

        ArrayList<String> temp = new ArrayList<>();

        for(int i = 0; i < people.size(); i++) {
            temp.add(people.get(i).getName());
        }
        String inputStringPeopleName= gson.toJson(temp);

        temp.clear();
        for(int i = 0; i < people.size(); i++) {
            temp.add(people.get(i).getNumber());
        }
        String inputStringPeopleNumber= gson.toJson(temp);

        String inputStringLogs= gson.toJson(logs);

        ContentValues eventValues = new ContentValues();
        eventValues.put(LoggerContract.EventEntry.COLUMN_TITLE, title);
        eventValues.put(LoggerContract.EventEntry.COLUMN_DESCRIPTION, description);
        eventValues.put(LoggerContract.EventEntry.COLUMN_LOCATION, location);
        eventValues.put(LoggerContract.EventEntry.COLUMN_START_DAY, startDay);
        eventValues.put(LoggerContract.EventEntry.COLUMN_START_MONTH, startMonth);
        eventValues.put(LoggerContract.EventEntry.COLUMN_START_YEAR, startYear);
        eventValues.put(LoggerContract.EventEntry.COLUMN_START_HOUR, startHour);
        eventValues.put(LoggerContract.EventEntry.COLUMN_START_MINUTE, startMinute);
        eventValues.put(LoggerContract.EventEntry.COLUMN_END_DAY, endDay);
        eventValues.put(LoggerContract.EventEntry.COLUMN_END_MONTH, endMonth);
        eventValues.put(LoggerContract.EventEntry.COLUMN_END_YEAR, endYear);
        eventValues.put(LoggerContract.EventEntry.COLUMN_END_HOUR, endHour);
        eventValues.put(LoggerContract.EventEntry.COLUMN_END_MINUTE, endMinute);
        eventValues.put(LoggerContract.EventEntry.COLUMN_PEOPLE_NAME, inputStringPeopleName);
        eventValues.put(LoggerContract.EventEntry.COLUMN_PEOPLE_NUMBER, inputStringPeopleNumber);
        eventValues.put(LoggerContract.EventEntry.COLUMN_LOGS, inputStringLogs);
        return eventValues;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end = false;
                showDatePicker();
            }
        });
        endDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end = true;
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(onDate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int yearR, int monthOfYear,
                              int dayOfMonth) {
            if(end){
                endDay.setText(String.valueOf(dayOfMonth));
                endMonth.setText(String.valueOf(monthOfYear+1));
                endYear.setText(String.valueOf(yearR));
            }
            else {
                startDay.setText(String.valueOf(dayOfMonth));
                startMonth.setText(String.valueOf(monthOfYear+1));
                startYear.setText(String.valueOf(yearR));
            }
        }
    };


}
