package com.winterproject.youssufradi.life_logger.Photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.winterproject.youssufradi.life_logger.R;

import java.util.ArrayList;


public class PhotoFragment extends Fragment {

    View rootView;
    private ListView customView;
    private ListView generatedView;
    public static ArrayList<PhotoEntryObject> customEntries = new ArrayList<>();
    private ArrayList<PhotoEntryObject> generatedEntries = new ArrayList<>();
    public static PhotoAdapter customAdaptor;
    private PhotoAdapter generatedAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_photo, container, false);

        customView = (ListView) rootView.findViewById(R.id.photo_custom);
        generatedView = (ListView) rootView.findViewById(R.id.photo_generated);

        customAdaptor = new PhotoAdapter(getActivity(),customEntries);
        generatedAdaptor = new PhotoAdapter(getActivity(),generatedEntries);

        customView.setAdapter(customAdaptor);
        generatedView.setAdapter(generatedAdaptor);

        return rootView;
    }
}