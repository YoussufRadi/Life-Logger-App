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
import android.widget.TextView;

import com.winterproject.youssufradi.life_logger.gallery.GalleryFragment;
import com.winterproject.youssufradi.life_logger.R;


public class EventDetails extends DialogFragment {

    View rootView;
    public GalleryFragment galleryFragment;
    private Button close;
    private TextView description;
    private TextView location;
    private TextView date;

    public static EventDetails newInstance() {
        EventDetails f = new EventDetails();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        super.onViewCreated(rootView, savedInstanceState);

        close = (Button) rootView .findViewById(R.id.event_close);
        description = (TextView) rootView.findViewById(R.id.event_highlight_details);
        location = (TextView) rootView.findViewById(R.id.event_location);
        date = (TextView) rootView.findViewById(R.id.event_day);

        description.setText(EventFragment.eventDisplay.getDescription());
        location.setText(EventFragment.eventDisplay.getLocation());
        date.setText(EventFragment.eventDisplay.getDay() + "/" + EventFragment.eventDisplay.getMonth()
                + "/" + EventFragment.eventDisplay.getYear());

        //Loading Gallarey Fragment for selected Images
        FragmentManager fm = getChildFragmentManager();
        galleryFragment = (GalleryFragment) fm.findFragmentByTag("galleryFragment");
        if (galleryFragment == null) {
            galleryFragment = new GalleryFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.event_gallery_fragment_container, galleryFragment, "galleryFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        GalleryFragment.checkBox = false;
        GalleryFragment.phArray = true;
        GalleryFragment.imagesPerRow = 3;

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDetails fragment = (EventDetails) getActivity().getSupportFragmentManager().findFragmentByTag("eventDetails");
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    GalleryFragment.photos.clear();
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
