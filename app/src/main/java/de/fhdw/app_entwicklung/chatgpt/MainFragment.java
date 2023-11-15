package de.fhdw.app_entwicklung.chatgpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.LaunchSpeechRecognition;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;

public class MainFragment extends Fragment {

    private static final String EXTRA_DATA_CHAT = "EXTRA_DATA_CHAT";
    private static final String CHAT_SEPARATOR = "\n\n";
    private ChatGpt chatGpt;
    private TextToSpeechTool textToSpeech;
    private Chat chat;

    private ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech;

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

        PrefsFacade prefs = new PrefsFacade(requireContext());
        textToSpeech = new TextToSpeechTool(requireContext(), Locale.GERMAN);
        chat = new Chat();
        if (savedInstanceState != null) {
            chat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
        }
        String apiToken = prefs.getApiToken();
        chatGpt = new ChatGpt(apiToken);
        getTextFromSpeech = registerForActivityResult(
                new LaunchSpeechRecognition(),
                query -> {
                    Message userMessage = new Message(Author.User, query);
                    chat.addMessage(userMessage);
                    if (chat.getMessages().size() > 1) {
                        getTextView().append(CHAT_SEPARATOR);
                    }
                    getTextView().append(toString(userMessage));

                    MainActivity.backgroundExecutorService.execute(() -> {

                        String answer = this.chatGpt.getChatCompletion(chat);

                        Message answerMessage = new Message(Author.Assistant, answer);
                        chat.addMessage(answerMessage);
                        getTextView().append(CHAT_SEPARATOR);
                        getTextView().append(toString(answerMessage));
                        textToSpeech.speak(answer);
                    });
                });

        getAskButton().setOnClickListener(v ->
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(Locale.GERMAN)));
        updateTextView();
        createExtraGreetingMessage();
    }

    @Override
    public void onPause() {
        super.onPause();

        textToSpeech.stop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_DATA_CHAT, chat);
    }

    @Override
    public void onDestroy() {
        textToSpeech.destroy();
        textToSpeech = null;

        super.onDestroy();
    }

    private void createExtraGreetingMessage() {
        Intent intent = requireActivity().getIntent();

        if (intent == null || !intent.hasExtra("USER_NAME")) {
            return;
        }

        String userName = intent.getStringExtra("USER_NAME");

        MainActivity.backgroundExecutorService.execute(() -> getTextView().append(chatGpt.getGreetingsMessage(userName, getContext())));
        getTextView().append(CHAT_SEPARATOR);
    }

    private void updateTextView() {
        getTextView().setText("");
        List<Message> messages = chat.getMessages();
        if (!messages.isEmpty()) {
            getTextView().append(toString(messages.get(0)));
            for (int i = 1; i < messages.size(); i++) {
                getTextView().append(CHAT_SEPARATOR);
                getTextView().append(toString(messages.get(i)));
            }
        }
    }

    private CharSequence toString(Message message) {
        return message.message;
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