package com.winterproject.youssufradi.life_logger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoggerFragment extends Fragment {


    View rootView;
    private FloatingActionButton add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_logger, container, false);

        add = (FloatingActionButton) rootView.findViewById(R.id.add_new);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Used Later to quickly add logs", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                NewLogFragment newFragment = NewLogFragment.newInstance();
                newFragment.show(ft, "newLog");
            }
        });

        return rootView;
    }

}
