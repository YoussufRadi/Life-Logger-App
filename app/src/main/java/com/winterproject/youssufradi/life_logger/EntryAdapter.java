package com.winterproject.youssufradi.life_logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;

import java.util.ArrayList;

/**
 * Created by y_sam on 11/25/2016.
 */

public class EntryAdapter extends BaseAdapter {

    private ArrayList<LogEntryObject> logs;
    private Context context;
    private Activity activity;

    public EntryAdapter(Activity activity, ArrayList<LogEntryObject> logs) {
        this.logs = logs;
        this.context = activity.getApplicationContext();
        this.activity = activity;
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
        Button remove = (Button) rootView.findViewById(R.id.log_delete_button);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteEntryFromDB(log);
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


    public void deleteEntryFromDB(LogEntryObject log){
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();

        if(db.delete(LoggerContract.LogEntry.TABLE_NAME, LoggerContract.LogEntry._ID + "=" + log.getId(), null) > 0)
            Toast.makeText(activity,"Log successfully removed !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity,"Error removing Log", Toast.LENGTH_SHORT).show();
        LoggerFragment.logEntries.remove(log);
        LoggerFragment.logAdapter.notifyDataSetChanged();
    }
}
