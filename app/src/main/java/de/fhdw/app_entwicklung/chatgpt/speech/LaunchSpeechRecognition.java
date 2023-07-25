package de.fhdw.app_entwicklung.chatgpt.speech;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionPart;
import android.speech.RecognizerIntent;
import androidx.activity.result.contract.ActivityResultContract;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import java.util.Locale;

public class LaunchSpeechRecognition extends ActivityResultContract<LaunchSpeechRecognition.SpeechRecognitionArgs, String> {
    
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, SpeechRecognitionArgs speechRecognitionArgs) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello");
        return intent;
    }

    @Override
    public String parseResult(int resultCode, @Nullable Intent data) {
        if (resultCode == android.app.Activity.RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                return Objects.requireNonNull(results).get(0);
            }
        }
        return null;
    }

    public static class SpeechRecognitionArgs
    {
        public SpeechRecognitionArgs()
        {
            //this.locale = locale;
        }

    }
}