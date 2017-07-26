package com.winterproject.youssufradi.life_logger.Event;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.winterproject.youssufradi.life_logger.Log.LoggerFragment;
import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.helpers.Contact;
import com.winterproject.youssufradi.life_logger.helpers.ContactAdapter;

import java.util.ArrayList;


public class EventDetails extends DialogFragment {

    public static EventDetails newInstance() {
        EventDetails f = new EventDetails();
        return f;
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        super.onViewCreated(rootView, savedInstanceState);

        TextView eventName = (TextView) rootView.findViewById(R.id.event_details_name);
        TextView descriptionText = (TextView) rootView.findViewById(R.id.event_details_highlight_details);
        TextView locationField = (TextView) rootView.findViewById(R.id.event_details_location_auto_complete_field);

        EditText startDay = (EditText) rootView.findViewById(R.id.event_details_start_day_picked);
        EditText startMonth = (EditText) rootView.findViewById(R.id.event_details_start_month_picked);
        EditText startYear = (EditText) rootView.findViewById(R.id.event_details_start_year_picked);
        EditText startHour = (EditText) rootView.findViewById(R.id.event_details_start_hour_picked);
        EditText startMinute = (EditText) rootView.findViewById(R.id.event_details_start_minutes_picked);

        EditText endDay = (EditText) rootView.findViewById(R.id.event_details_end_day_picked);
        EditText endMonth = (EditText) rootView.findViewById(R.id.event_details_end_month_picked);
        EditText endYear = (EditText) rootView.findViewById(R.id.event_details_end_year_picked);
        EditText endHour = (EditText) rootView.findViewById(R.id.event_details_end_hour_picked);
        EditText endMinute = (EditText) rootView.findViewById(R.id.event_details_end_minutes_picked);


        Button close = (Button) rootView.findViewById(R.id.event_details_close);
        ListView liContact = (ListView) rootView.findViewById(R.id.event_details_contact_list);


        eventName.setText(EventFragment.eventDisplay.getDescription());
        descriptionText.setText(EventFragment.eventDisplay.getDescription());
        startDay.setText(Integer.toString(EventFragment.eventDisplay.getStartDay()));
        startMonth.setText(Integer.toString(EventFragment.eventDisplay.getStartMonth()));
        startYear.setText(Integer.toString(EventFragment.eventDisplay.getStartYear()));
        startHour.setText(Integer.toString(EventFragment.eventDisplay.getStartHour()));
        startMinute.setText(Integer.toString(EventFragment.eventDisplay.getStartMinute()));
        endDay.setText(Integer.toString(EventFragment.eventDisplay.getEndDay()));
        endMonth.setText(Integer.toString(EventFragment.eventDisplay.getEndMonth()));
        endYear.setText(Integer.toString(EventFragment.eventDisplay.getEndYear()));
        endHour.setText(Integer.toString(EventFragment.eventDisplay.getEndHour()));
        endMinute.setText(Integer.toString(EventFragment.eventDisplay.getEndMinute()));
        locationField.setText(EventFragment.eventDisplay.getLocation());
        ArrayList<Contact> contacts = EventFragment.eventDisplay.getPeople();
        LoggerFragment.passedEntries = EventFragment.eventDisplay.getLogs();
        ContactAdapter contactAdapter = new ContactAdapter(getActivity(), contacts, true);
        liContact.setAdapter(contactAdapter);


        //Loading Log Fragment for selected Images
        FragmentManager fm = getChildFragmentManager();
        LoggerFragment logFragment = (LoggerFragment) fm.findFragmentByTag("logFragment");
        if (logFragment == null) {
            LoggerFragment.logEntries.clear();
            LoggerFragment.checkbox = false;
            LoggerFragment.hasArray = true;
            LoggerFragment.displaymood = false;
            logFragment = new LoggerFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.event_details_log_fragment_container, logFragment, "logFragment");
            ft.commit();
            fm.executePendingTransactions();
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDetails fragment = (EventDetails) getActivity().getSupportFragmentManager().findFragmentByTag("eventDetails");
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
