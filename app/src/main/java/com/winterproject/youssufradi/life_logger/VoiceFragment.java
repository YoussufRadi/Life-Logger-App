package com.winterproject.youssufradi.life_logger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.RecognizeCallback;


public class VoiceFragment extends Fragment {

    private FloatingActionButton mic;
    private SpeechToText speechService;
    private EditText input;
    private MicrophoneInputStream capture;
    private boolean listening = false;


    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_voice, container, false);

        speechService = initSpeechToTextService();
        input = (EditText) rootView.findViewById(R.id.input);


        mic = (FloatingActionButton) rootView.findViewById(R.id.mic);

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mic.setEnabled(false);
//                if(getString(R.string.speech_text_username)  == "")
                    Snackbar.make(v, "Used Later to quickly record voice notes", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
//                else if(listening != true) {
//                    capture = new MicrophoneInputStream(true);
//                    new Thread(new Runnable() {
//                        @Override public void run() {
//                            try {
//                                speechService.recognizeUsingWebSocket(capture, getRecognizeOptions(), new MicrophoneRecognizeDelegate());
//                            } catch (Exception e) {
//                                showError(e);
//                            }
//                        }
//                    }).start();
//                    listening = true;
//                } else {
//                    try {
//                        capture.close();
//                        listening = false;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
            }
        });

        return rootView;
    }


    private void showError(final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void showMicText(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                input.setText(text);
            }
        });
    }

    private void enableMicButton() {
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                mic.setEnabled(true);
            }
        });
    }

    private SpeechToText initSpeechToTextService() {
        SpeechToText service = new SpeechToText();
        String username = getString(R.string.speech_text_username);
        String password = getString(R.string.speech_text_password);
        service.setUsernameAndPassword(username, password);
        service.setEndPoint("https://stream.watsonplatform.net/speech-to-text/api");
        return service;
    }

    private RecognizeOptions getRecognizeOptions() {
        return new RecognizeOptions.Builder()
                .continuous(true)
                .contentType(ContentType.OPUS.toString())
                .model("en-US_BroadbandModel")
                .interimResults(true)
                .inactivityTimeout(2000)
                .build();
    }


    private class MicrophoneRecognizeDelegate implements RecognizeCallback {

        @Override
        public void onTranscription(SpeechResults speechResults) {
            System.out.println(speechResults);
            if(speechResults.getResults() != null && !speechResults.getResults().isEmpty()) {
                String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                showMicText(text);
            }
        }

        @Override public void onConnected() {

        }

        @Override public void onError(Exception e) {
            showError(e);
            enableMicButton();
        }

        @Override public void onDisconnected() {
            enableMicButton();
        }
    }

}