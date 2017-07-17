package com.winterproject.youssufradi.life_logger;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

    static NewLogFragment newInstance() {
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



        //Loading Gallarey Fragment for selected Images
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
        GalleryFragment.photos.clear();


        imageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                GalleryFragment newFragment = GalleryFragment.newInstance();
                GalleryFragment.selectedPhotos.clear();
                GalleryFragment.getAllShownImagesPath(getActivity());
                GalleryFragment.checkBox = true;
                GalleryFragment.imagesPerRow = 2;
                GalleryFragment.phArray = false;
                GalleryFragment.selectedPhotos.clear();
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

                NewLogFragment fragment = (NewLogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("newLog");
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }

                Log.e("highlights : ", highlightText.getText().toString());
                Log.e("day : ", day.getText().toString());
                Log.e("month : ", month.getText().toString());
                Log.e("year : ", year.getText().toString());
                Log.e("location : ", locationField.getText().toString());
                String k = "";
                for(int i = 0; i < GalleryFragment.photos.size(); i++) {
                    Log.e("Selected Photos: ", GalleryFragment.photos.get(i));
                    k = k + "   " + GalleryFragment.photos.get(i);
                }


                LoggerFragment.getData(highlightText.getText().toString() + "    " + day.getText().toString() + month.getText().toString()
                        + "    "  + year.getText().toString() + "    "  + locationField.getText().toString() + "    "  + k);
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


 class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;

    public DatePickerFragment() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }
}
