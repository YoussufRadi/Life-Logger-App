package com.winterproject.youssufradi.jarvis_smartui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.android.library.camera.GalleryHelper;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.RecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;


//public class PhotoFragment extends Fragment {
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_photo, container, false);
//    }
//}

public class LogFragment extends Fragment {

    private RadioGroup targetLanguage;
    private EditText input;
    private ImageButton mic;
    private Button translate;
    private ImageButton play;
    private TextView translatedText;
    private Button gallery;
    private Button camera;
    private ImageView loadedImage;

    private SpeechToText speechService;
    private TextToSpeech textService;
    private LanguageTranslator translationService;
    private Language selectedTargetLanguage = Language.SPANISH;

    private StreamPlayer player = new StreamPlayer();
    private CameraHelper cameraHelper;
    private GalleryHelper galleryHelper;

    private MicrophoneInputStream capture;
    private boolean listening = false;


    View rootView;

    //    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_photo, container, false);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_log, container, false);

        cameraHelper = new CameraHelper(this.getActivity());
        galleryHelper = new GalleryHelper(this.getActivity());

        speechService = initSpeechToTextService();
        textService = initTextToSpeechService();
        translationService = initLanguageTranslatorService();

        targetLanguage = (RadioGroup) rootView.findViewById(R.id.target_language);
        input = (EditText) rootView.findViewById(R.id.input);
        mic = (ImageButton) rootView.findViewById(R.id.mic);
        translate = (Button) rootView.findViewById(R.id.translate);
        play = (ImageButton) rootView.findViewById(R.id.play);
        translatedText = (TextView) rootView.findViewById(R.id.translated_text);
        gallery = (Button) rootView.findViewById(R.id.gallery_button);
        camera = (Button) rootView.findViewById(R.id.camera_button);
        loadedImage = (ImageView) rootView.findViewById(R.id.loaded_image);

        targetLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.spanish:
                        selectedTargetLanguage = Language.SPANISH;
                        break;
                    case R.id.french:
                        selectedTargetLanguage = Language.FRENCH;
                        break;
                    case R.id.italian:
                        selectedTargetLanguage = Language.ITALIAN;
                        break;
                }
            }
        });

        input.addTextChangedListener(new EmptyTextWatcher() {
            @Override public void onEmpty(boolean empty) {
                if (empty) {
                    translate.setEnabled(false);
                } else {
                    translate.setEnabled(true);
                }
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //mic.setEnabled(false);

                if(listening != true) {
                    capture = new MicrophoneInputStream(true);
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                speechService.recognizeUsingWebSocket(capture, getRecognizeOptions(), new MicrophoneRecognizeDelegate());
                            } catch (Exception e) {
                                showError(e);
                            }
                        }
                    }).start();
                    listening = true;
                } else {
                    try {
                        capture.close();
                        listening = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                new TranslationTask().execute(input.getText().toString());
            }
        });

        translatedText.addTextChangedListener(new EmptyTextWatcher() {
            @Override public void onEmpty(boolean empty) {
                if (empty) {
                    play.setEnabled(false);
                } else {
                    play.setEnabled(true);
                }
            }
        });

        play.setEnabled(false);

        play.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new SynthesisTask().execute(translatedText.getText().toString());
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                galleryHelper.dispatchGalleryIntent();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                cameraHelper.dispatchTakePictureIntent();
            }
        });
        return rootView;
    }


    private void showTranslation(final String translation) {
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                translatedText.setText(translation);
            }
        });
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

    private TextToSpeech initTextToSpeechService() {
        TextToSpeech service = new TextToSpeech();
        String username = getString(R.string.text_speech_username);
        String password = getString(R.string.text_speech_password);
        service.setUsernameAndPassword(username, password);
        return service;
    }

    private LanguageTranslator initLanguageTranslatorService() {
        LanguageTranslator service = new LanguageTranslator();
        String username = getString(R.string.language_translation_username);
        String password = getString(R.string.language_translation_password);
        service.setUsernameAndPassword(username, password);
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

    private abstract class EmptyTextWatcher implements TextWatcher {
        private boolean isEmpty = true; // assumes text is initially empty

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                isEmpty = true;
                onEmpty(true);
            } else if (isEmpty) {
                isEmpty = false;
                onEmpty(false);
            }
        }

        @Override public void afterTextChanged(Editable s) {}

        public abstract void onEmpty(boolean empty);
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

    private class TranslationTask extends AsyncTask<String, Void, String> {

        @Override protected String doInBackground(String... params) {
            showTranslation(translationService.translate(params[0], Language.ENGLISH, selectedTargetLanguage).execute().getFirstTranslation());
            return "Did translate";
        }
    }

    private class SynthesisTask extends AsyncTask<String, Void, String> {

        @Override protected String doInBackground(String... params) {
            player.playStream(textService.synthesize(params[0], Voice.EN_LISA).execute());
            return "Did synthesize";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CameraHelper.REQUEST_PERMISSION: {
                // permission granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraHelper.dispatchTakePictureIntent();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
            loadedImage.setImageBitmap(cameraHelper.getBitmap(resultCode));
        }

        if (requestCode == GalleryHelper.PICK_IMAGE_REQUEST) {
            loadedImage.setImageBitmap(galleryHelper.getBitmap(resultCode, data));
        }
    }

}