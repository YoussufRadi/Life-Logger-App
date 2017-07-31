package com.winterproject.youssufradi.life_logger;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import static android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY;


public class VoiceFragment extends Fragment {

    private FloatingActionButton mic;
    private EditText input;

    String TAG = "VOICE FRAGMENT :";

    View rootView;
    private SpeechRecognizer recognizer;
    private boolean mIsListening = false;
    private ProgressDialog pDialoge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_voice, container, false);

        input = (EditText) rootView.findViewById(R.id.input);


        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},2);

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
                }
                else {
                    pDialoge = new ProgressDialog(getActivity());
                    pDialoge.setMessage("Converting text");
                    pDialoge.show();
                    recognizer.stopListening();
                    Log.e("Tag", "Stop");
                }
            }
        });


        return rootView;
    }


}