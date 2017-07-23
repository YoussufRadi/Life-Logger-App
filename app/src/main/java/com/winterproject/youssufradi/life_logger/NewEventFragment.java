package com.winterproject.youssufradi.life_logger;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewEventFragment extends DialogFragment {

    View rootView;
    private Button datePickerButton;
    private EditText day;
    private EditText month;
    private EditText year;
    private Button imageSelectButton;
    public GalleryFragment galleryFragment;
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 26123;
    private EditText descriptionText;
    private Button locationPickerButton;
    public static EditText locationField;
    private Button submit;
    public static EventEntryObject currentEvent;

    static NewEventFragment newInstance() {
        NewEventFragment f = new NewEventFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_new_event, container, false);
        super.onViewCreated(rootView, savedInstanceState);

        datePickerButton = (Button) rootView.findViewById(R.id.date_picker);
        imageSelectButton = (Button) rootView.findViewById(R.id.image_select_fragment_opener);
        day = (EditText) rootView.findViewById(R.id.day_picked);
        month = (EditText) rootView.findViewById(R.id.month_picked);
        year = (EditText) rootView.findViewById(R.id.year_picked);

        descriptionText = (EditText) rootView.findViewById(R.id.highlight_details);
        locationPickerButton = (Button) rootView.findViewById(R.id.location_select_intent_opener);
        locationField = (EditText) rootView.findViewById(R.id.location_auto_complete_field);
        submit = (Button) rootView.findViewById(R.id.add_new_event);


        Calendar c = Calendar.getInstance();
        day.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
        month.setText(Integer.toString(c.get(Calendar.MONTH)+1));
        year.setText(Integer.toString(c.get(Calendar.YEAR)));

        GalleryFragment.photos.clear();

        NewEventFragment fragment = (NewEventFragment) getActivity().getSupportFragmentManager().findFragmentByTag("editEvent");
        if (fragment != null) {
            descriptionText.setText(currentEvent.getDescription());
            day.setText(Integer.toString(currentEvent.getDay()));
            month.setText(Integer.toString(currentEvent.getMonth()));
            year.setText(Integer.toString(currentEvent.getYear()));
            locationField.setText(currentEvent.getLocation());
//            GalleryFragment.photos = currentEvent.getPhotos();
            submit.setText("Edit");
        }

        //Loading Gallery Fragment for selected Images
        FragmentManager fm = getChildFragmentManager();
        galleryFragment = (GalleryFragment) fm.findFragmentByTag("galleryFragment");
        if (galleryFragment == null) {
            galleryFragment = new GalleryFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.gallery_fragment_container, galleryFragment, "galleryFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        GalleryFragment.checkBox = false;
        GalleryFragment.phArray = true;
        GalleryFragment.imagesPerRow = 2;


        imageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                GalleryFragment newFragment = GalleryFragment.newInstance();
                GalleryFragment.getAllShownImagesPath(getActivity());
                GalleryFragment.checkBox = true;
                GalleryFragment.imagesPerRow = 2;
                GalleryFragment.phArray = false;
                GalleryFragment.photos.clear();
                newFragment.show(ft, "gallerySelector");
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
                EventEntryObject newEntry = new EventEntryObject(0,
                        descriptionText.getText().toString().trim(),locationField.getText().toString().trim(),
                        Integer.parseInt(day.getText().toString().trim()),
                        Integer.parseInt(month.getText().toString().trim()),
                        Integer.parseInt(year.getText().toString().trim()), GalleryFragment.photos, GalleryFragment.photos);

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



        return rootView;
    }

    public void reset() {

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.remove(galleryFragment);
        galleryFragment = (GalleryFragment) fm.findFragmentByTag("galleryFragment");
        galleryFragment.checkBox = false;
        galleryFragment.phArray = true;
        galleryFragment.imagesPerRow = 2;
        galleryFragment = new GalleryFragment();
        ft.add(R.id.gallery_fragment_container, galleryFragment, "galleryFragment");
        ft.commit();
        fm.executePendingTransactions();

        galleryFragment.mAdapter.notifyDataSetChanged();
        galleryFragment.recyclerView.invalidate();


    }

    public long insertInDB(EventEntryObject newEntry){
        SQLiteDatabase db = new LoggerDBHelper(getActivity()).getWritableDatabase();
        ContentValues movie = createMovieValues(newEntry.getDescription(), newEntry.getLocation(),
                newEntry.getDay(), newEntry.getMonth(), newEntry.getYear(),
                newEntry.getPeople(), newEntry.getLogs());

        long movieID = db.insert(LoggerContract.EventEntry.TABLE_NAME, null, movie);
        if(movieID != -1)
            Toast.makeText(getActivity(),"Congrats on your new Event!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(),"Error adding Event", Toast.LENGTH_SHORT).show();
        db.close();
        Collections.sort(EventFragment.eventEntries);
        return movieID;
    }

    static ContentValues createMovieValues(String description, String location, int day,
                                           int month, int year, ArrayList<String> people,
                                           ArrayList<String> logs) {
        Gson gson = new Gson();
        String inputStringPeople= gson.toJson(people);
        String inputStringLogs= gson.toJson(logs);
        ContentValues eventValues = new ContentValues();
        eventValues.put(LoggerContract.EventEntry.COLUMN_DESCRIPTION, description);
        eventValues.put(LoggerContract.EventEntry.COLUMN_LOCATION, location);
        eventValues.put(LoggerContract.EventEntry.COLUMN_DAY, day);
        eventValues.put(LoggerContract.EventEntry.COLUMN_MONTH, month);
        eventValues.put(LoggerContract.EventEntry.COLUMN_YEAR, year);
        eventValues.put(LoggerContract.EventEntry.COLUMN_PEOPLE, inputStringPeople);
        eventValues.put(LoggerContract.EventEntry.COLUMN_LOGS, inputStringLogs);
        return eventValues;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int yearR, int monthOfYear,
                              int dayOfMonth) {
            day.setText(String.valueOf(dayOfMonth));
            month.setText(String.valueOf(monthOfYear+1));
            year.setText(String.valueOf(yearR));
        }
    };


}
