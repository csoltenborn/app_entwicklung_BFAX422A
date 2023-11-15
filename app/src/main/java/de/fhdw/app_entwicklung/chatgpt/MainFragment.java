package de.fhdw.app_entwicklung.chatgpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.LauchTextToSpeech;
import de.fhdw.app_entwicklung.chatgpt.speech.LaunchSpeechRecognition;

public class MainFragment extends Fragment {
    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
               getTextView().append(query);

                LauchTextToSpeech ttp = new LauchTextToSpeech(this.getContext());

                MainActivity.backgroundExecutorService.execute(() ->
                    {
                        ChatGpt chatGpt = new ChatGpt("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");
                        String answer = chatGpt.getChatCompletion(query);
                        getTextView().setText(answer);
                        ttp.Speak(answer);

                    });


            });

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getAskButton().setOnClickListener(v -> MainActivity.backgroundExecutorService.execute(() ->
        {
            getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs());

        }));
    }

    private TextView getTextView() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.textView);
    }

    private Button getAskButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.button_ask);
    }

}