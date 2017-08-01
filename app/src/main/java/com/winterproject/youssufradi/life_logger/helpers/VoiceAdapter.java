package com.winterproject.youssufradi.life_logger.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.VoiceFragment;

import java.util.ArrayList;

/**
 * Created by y_sam on 11/25/2016.
 */

public class VoiceAdapter extends BaseAdapter {

    private final Context context;
    private final Activity activity;
    private ArrayList<String> logs;
    private String m_Text = "";

    public VoiceAdapter(Activity activity, ArrayList<String> logs) {
        this.logs = logs;
        this.activity = activity;
        this.context = activity;
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public String getItem(int i) {
        return logs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.voice_item, viewGroup, false);

        final int pos = i;

        TextView info = (TextView) rootView.findViewById(R.id.input);
        info.setText(logs.get(i));

//        final EditText editView = (EditText) rootView.findViewById(R.id.input_edit);

        Button edit = (Button) rootView.findViewById(R.id.voice_edit);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(edit.getText() == "Edit"){
//                    edit.setText("Confirm");
//                    info.setFocusable(true);
//                } else {
//                    edit.setText("Edit");
//                    logs.set(pos, info.getText().toString());
//                    info.setFocusable(false);
//                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Title");

                // Set up the input
                final EditText input = new EditText(context);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(logs.get(pos));
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        VoiceFragment.deleteEntryFromDB(pos,activity);
                        VoiceFragment.insertInDB(m_Text,activity);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


        Button remove = (Button) rootView.findViewById(R.id.voice_delete_button);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                VoiceFragment.deleteEntryFromDB(pos, activity);
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
        return rootView;
    }
}
