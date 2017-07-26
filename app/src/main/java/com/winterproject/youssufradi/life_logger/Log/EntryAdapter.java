package com.winterproject.youssufradi.life_logger.Log;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winterproject.youssufradi.life_logger.R;

import java.util.ArrayList;

/**
 * Created by y_sam on 11/25/2016.
 */

public class EntryAdapter extends BaseAdapter {

    private ArrayList<LogEntryObject> logs;
    private Context context;
    private FragmentActivity activity;
    private TextView edit;
    private boolean checkbox;

    public EntryAdapter(FragmentActivity activity, ArrayList<LogEntryObject> logs, boolean checkbox) {
        this.logs = logs;
        this.context = activity.getApplicationContext();
        this.activity = activity;
        this.checkbox = checkbox;
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public LogEntryObject getItem(int i) {
        return logs.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.log_entry, viewGroup, false);

        final LogEntryObject log = logs.get(i);

        ImageView thumbnailView = (ImageView) rootView.findViewById(R.id.log_image_view);
        TextView date = (TextView) rootView.findViewById(R.id.log_date_text_view);
        TextView location = (TextView) rootView.findViewById(R.id.log_location_text_view);
        TextView highlights = (TextView) rootView.findViewById(R.id.log_description_text_view);
        LinearLayout li = (LinearLayout) rootView.findViewById(R.id.log_options);
        Button remove = (Button) rootView.findViewById(R.id.log_delete_button);
        CheckBox selected = (CheckBox) rootView.findViewById(R.id.log_selected);

        if(LoggerFragment.hasArray)
            li.setVisibility(View.GONE);

        if(LoggerFragment.displaymood)
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
                                LoggerFragment.deleteEntryFromDB(log, activity);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(activity,"Ohhhh that's better we thought this too!!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete this log?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


        edit = (TextView) rootView.findViewById(R.id.log_edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewLogFragment.currentLog = log;
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                NewLogFragment newFragment = NewLogFragment.newInstance();
                newFragment.show(ft, "editLog");
            }
        });

        if(!logs.get(i).getPhotos().isEmpty())
            Glide.with(context).load(logs.get(i).getPhotos().get(0))
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumbnailView);
        date.setText(log.getDay() + " / " + log.getMonth()  + " / " + log.getYear());
        location.setText(log.getLocation());
        highlights.setText(log.getHighlights());
        return rootView;
    }


}
