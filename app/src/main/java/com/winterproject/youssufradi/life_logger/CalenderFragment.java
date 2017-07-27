package com.winterproject.youssufradi.life_logger;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.winterproject.youssufradi.life_logger.Event.EventEntryObject;
import com.winterproject.youssufradi.life_logger.Event.EventFragment;
import com.winterproject.youssufradi.life_logger.Log.LogEntryObject;
import com.winterproject.youssufradi.life_logger.Log.LoggerFragment;
import com.winterproject.youssufradi.life_logger.helpers.PageAdapter;

import java.util.ArrayList;

public class CalenderFragment extends Fragment {

    private View rootView;
    CalendarView calendar;
    private int lastTab;
    ViewPager viewPager;
    private PageAdapter adapter;
    TabLayout tabLayout;
    //TODO Allow Calender to change data of the logs or events view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_calender, container, false);
        initializeCalendar();


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Logs"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        adapter = new PageAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                lastTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return rootView;
    }


    public void initializeCalendar() {
        calendar = (CalendarView) rootView.findViewById(R.id.calendar);
        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);
        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);
        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.bg_main));
        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.bg_main);
        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month++;
                if(lastTab == 0) {
                    PageAdapter.tab1.getDataFromDB(getActivity());
                    ArrayList<EventEntryObject> event = new ArrayList<>();
                    for (int i = 0; i < EventFragment.eventEntries.size(); i++) {
                        if (EventFragment.eventEntries.get(i).getStartDay() == day
                                && EventFragment.eventEntries.get(i).getStartMonth() == month
                                && EventFragment.eventEntries.get(i).getStartYear() == year)
                            event.add(EventFragment.eventEntries.get(i));
                    }
                    PageAdapter.tab1.selectedEntries = event;
                    PageAdapter.tab1.checkbox = false;
                    PageAdapter.tab1.hasArray = true;
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < PageAdapter.tab1.eventEntries.size(); i++) {
                        PageAdapter.tab1.eventEntries.get(i).printEvent();
                    }
                } else if(lastTab == 1) {

                    PageAdapter.tab2.getDataFromDB(getActivity());
                    ArrayList<LogEntryObject> log = new ArrayList<>();
                    for (int i = 0; i < LoggerFragment.logEntries.size(); i++) {
                        if (LoggerFragment.logEntries.get(i).getDay() == day
                                && LoggerFragment.logEntries.get(i).getMonth() == month
                                && LoggerFragment.logEntries.get(i).getYear() == year)
                            log.add(LoggerFragment.logEntries.get(i));
                    }
                    PageAdapter.tab2.logEntries = log;
                    PageAdapter.tab2.checkbox = false;
                    PageAdapter.tab2.hasArray = true;
                    adapter.notifyDataSetChanged();
                    PageAdapter.tab2 = new LoggerFragment();
                    PageAdapter.tab2.logEntries.add(log.get(0));
                    for (int i = 0; i < PageAdapter.tab2.logEntries.size(); i++) {
                        PageAdapter.tab2.logEntries.get(i).printLog();
                    }
//                    PageAdapter.tab2.logEntries.add(log.get(0));
//                    LoggerFragment.getDataFromDB(getActivity());
//                    adapter.notifyDataSetChanged();

                }
            }
        });

    }

}
