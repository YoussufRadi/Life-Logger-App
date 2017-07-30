package com.winterproject.youssufradi.life_logger.Event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winterproject.youssufradi.life_logger.R;

import java.util.ArrayList;

/**
 * Created by y_sam on 11/25/2016.
 */

public class EventAdapter extends BaseAdapter {

    private ArrayList<EventEntryObject> events;
    private Context context;
    private FragmentActivity activity;
    private TextView edit;
    private boolean checkbox;

    public EventAdapter(FragmentActivity activity, ArrayList<EventEntryObject> events, boolean checkbox) {
        this.events = events;
        this.context = activity.getApplicationContext();
        this.activity = activity;
        this.checkbox = checkbox;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public EventEntryObject getItem(int i) {
        return events.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.event_entry, viewGroup, false);

        final EventEntryObject event = events.get(i);

        TextView date = (TextView) rootView.findViewById(R.id.event_date_text_view);
        TextView location = (TextView) rootView.findViewById(R.id.event_location_text_view);
        TextView description = (TextView) rootView.findViewById(R.id.event_description_text_view);
        LinearLayout li = (LinearLayout) rootView.findViewById(R.id.event_options);
        Button remove = (Button) rootView.findViewById(R.id.event_delete_button);
        CheckBox selected = (CheckBox) rootView.findViewById(R.id.event_selected);

        if(EventFragment.displaymood)
            li.setVisibility(View.GONE);

        if(checkbox){
            li.setVisibility(View.GONE);
        }
        else
            selected.setVisibility(View.GONE);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                EventFragment.deleteEntryFromDB(event, activity);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(activity,"Ohhhh that's better we thought this too!!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete this Event?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


        edit = (TextView) rootView.findViewById(R.id.event_edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewEventFragment.currentEvent = event;
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                NewEventFragment newFragment = NewEventFragment.newInstance();
                newFragment.show(ft, "editEvent");
            }
        });


        date.setText(event.getStartDay() + " / " + event.getStartMonth()  + " / " + event.getStartYear());
        location.setText(event.getLocation());
        description.setText(event.getDescription());
        return rootView;
    }


}
