package de.fhdw.app_entwicklung.chatgpt.speech;

import static de.fhdw.app_entwicklung.chatgpt.WidgetProvider.ACTION_AUDIO_FINISHED;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Locale;

import de.fhdw.app_entwicklung.chatgpt.WidgetProvider;

public class TextToSpeechTool implements TextToSpeech.OnInitListener {
    private final TextToSpeech textToSpeech;
    private boolean ttsAvailable = false;
    private final Locale locale;

    public TextToSpeechTool(@NonNull Context context, Locale locale)
    {
        textToSpeech = new TextToSpeech(context, this);
        this.locale = locale;

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                Log.i("TTS", "DONE");
                Intent intent = new Intent(context, WidgetProvider.class);
                intent.setAction(ACTION_AUDIO_FINISHED);
                context.sendBroadcast(intent);
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "TTS: This Language is not supported");
            } else {
                ttsAvailable = true;
            }
        } else {
            Log.e("error", "Failed to initialize TTS");
        }
    }

    public void speak(String text) {
        if (ttsAvailable) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        }
    }

    public void stop() {
        if (ttsAvailable) {
            textToSpeech.stop();
        }
    }

    public void destroy()
    {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }


}