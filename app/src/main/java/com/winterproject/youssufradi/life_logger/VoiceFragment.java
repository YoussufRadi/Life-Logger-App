package com.winterproject.youssufradi.life_logger;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.winterproject.youssufradi.life_logger.data.LoggerContract;
import com.winterproject.youssufradi.life_logger.data.LoggerDBHelper;
import com.winterproject.youssufradi.life_logger.helpers.VoiceAdapter;

import java.util.ArrayList;

import static android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY;


public class VoiceFragment extends Fragment {

    private FloatingActionButton mic;
    private ListView inputList;

    String TAG = "VOICE FRAGMENT :";

    View rootView;
    private SpeechRecognizer recognizer;
    private boolean mIsListening = false;
    private ProgressDialog pDialoge;
    private Snackbar snakBar;
    private static ArrayList<String> logs = new ArrayList<>();
    private static ArrayList<Long> ids = new ArrayList<>();
    public static VoiceAdapter vAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_voice, container, false);

        inputList = (ListView) rootView.findViewById(R.id.voice_log);
        vAdapter = new VoiceAdapter(getActivity(), logs);
        inputList.setAdapter(vAdapter);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},2);

        getDataFromDB(getActivity());

        mic = (FloatingActionButton) rootView.findViewById(R.id.mic);

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        "com.domain.app");
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);


                recognizer = SpeechRecognizer
                        .createSpeechRecognizer(getActivity().getApplicationContext());
                RecognitionListener listener = new RecognitionListener() {
                    @Override
                    public void onResults(Bundle results) {
                        mIsListening = false;
                        ArrayList<String> voiceResults = results
                                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if (voiceResults == null) {
                            Log.e(TAG, "No voice results");
                        } else {
                            Log.e(TAG, "Printing matches: ");
                            for (String match : voiceResults) {
                                Log.e(TAG, match);
                            }
                            logs.add(voiceResults.get(0));
                            insertInDB(voiceResults.get(0),getActivity());
                            vAdapter.notifyDataSetChanged();
                        }
                        pDialoge.hide();
                    }

                    @Override
                    public void onReadyForSpeech(Bundle params) {
                        Log.e(TAG, "Ready for speech");
                    }

                    @Override
                    public void onError(int error) {
                        if ((error == SpeechRecognizer.ERROR_NO_MATCH)
                                || (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)){
                            Log.e(TAG,
                                    "Error listening for speech: Timeout " + error);
                        }
                        else if(error == ERROR_RECOGNIZER_BUSY){
                            recognizer.cancel();
                            Log.e(TAG,
                                    "Error listening for speech: Busy " + error);
                        }
                        recognizer.startListening(intent);
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        Log.e(TAG, "Speech starting");
                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onEndOfSpeech() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {
                        // TODO Auto-generated method stub

                    }
                };
                recognizer.setRecognitionListener(listener);
                if (!mIsListening)
                {
                    mIsListening = true;
                    recognizer.startListening(intent);
                    snakBar = Snackbar.make(rootView,"Recording",Snackbar.LENGTH_INDEFINITE);
                    snakBar.setAction("Action", null).show();
                }
                else {
                    pDialoge = new ProgressDialog(getActivity());
                    pDialoge.setMessage("Converting text");
                    snakBar.dismiss();
                    pDialoge.show();
                    recognizer.stopListening();
                    Log.e("Tag", "Stop");
                }
            }
        });


        return rootView;
    }


    public static void getDataFromDB(Activity activity){
        logs.clear();
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();
        Cursor voiceCursor = db.query(
                LoggerContract.VoiceEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        boolean cursor = voiceCursor.moveToFirst();
        if(cursor){
            int voiceID = voiceCursor.getColumnIndex(LoggerContract.VoiceEntry._ID);
            int voiceDescription = voiceCursor.getColumnIndex(LoggerContract.VoiceEntry.COLUMN_DESCRIPTION);
            do {
                long COLUMN_ID = voiceCursor.getLong(voiceID);
                String COLUMN_DESCRIPTION = voiceCursor.getString(voiceDescription);

                logs.add(COLUMN_DESCRIPTION);
                ids.add(COLUMN_ID);

            } while(voiceCursor.moveToNext());
        }
        voiceCursor.close();
        db.close();
    }

    public static void deleteEntryFromDB(int position, Activity activity){
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();

        if(db.delete(LoggerContract.VoiceEntry.TABLE_NAME, LoggerContract.VoiceEntry._ID + "=" + Long.toString(ids.get(position)), null) > 0)
            Toast.makeText(activity,"Voice Log successfully removed !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity,"Error removing Voice Log", Toast.LENGTH_SHORT).show();
        logs.remove(position);
        ids.remove(position);
        vAdapter.notifyDataSetChanged();
    }

    public static void insertInDB(String note, Activity activity){
        SQLiteDatabase db = new LoggerDBHelper(activity).getWritableDatabase();
        ContentValues movie = createMovieValues(note);

        long noteID = db.insert(LoggerContract.VoiceEntry.TABLE_NAME, null, movie);
        if(noteID != -1)
            Toast.makeText(activity,"Congrats on your new Voice Note!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity,"Error adding Voice Note", Toast.LENGTH_SHORT).show();
        db.close();

        logs.add(note);
        ids.add(noteID);
        vAdapter.notifyDataSetChanged();

    }

    static ContentValues createMovieValues(String description) {
        ContentValues note = new ContentValues();
        note.put(LoggerContract.VoiceEntry.COLUMN_DESCRIPTION, description);
        return note;
    }


}