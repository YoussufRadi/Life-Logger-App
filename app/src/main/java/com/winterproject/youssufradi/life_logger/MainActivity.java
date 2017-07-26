package com.winterproject.youssufradi.life_logger;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.winterproject.youssufradi.life_logger.Event.EventFragment;
import com.winterproject.youssufradi.life_logger.Event.NewEventFragment;
import com.winterproject.youssufradi.life_logger.Log.LoggerFragment;
import com.winterproject.youssufradi.life_logger.Log.NewLogFragment;
import com.winterproject.youssufradi.life_logger.firebase.SettingFragment;
import com.winterproject.youssufradi.life_logger.gallery.GalleryFragment;
import com.winterproject.youssufradi.life_logger.helpers.Contact;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.window_main, new CalenderFragment())
                    .commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        LoggerFragment.getDataFromDB(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.window_main, new SettingFragment())
//            .commit();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calender) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.window_main, new CalenderFragment())
                    .commit();

        } else if (id == R.id.nav_log) {
            LoggerFragment.getDataFromDB(this);
            LoggerFragment.checkbox = false;
            LoggerFragment.hasArray = false;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.window_main, new LoggerFragment())
                    .commit();

        } else if (id == R.id.nav_photo) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.window_main, new EventFragment())
                    .commit();

        } else if (id == R.id.nav_gallery) {
            GalleryFragment.checkBox = false;
            GalleryFragment.imagesPerRow = 4;
            GalleryFragment.phArray = false;
            GalleryFragment.photos.clear();
            if(GalleryFragment.mAdapter != null)
                GalleryFragment.getAllShownImagesPath(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.window_main, new GalleryFragment())
                    .commit();

        } else if (id == R.id.nav_voice) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.window_main, new VoiceFragment())
                    .commit();

        } else if (id == R.id.nav_info) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.window_main, new SettingFragment())
                    .commit();

        } else if (id == R.id.nav_review) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.window_main, new ReviewFragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    GalleryFragment.getAllShownImagesPath(this);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NewLogFragment.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                NewLogFragment.locationField.setText(place.getName());
                NewLogFragment.locationField.setVisibility(View.VISIBLE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Toast.makeText(this,"Following Error: "+ status.getStatusMessage(),Toast.LENGTH_SHORT).show();


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this,"You Cancelled Location Selection",Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == NewEventFragment.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                NewEventFragment.locationField.setText(place.getName());
                NewEventFragment.locationField.setVisibility(View.VISIBLE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Toast.makeText(this,"Following Error: "+ status.getStatusMessage(),Toast.LENGTH_SHORT).show();


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this,"You Cancelled Location Selection",Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode ==  NewEventFragment.PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                Contact current = new Contact(name, number);
                NewEventFragment.contacts.add(current);
                NewEventFragment.contactAdapter.notifyDataSetChanged();
            }
        }
    }

}
