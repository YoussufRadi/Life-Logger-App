package com.winterproject.youssufradi.life_logger;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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



public class NewLogFragment extends DialogFragment {

    View rootView;
    private Button editText;
    private EditText day;
    private EditText month;
    private EditText year;
    private Button imageSelect;


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

        editText = (Button) rootView.findViewById(R.id.date_picker);
        imageSelect = (Button) rootView.findViewById(R.id.image_select_fragment_opener);
        day = (EditText) rootView.findViewById(R.id.day_picked);
        month = (EditText) rootView.findViewById(R.id.month_picked);
        year = (EditText) rootView.findViewById(R.id.year_picked);
        Calendar c = Calendar.getInstance();
        day.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
        month.setText(Integer.toString(c.get(Calendar.MONTH)+1));
        year.setText(Integer.toString(c.get(Calendar.YEAR)));


        FragmentManager fm = getChildFragmentManager();
        GalleryFragment galleryFragment = (GalleryFragment) fm.findFragmentByTag("galleryFragment");
        if (galleryFragment == null) {
            galleryFragment = new GalleryFragment();
            GalleryFragment.checkBox = false;
            GalleryFragment.phArray = true;
            GalleryFragment.imagesPerRow = 2;
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.gallery_fragment_container, galleryFragment, "galleryFragment");
            ft.commit();
            fm.executePendingTransactions();
        }


        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                GalleryFragment newFragment = GalleryFragment.newInstance();
                GalleryFragment.checkBox = true;
                GalleryFragment.imagesPerRow = 2;
                GalleryFragment.phArray = false;
                newFragment.show(ft, "gallery Selector");
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment parentFragment = getParentFragment();
        FragmentManager manager;
        if (parentFragment != null) {
            // If parent is another fragment, then this fragment is nested
            manager = parentFragment.getChildFragmentManager();
        } else {
            // This fragment is placed into activity
            manager = getActivity().getSupportFragmentManager();
        }
        manager.beginTransaction().remove(this).commitAllowingStateLoss();
        super.onDestroyView();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText.setOnClickListener(new View.OnClickListener() {
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
