package com.winterproject.youssufradi.life_logger;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class GalleryFragment extends DialogFragment {

    private static ArrayList<String> photos;
    public static Boolean checkBox = false;
    public static Boolean phArray = false;
    private ProgressDialog pDialog;
    private static GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private Button selectButton;

    View rootView;


    static GalleryFragment newInstance() {
        GalleryFragment f = new GalleryFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        selectButton = (Button) rootView.findViewById(R.id.image_select_button);

        pDialog = new ProgressDialog(getActivity());
        photos = new ArrayList<>();
        mAdapter = new GalleryAdapter(getActivity().getApplicationContext(), photos, checkBox);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if(checkBox) {
            selectButton.setVisibility(View.VISIBLE);
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"Visisblasdsdasd", Toast.LENGTH_SHORT).show();
                }
            });

        }
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", photos);
                bundle.putInt("position", position);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(),getParentFragment().getClass().toString(),Toast.LENGTH_SHORT);
//        checkBox = false;
//        phArray = false;
    }

    public static void getAllShownImagesPath(Activity activity) {
        if(!phArray){
            Uri uri;
            Cursor cursor;
            int column_index_data;//, column_index_folder_name;
            String absolutePathOfImage = null;
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            //column_index_folder_name =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                photos.add(absolutePathOfImage);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}