package com.winterproject.youssufradi.life_logger.Photo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;
import com.winterproject.youssufradi.life_logger.gallery.GalleryFragment;
import com.winterproject.youssufradi.life_logger.helpers.Contact;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PhotoFragment extends Fragment {

    View rootView;
    private GridView customView;
    private GridView generatedView;
    public static ArrayList<PhotoEntryObject> customEntries = new ArrayList<>();
    public static ArrayList<PhotoEntryObject> generatedEntries = new ArrayList<>();
    public static PhotoAdapter customAdaptor;
    public static PhotoAdapter generatedAdaptor;
    public static PhotoEntryObject photoDisplay;
    private FloatingActionButton add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_photo, container, false);

        customView = (GridView) rootView.findViewById(R.id.photo_custom);
        generatedView = (GridView) rootView.findViewById(R.id.photo_generated);

        customAdaptor = new PhotoAdapter(getActivity(),customEntries);
        generatedAdaptor = new PhotoAdapter(getActivity(),generatedEntries);

        customView.setAdapter(customAdaptor);
        generatedView.setAdapter(generatedAdaptor);

        getDataFromDB(getActivity(),true);
        getDataFromDB(getActivity(),false);

        add = (FloatingActionButton) rootView.findViewById(R.id.photo_add_new);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Used Later to quickly add logs", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                NewPhotoFragment newFragment = NewPhotoFragment.newInstance();
                newFragment.show(ft, "newPhoto");
            }
        });

        generatedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                photoDisplay = generatedEntries.get(position);
                GalleryFragment.photos = photoDisplay.getPhotos();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                PhotoDetails newFragment = PhotoDetails.newInstance();
                newFragment.show(ft, "photoDetails");
            }
        });


        FloatingActionButton refresh  = (FloatingActionButton) rootView.findViewById(R.id.photo_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter Watson Api");

                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                getLocation();
            }
        });

        return rootView;
    }

    public void getLocation(){
        GalleryFragment.getAllShownImagesPath(getActivity());
        for(int i = 0; i < GalleryFragment.photos.size(); i++){
            String filePath = GalleryFragment.photos.get(i);
            Log.e("Photo Path : ", filePath);
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"File Not found", Toast.LENGTH_SHORT);
                return;
            }

            String lat_data;
            String lon_data;

            float[] LatLong = new float[2];
            if(exif.getLatLong(LatLong)){
                lat_data = Float.toString(LatLong[0]);
                lon_data = Float.toString(LatLong[1]);
            }else{
                Log.e("Photo Error : ",  "No latitude and longitude");
                continue;
            }
            Log.e("Lat and Log", lat_data + "   xxxxx  " + lon_data);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            if(lat_data != null && lon_data != null)
                try {
                    addresses = geocoder.getFromLocation(Double.parseDouble(lat_data), Double.parseDouble(lon_data), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
//                    Log.e("Photo Location : ", state);
//                    Log.e("Photo Location : ", country);
                    boolean flag = true;
                    for(int j = 0; j < generatedEntries.size(); j++)
                        if(generatedEntries.get(j).getName().equals(country)){
                            PhotoEntryObject entry  = generatedEntries.get(j);
                            if(entry.getPhotos().contains(filePath)){
                                flag = false;
                                break;
                            }
                            entry.addNewPhoto(filePath);
                            deleteEntryFromDB(entry,getActivity(),false);
                            entry.setId(NewPhotoFragment.insertInDB(entry,getActivity()));
                            generatedEntries.add(entry);
                            flag = false;
                        }
                    if(flag) {
                        PhotoEntryObject entry = new PhotoEntryObject(0, country, "", 1, filePath);
                        generatedEntries.add(entry);
                        entry.setId(NewPhotoFragment.insertInDB(entry, getActivity()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Photo Error", "Cannot Get Location");
                }
        }
        generatedAdaptor.notifyDataSetChanged();
    }

    public static void getDataFromDB(Activity activity, boolean custom){

        if(custom)
            customEntries.clear();
        else
            generatedEntries.clear();

        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();
        Cursor photoCursor = db.query(
                LoggerContract.PhotoEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        boolean cursor = photoCursor.moveToFirst();
        if(cursor){
            int photoID = photoCursor.getColumnIndex(LoggerContract.PhotoEntry._ID);
            int name = photoCursor.getColumnIndex(LoggerContract.PhotoEntry.COLUMN_NAME);
            int description = photoCursor.getColumnIndex(LoggerContract.PhotoEntry.COLUMN_DESCRIPTION);
            int type = photoCursor.getColumnIndex(LoggerContract.PhotoEntry.COLUMN_TYPE);
            int photoPhotos = photoCursor.getColumnIndex(LoggerContract.PhotoEntry.COLUMN_PHOTOS);
            int photoPeopleName = photoCursor.getColumnIndex(LoggerContract.PhotoEntry.COLUMN_PEOPLE_NAME);
            int photoPeopleNumber = photoCursor.getColumnIndex(LoggerContract.PhotoEntry.COLUMN_PEOPLE_NUMBER);
            Gson gson = new Gson();
            do {
                long COLUMN_ID = photoCursor.getLong(photoID);
                int COLUMN_TYPE = photoCursor.getInt(type);
                String COLUMN_NAME = photoCursor.getString(name);
                String COLUMN_DESCRIPTION = photoCursor.getString(description);
                String COLUMN_PHOTOS = photoCursor.getString(photoPhotos);
                String COLUMN_PEOPLE_NAME = photoCursor.getString(photoPeopleName);
                String COLUMN_PEOPLE_NUMBER = photoCursor.getString(photoPeopleNumber);

                Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                ArrayList<String>  finalOutputString = gson.fromJson(COLUMN_PHOTOS,  listType);
                ArrayList<String>  finalOutputStringPeopleName = gson.fromJson(COLUMN_PEOPLE_NAME,  listType);
                ArrayList<String>  finalOutputStringPeopleNumber = gson.fromJson(COLUMN_PEOPLE_NUMBER,  listType);

                ArrayList<Contact> array = new ArrayList<>();
                for(int i = 0; i < finalOutputStringPeopleName.size(); i++)
                    array.add(new Contact(finalOutputStringPeopleName.get(i),
                            finalOutputStringPeopleNumber.get(i)));

                if(custom && COLUMN_TYPE == 0)
                    customEntries.add(new PhotoEntryObject(COLUMN_ID, COLUMN_NAME,COLUMN_DESCRIPTION, 0, finalOutputString, array));
                else if(!custom && COLUMN_TYPE == 1)
                    generatedEntries.add(new PhotoEntryObject(COLUMN_ID, COLUMN_NAME,COLUMN_DESCRIPTION, 1, finalOutputString, array));

            } while(photoCursor.moveToNext());
        }
        photoCursor.close();
        db.close();
    }


    public static void deleteEntryFromDB(PhotoEntryObject photo, Activity activity, boolean custom){
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();

        if(db.delete(LoggerContract.PhotoEntry.TABLE_NAME, LoggerContract.PhotoEntry._ID + "=" + photo.getId(), null) > 0)
            Toast.makeText(activity,"Gallery successfully removed !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity,"Error removing Gallery", Toast.LENGTH_SHORT).show();
        if(custom) {
            customEntries.remove(photo);
            customAdaptor.notifyDataSetChanged();
        } else {
            generatedEntries.remove(photo);
            generatedAdaptor.notifyDataSetChanged();
        }
    }
}