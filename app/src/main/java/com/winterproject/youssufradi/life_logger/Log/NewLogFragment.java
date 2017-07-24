package com.winterproject.youssufradi.life_logger.Log;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.winterproject.youssufradi.life_logger.helpers.DatePickerFragment;
import com.winterproject.youssufradi.life_logger.gallery.GalleryFragment;
import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;

import java.util.ArrayList;
import java.util.Collections;


public class NewLogFragment extends DialogFragment {

    View rootView;
    private Button datePickerButton;
    private EditText day;
    private EditText month;
    private EditText year;
    private Button imageSelectButton;
    public GalleryFragment galleryFragment;
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 26123;
    private EditText highlightText;
    private Button locationPickerButton;
    public static EditText locationField;
    private Button submit;
    public static LogEntryObject currentLog;

    public static NewLogFragment newInstance() {
        NewLogFragment f = new NewLogFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_new_log, container, false);
        super.onViewCreated(rootView, savedInstanceState);

        datePickerButton = (Button) rootView.findViewById(R.id.date_picker);
        imageSelectButton = (Button) rootView.findViewById(R.id.image_select_fragment_opener);
        day = (EditText) rootView.findViewById(R.id.day_picked);
        month = (EditText) rootView.findViewById(R.id.month_picked);
        year = (EditText) rootView.findViewById(R.id.year_picked);

        highlightText = (EditText) rootView.findViewById(R.id.highlight_details);
        locationPickerButton = (Button) rootView.findViewById(R.id.location_select_intent_opener);
        locationField = (EditText) rootView.findViewById(R.id.location_auto_complete_field);
        submit = (Button) rootView.findViewById(R.id.add_new_log);


        Calendar c = Calendar.getInstance();
        day.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
        month.setText(Integer.toString(c.get(Calendar.MONTH)+1));
        year.setText(Integer.toString(c.get(Calendar.YEAR)));

        GalleryFragment.photos.clear();

        NewLogFragment fragment = (NewLogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("editLog");
        if (fragment != null) {
            highlightText.setText(currentLog.getHighlights());
            day.setText(Integer.toString(currentLog.getDay()));
            month.setText(Integer.toString(currentLog.getMonth()));
            year.setText(Integer.toString(currentLog.getYear()));
            locationField.setText(currentLog.getLocation());
            GalleryFragment.photos = currentLog.getPhotos();
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
                LogEntryObject newEntry = new LogEntryObject(0,
                        highlightText.getText().toString().trim(),locationField.getText().toString().trim(),
                        Integer.parseInt(day.getText().toString().trim()),
                        Integer.parseInt(month.getText().toString().trim()),
                        Integer.parseInt(year.getText().toString().trim()), GalleryFragment.photos);
                NewLogFragment fragment = (NewLogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("newLog");
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                } else {
                    fragment = (NewLogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("editLog");
                    if (fragment != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        LoggerFragment.deleteEntryFromDB(currentLog,getActivity());
                        LoggerFragment.logEntries.remove(currentLog);
                    }
                }
                newEntry.setId(insertInDB(newEntry));
                LoggerFragment.logEntries.add(newEntry);
                LoggerFragment.logAdapter.notifyDataSetChanged();
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

    public long insertInDB(LogEntryObject newEntry){
        SQLiteDatabase db = new LoggerDBHelper(getActivity()).getWritableDatabase();
        ContentValues movie = createMovieValues(newEntry.getHighlights(), newEntry.getLocation(),
                newEntry.getDay(), newEntry.getMonth(), newEntry.getYear(), newEntry.getPhotos());
        long logID = db.insert(LoggerContract.LogEntry.TABLE_NAME, null, movie);
        if(logID != -1)
            Toast.makeText(getActivity(),"Congrats on your new Log!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(),"Error adding Log", Toast.LENGTH_SHORT).show();
        db.close();
        Collections.sort(LoggerFragment.logEntries);
        return logID;
    }

    static ContentValues createMovieValues(String highlights, String location, int day,
    int month, int year, ArrayList<String> photos) {
        Gson gson = new Gson();
        String inputString= gson.toJson(photos);
        ContentValues logValues = new ContentValues();
        logValues.put(LoggerContract.LogEntry.COLUMN_HIGHLIGHT, highlights);
        logValues.put(LoggerContract.LogEntry.COLUMN_LOCATION, location);
        logValues.put(LoggerContract.LogEntry.COLUMN_DAY, day);
        logValues.put(LoggerContract.LogEntry.COLUMN_MONTH, month);
        logValues.put(LoggerContract.LogEntry.COLUMN_YEAR, year);
        logValues.put(LoggerContract.LogEntry.COLUMN_PHOTOS, inputString);
        return logValues;
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


