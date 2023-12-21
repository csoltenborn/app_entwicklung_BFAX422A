package de.fhdw.app_entwicklung.chatgpt;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    private PrefsFacade prefs;
    private TextToSpeechTool textToSpeech;
    private Chat chat;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
                if (query != null) {
                    askChatGPT(query);
                }
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

        prefs = new PrefsFacade(requireContext());
        textToSpeech = new TextToSpeechTool(requireContext(), Locale.GERMAN);
        chat = new Chat();
        if (savedInstanceState != null) {
            chat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
        }

        getAskButton().setOnClickListener(v ->
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(Locale.GERMAN)));
        getAskTextButton().setOnClickListener(v -> askTextButton());
        updateTextView();
        getTextView().setMovementMethod(new ScrollingMovementMethod());
    }

    private void askTextButton() {
        String query = getRequest().getText().toString();
        if (!query.isEmpty()) {
            getRequest().setText("");
            askChatGPT(query);
        }
    }

    private void askChatGPT(String query) {
        Message userMessage = new Message(Author.User, query, prefs.getUsername(), "#8a50b0");
        chat.addMessage(userMessage);
        if (chat.getMessages().size() > 1) {
            getTextView().append(CHAT_SEPARATOR);
        }
        appendColoredText(getTextView(), toString(userMessage), userMessage.color);

        MainActivity.backgroundExecutorService.execute(() -> {
            String apiToken = prefs.getApiToken();
            ChatGpt chatGpt = new ChatGpt(apiToken);
            String answer = chatGpt.getChatCompletion(chat);

            Message answerMessage = new Message(Author.Assistant, answer, prefs.getGptName(), "#69a1fa");
            chat.addMessage(answerMessage);
            getTextView().append(CHAT_SEPARATOR);
            appendColoredText(getTextView(), toString(answerMessage), answerMessage.color);
            textToSpeech.speak(answer);
        });
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

    private void updateTextView() {
        getTextView().setText("");
        List<Message> messages = chat.getMessages();
        if (!messages.isEmpty()) {
            appendColoredText(getTextView(), toString(messages.get(0)), messages.get(0).color);
            for (int i = 1; i < messages.size(); i++) {
                getTextView().append(CHAT_SEPARATOR);
                appendColoredText(getTextView(), toString(messages.get(i)), messages.get(i).color);
            }
        }
    }

    private String toString(Message message) {
        return message.name + ": " + message.message;
    }

    private TextView getTextView() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.textView);
    }

    private Button getAskButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.button_ask);
    }

    private Button getAskTextButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.button_ask_text);
    }

    private EditText getRequest() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.request);
    }

    public static void appendColoredText(TextView tv, String text, String color) {
        int colorInt = Color.parseColor(color);
        int start = tv.getText().length();
        tv.append(text);
        int end = tv.getText().length();

        Spannable spannableText = (Spannable) tv.getText();
        spannableText.setSpan(new ForegroundColorSpan(colorInt), start, end, 0);
    }

}