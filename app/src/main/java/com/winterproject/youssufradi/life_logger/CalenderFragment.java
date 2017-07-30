package com.winterproject.youssufradi.life_logger;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import com.winterproject.youssufradi.life_logger.Event.EventEntryObject;
import com.winterproject.youssufradi.life_logger.Event.EventFragment;
import com.winterproject.youssufradi.life_logger.Log.LogEntryObject;
import com.winterproject.youssufradi.life_logger.Log.LoggerFragment;

import java.util.ArrayList;

public class CalenderFragment extends Fragment {

    private View rootView;
    CalendarView calendar;
    private int lastTab;
    LinearLayout viewPager;
    TabLayout tabLayout;
    private EventFragment eventFragment;
    private LoggerFragment loggerFragment;

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

        viewPager = (LinearLayout) rootView.findViewById(R.id.calendar_details);

        switchTab();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                lastTab = tab.getPosition();
                Log.e("TAB : ", lastTab + "");
                switchTab();
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

    private void switchTab() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(lastTab == 0) {
            if(loggerFragment != null)
                ft.remove(loggerFragment);
            eventFragment = (EventFragment) fm.findFragmentByTag("calenderEventFragment");
            if (eventFragment == null) {
                eventFragment = new EventFragment();
                EventFragment.checkbox = false;
                EventFragment.hasArray = false;
                EventFragment.displaymood = true;
                ft.add(R.id.calendar_details, eventFragment, "calenderEventFragment");
                ft.commit();
                fm.executePendingTransactions();
            }

        } else if(lastTab == 1) {
            if(eventFragment != null)
                ft.remove(eventFragment);
            loggerFragment = (LoggerFragment) fm.findFragmentByTag("calenderLogFragment");
            if (loggerFragment == null) {
                LoggerFragment.checkbox = false;
                LoggerFragment.hasArray = false;
                LoggerFragment.displaymood = true;
                LoggerFragment.getDataFromDB(getActivity());
                loggerFragment = new LoggerFragment();
                ft.add(R.id.calendar_details, loggerFragment, "calenderLogFragment");
                ft.commit();
                fm.executePendingTransactions();
            }
        }
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
                    EventFragment.getDataFromDB(getActivity());
                    ArrayList<EventEntryObject> event = new ArrayList<>();
                    for (int i = 0; i < EventFragment.eventEntries.size(); i++) {
                        if (EventFragment.eventEntries.get(i).getStartDay() == day
                                && EventFragment.eventEntries.get(i).getStartMonth() == month
                                && EventFragment.eventEntries.get(i).getStartYear() == year)
                            event.add(EventFragment.eventEntries.get(i));
                    }
                    EventFragment.selectedEntries = event;
                    EventFragment.checkbox = false;
                    EventFragment.hasArray = true;

                    FragmentManager fm = getChildFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(eventFragment);
                    eventFragment = (EventFragment) fm.findFragmentByTag("calenderEventFragment");
                    eventFragment = new EventFragment();
                    ft.add(R.id.calendar_details, eventFragment, "calenderEventFragment");
                    ft.commit();
                    fm.executePendingTransactions();
                    eventFragment.eventAdapter.notifyDataSetChanged();

                } else if(lastTab == 1) {

                    LoggerFragment.getDataFromDB(getActivity());
                    ArrayList<LogEntryObject> log = new ArrayList<>();
                    for (int i = 0; i < LoggerFragment.logEntries.size(); i++) {
                        if (LoggerFragment.logEntries.get(i).getDay() == day
                                && LoggerFragment.logEntries.get(i).getMonth() == month
                                && LoggerFragment.logEntries.get(i).getYear() == year)
                            log.add(LoggerFragment.logEntries.get(i));
                    }
                    LoggerFragment.selectedEntries = log;
                    LoggerFragment.passedEntries.clear();
                    LoggerFragment.checkbox = false;
                    LoggerFragment.hasArray = true;

                    FragmentManager fm = getChildFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(loggerFragment);
                    loggerFragment = (LoggerFragment) fm.findFragmentByTag("calenderLogFragment");
                    loggerFragment = new LoggerFragment();
                    ft.add(R.id.calendar_details, loggerFragment, "calenderLogFragment");
                    ft.commit();
                    fm.executePendingTransactions();
                    loggerFragment.logAdapter.notifyDataSetChanged();
                }
            }
        });

    }

}
