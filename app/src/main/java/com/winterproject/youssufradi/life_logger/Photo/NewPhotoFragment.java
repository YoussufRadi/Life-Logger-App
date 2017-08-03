package com.winterproject.youssufradi.life_logger.Photo;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;
import com.winterproject.youssufradi.life_logger.gallery.GalleryFragment;
import com.winterproject.youssufradi.life_logger.helpers.Contact;
import com.winterproject.youssufradi.life_logger.helpers.ContactAdapter;

import java.util.ArrayList;



public class NewPhotoFragment extends DialogFragment {

    public final static int PICK_CONTACT = 10344;

    View rootView;
    private EditText photoName;
    private EditText descriptionText;
    public GalleryFragment galleryFragment;
    private Button imageSelectButton;
    private Button submit;

    public static PhotoEntryObject currentPhotos;
    private ListView liContact;
    public static ContactAdapter contactAdapter;
    public static ArrayList<Contact> contacts = new ArrayList<>();

    public static NewPhotoFragment newInstance() {
        NewPhotoFragment f = new NewPhotoFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_new_photo, container, false);
        super.onViewCreated(rootView, savedInstanceState);

        imageSelectButton = (Button) rootView.findViewById(R.id.photo_new_gallery_selector);

        photoName = (EditText) rootView.findViewById(R.id.photo_new_name);
        descriptionText = (EditText) rootView.findViewById(R.id.photo_new_description);

        liContact = (ListView) rootView.findViewById(R.id.photo_contact_list);
        contactAdapter = new ContactAdapter(getActivity(),contacts,false);
        liContact.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();

        submit = (Button) rootView.findViewById(R.id.add_new_photo);


        NewPhotoFragment fragment = (NewPhotoFragment) getActivity().getSupportFragmentManager().findFragmentByTag("editPhoto");
        if (fragment != null) {
            photoName.setText(currentPhotos.getName());
            descriptionText.setText(currentPhotos.getDescription());
            contacts = currentPhotos.getContacts();
            contactAdapter = new ContactAdapter(getActivity(),contacts,false);
            liContact.setAdapter(contactAdapter);
            GalleryFragment.photos = currentPhotos.getPhotos();
            Log.e("PHOTO SIZE : ", currentPhotos.getPhotos().size() + "");
            submit.setText("Edit");
            if(currentPhotos.getType() == 1) {
                RelativeLayout text = (RelativeLayout) rootView.findViewById(R.id.contact_layout);
                text.setVisibility(View.GONE);
                TextView text1 = (TextView) rootView.findViewById(R.id.description_text_view);
                descriptionText.setVisibility(View.GONE);
                text1.setVisibility(View.GONE);
                liContact.setVisibility(View.GONE);
            }
        }

        //Loading Gallery Fragment for selected Images
        FragmentManager fm = getChildFragmentManager();
        galleryFragment = (GalleryFragment) fm.findFragmentByTag("galleryFragment");
        if (galleryFragment == null) {
            galleryFragment = new GalleryFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.photo_gallery_fragment_container, galleryFragment, "galleryFragment");
            ft.commit();
            fm.executePendingTransactions();
        }

        GalleryFragment.checkBox = false;
        GalleryFragment.phArray = true;
        GalleryFragment.imagesPerRow = 2;


        imageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                GalleryFragment newFragment = GalleryFragment.newInstance();
                GalleryFragment.getAllShownImagesPath(getActivity());
                GalleryFragment.checkBox = true;
                GalleryFragment.imagesPerRow = 2;
                GalleryFragment.phArray = false;
                GalleryFragment.photos = new ArrayList<>();
                newFragment.show(ft, "gallerySelector");
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotoEntryObject newEntry = new PhotoEntryObject( 0, photoName.getText().toString().trim(),
                        descriptionText.getText().toString().trim(), 0, GalleryFragment.photos, contacts);

                NewPhotoFragment fragment = (NewPhotoFragment) getActivity().getSupportFragmentManager().findFragmentByTag("newPhoto");
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                } else {
                    fragment = (NewPhotoFragment) getActivity().getSupportFragmentManager().findFragmentByTag("editPhoto");
                    if (fragment != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        PhotoFragment.deleteEntryFromDB(currentPhotos,getActivity(),true);
                        PhotoFragment.customEntries.remove(currentPhotos);
                    }
                }
                newEntry.setId(insertInDB(newEntry, getActivity()));
                PhotoFragment.customEntries.add(newEntry);
                PhotoFragment.customAdaptor.notifyDataSetChanged();
            }
        });

        Button contact = (Button) rootView.findViewById(R.id.photo_contact_select_fragment_opener);

        contact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                getActivity().startActivityForResult(intent, PICK_CONTACT);
            }
        });



        return rootView;
    }

    public void reset() {

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.remove(galleryFragment);
        galleryFragment = (GalleryFragment) fm.findFragmentByTag("galleryFragment");
        galleryFragment.checkBox = false;
        galleryFragment.phArray = true;
        galleryFragment.imagesPerRow = 2;
        galleryFragment = new GalleryFragment();
        ft.add(R.id.photo_gallery_fragment_container, galleryFragment, "galleryFragment");
        ft.commit();
        fm.executePendingTransactions();

        galleryFragment.mAdapter.notifyDataSetChanged();
        galleryFragment.recyclerView.invalidate();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public static long insertInDB(PhotoEntryObject newEntry, Activity activity){
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();
        ContentValues movie = createMovieValues(newEntry.getName(),newEntry.getDescription(),
                newEntry.getType(), newEntry.getContacts(), newEntry.getPhotos());

        long photoID = db.insert(LoggerContract.PhotoEntry.TABLE_NAME, null, movie);
        if(photoID != -1)
            Toast.makeText(activity,"Congrats on your new Gallery!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity,"Error adding Gallery", Toast.LENGTH_SHORT).show();
        db.close();
        return photoID;
    }

    static ContentValues createMovieValues(String title, String description, int type,
                                           ArrayList<Contact> people, ArrayList<String> photos) {
        Gson gson = new Gson();

        ArrayList<String> temp = new ArrayList<>();

        for(int i = 0; i < people.size(); i++) {
            temp.add(people.get(i).getName());
        }
        String inputStringPeopleName= gson.toJson(temp);

        temp.clear();
        for(int i = 0; i < people.size(); i++) {
            temp.add(people.get(i).getNumber());
        }
        String inputStringPeopleNumber= gson.toJson(temp);

        String inputStringPhotos= gson.toJson(photos);

        ContentValues photoValues = new ContentValues();
        photoValues.put(LoggerContract.PhotoEntry.COLUMN_NAME, title);
        photoValues.put(LoggerContract.PhotoEntry.COLUMN_DESCRIPTION, description);
        photoValues.put(LoggerContract.PhotoEntry.COLUMN_PEOPLE_NAME, inputStringPeopleName);
        photoValues.put(LoggerContract.PhotoEntry.COLUMN_TYPE, type);
        photoValues.put(LoggerContract.PhotoEntry.COLUMN_PEOPLE_NUMBER, inputStringPeopleNumber);
        photoValues.put(LoggerContract.PhotoEntry.COLUMN_PHOTOS, inputStringPhotos);
        return photoValues;
    }

}
