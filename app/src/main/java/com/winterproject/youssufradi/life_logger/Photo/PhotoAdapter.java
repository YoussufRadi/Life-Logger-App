package com.winterproject.youssufradi.life_logger.Photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winterproject.youssufradi.life_logger.R;

import java.util.ArrayList;

/**
 * Created by y_sam on 11/25/2016.
 */

public class PhotoAdapter extends BaseAdapter {

    private ArrayList<PhotoEntry> entries;
    private Context context;

    public PhotoAdapter(Context context, ArrayList<PhotoEntry> entries) {
        this.entries = entries;
        this.context = context;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public PhotoEntry getItem(int i) {
        return entries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.contact_layout, viewGroup, false);

        ImageView imageOne = (ImageView) rootView.findViewById(R.id.gallery_image_1);
        ImageView imageTow = (ImageView) rootView.findViewById(R.id.gallery_image_2);
        ImageView imageThree = (ImageView) rootView.findViewById(R.id.gallery_image_3);

        ArrayList<String> photos = entries.get(position).getPhotos();
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

        return rootView;
    }
}
