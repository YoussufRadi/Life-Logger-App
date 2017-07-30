package com.winterproject.youssufradi.life_logger.Photo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winterproject.youssufradi.life_logger.R;
import com.winterproject.youssufradi.life_logger.gallery.GalleryFragment;

import java.util.ArrayList;

/**
 * Created by y_sam on 11/25/2016.
 */

public class PhotoAdapter extends BaseAdapter {

    private final FragmentActivity activity;
    private ArrayList<PhotoEntryObject> entries;
    private Context context;

    public PhotoAdapter(FragmentActivity activity, ArrayList<PhotoEntryObject> entries) {
        this.activity = activity;
        this.entries = entries;
        this.context = activity.getApplicationContext();;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public PhotoEntryObject getItem(int i) {
        return entries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.gallery_folder, viewGroup, false);

        ImageView imageOne = (ImageView) rootView.findViewById(R.id.gallery_image_1);
        ImageView imageTow = (ImageView) rootView.findViewById(R.id.gallery_image_2);
        ImageView imageThree = (ImageView) rootView.findViewById(R.id.gallery_image_3);

        final PhotoEntryObject entry = entries.get(position);
        ArrayList<String> photos = entry.getPhotos();
        for(int i = 0 ; i  < 3 && i < photos.size(); i++){
            ImageView current = null;
            if(i == 0)
                current = imageOne;
            else if (i == 1)
                current = imageTow;
            else if (i == 2)
                current = imageThree;
            current.setVisibility(View.VISIBLE);
            Glide.with(context).load(photos.get(i))
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(current);
        }

        Button remove = (Button)rootView.findViewById(R.id.photo_delete_button);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                PhotoFragment.deleteEntryFromDB(entry, activity, true);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(activity,"Ohhhh that's better we thought this too!!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete this Gallery?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


        TextView edit = (TextView) rootView.findViewById(R.id.photo_edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPhotoFragment.currentPhotos = entry;
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                NewPhotoFragment newFragment = NewPhotoFragment.newInstance();
                newFragment.show(ft, "editPhoto");
            }
        });

        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoFragment.photoDisplay = entry;
                GalleryFragment.photos = entry.getPhotos();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                PhotoDetails newFragment = PhotoDetails.newInstance();
                newFragment.show(ft, "photoDetails");
            }
        });

        return rootView;
    }
}
