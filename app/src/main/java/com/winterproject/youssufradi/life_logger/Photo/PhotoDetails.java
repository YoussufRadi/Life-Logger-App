package com.winterproject.youssufradi.life_logger.Photo;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.gallery.GalleryFragment;
import com.winterproject.youssufradi.life_logger.helpers.Contact;
import com.winterproject.youssufradi.life_logger.helpers.ContactAdapter;

import java.util.ArrayList;

public class PhotoDetails extends DialogFragment {

    View rootView;
    private TextView photoName;
    private TextView descriptionText;
    public GalleryFragment galleryFragment;
    private Button close;

    private ListView liContact;
    public ContactAdapter contactAdapter;
    public static ArrayList<Contact> contacts = new ArrayList<>();

    public static PhotoDetails newInstance() {
        PhotoDetails f = new PhotoDetails();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_photo_details, container, false);
        super.onViewCreated(rootView, savedInstanceState);


        photoName = (TextView) rootView.findViewById(R.id.photo_details_new_name);
        descriptionText = (TextView) rootView.findViewById(R.id.photo_details_new_description);

        liContact = (ListView) rootView.findViewById(R.id.photo_details_contact_list);
        contactAdapter = new ContactAdapter(getActivity(),contacts,false);
        liContact.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();

        close = (Button) rootView.findViewById(R.id.photo_close);

        photoName.setText(PhotoFragment.photoDisplay.getName());
        descriptionText.setText(PhotoFragment.photoDisplay.getDescription());
        contacts = PhotoFragment.photoDisplay.getContacts();
        contactAdapter = new ContactAdapter(getActivity(),contacts,false);
        liContact.setAdapter(contactAdapter);
        if(contacts.isEmpty()){
            TextView text = (TextView) rootView.findViewById(R.id.contact_text_view);
            text.setVisibility(View.GONE);
            TextView text1 = (TextView) rootView.findViewById(R.id.description_text_view);
            descriptionText.setVisibility(View.GONE);
            text1.setVisibility(View.GONE);
            liContact.setVisibility(View.GONE);
        }
        GalleryFragment.photos = PhotoFragment.photoDisplay.getPhotos();

        //Loading Gallery Fragment for selected Images
        FragmentManager fm = getChildFragmentManager();
        galleryFragment = (GalleryFragment) fm.findFragmentByTag("galleryFragment");
        if (galleryFragment == null) {
            galleryFragment = new GalleryFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.photo_details_gallery_fragment_container, galleryFragment, "galleryFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        GalleryFragment.checkBox = false;
        GalleryFragment.phArray = true;
        GalleryFragment.imagesPerRow = 2;


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDetails fragment = (PhotoDetails) getActivity().getSupportFragmentManager().findFragmentByTag("photoDetails");
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    GalleryFragment.photos = new ArrayList<>();
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
