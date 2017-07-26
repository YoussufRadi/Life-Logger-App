package com.winterproject.youssufradi.life_logger.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.winterproject.youssufradi.life_logger.Event.NewEventFragment;
import com.winterproject.youssufradi.life_logger.R;

import java.util.ArrayList;

/**
 * Created by y_sam on 11/25/2016.
 */

public class ContactAdapter extends BaseAdapter {

    private final boolean flag;
    private ArrayList<Contact> contacts;
    private Context context;
    Contact cur;

    public ContactAdapter(Context context, ArrayList<Contact> contacts, boolean flag) {
        this.contacts = contacts;
        this.context = context;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.contact_layout, viewGroup, false);

        TextView name = (TextView) rootView.findViewById(R.id.contact_name);
        TextView number = (TextView) rootView.findViewById(R.id.contact_number);
        Button remove = (Button) rootView.findViewById(R.id.contact_remove);

        cur = contacts.get(i);

        name.setText(cur.getName());
        number.setText(cur.getNumber());

        if(flag)
            remove.setVisibility(View.GONE);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.remove(cur);
                NewEventFragment.contactAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }
}
