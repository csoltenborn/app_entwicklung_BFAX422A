package de.fhdw.app_entwicklung.chatgpt;

import android.os.Bundle;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
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
    private Chat selectedChat;
    private List<Chat> chats;
    private Spinner spinner;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
                Message userMessage = new Message(Author.User, query);
                selectedChat.addMessage(userMessage);
                if (selectedChat.getMessages().size() > 1) {
                    getTextView().append(CHAT_SEPARATOR);
                }
                getTextView().append(toString(userMessage));

                MainActivity.backgroundExecutorService.execute(() -> {
                    String apiToken = prefs.getApiToken();
                    ChatGpt chatGpt = new ChatGpt(apiToken);
                    String answer = chatGpt.getChatCompletion(selectedChat);

                    Message answerMessage = new Message(Author.Assistant, answer);
                    selectedChat.addMessage(answerMessage);
                    getTextView().append(CHAT_SEPARATOR);
                    getTextView().append(toString(answerMessage));
                    textToSpeech.speak(answer);
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

        prefs = new PrefsFacade(requireContext());
        textToSpeech = new TextToSpeechTool(requireContext(), Locale.GERMAN);
        selectedChat = new Chat();
        if (savedInstanceState != null) {
            selectedChat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
        }


        getAskButton().setOnClickListener(v ->
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(Locale.GERMAN)));

        getNewButton().setOnClickListener(v -> chats.add(new Chat()));

        getDeleteButton().setOnClickListener(v -> {
            chats.remove(selectedChat);
            updateTextView();
        });

        updateTextView();

        // Set InputType Null to disable Text editing on Dropdown TextView
        spinner = (Spinner) super.getActivity().findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedChat = (Chat) adapterView.getItemAtPosition(i);
                Toast.makeText(requireContext(), selectedChat.toString(), Toast.LENGTH_SHORT).show();
                updateTextView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        chats = new ArrayList<>();
        chats.add(selectedChat);

        // Setting Spinner Items
        spinner.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, chats));

        // Making TextView Scrollable
        getTextView().setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onPause() {
        super.onPause();

        textToSpeech.stop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_DATA_CHAT, selectedChat);
    }

    @Override
    public void onDestroy() {
        textToSpeech.destroy();
        textToSpeech = null;

        super.onDestroy();
    }

    private void updateTextView() {
        getTextView().setText("");
        List<Message> messages = selectedChat.getMessages();
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

    private Button getNewButton(){
        return getView().findViewById(R.id.button_new);
    }

    private Button getDeleteButton(){
        return getView().findViewById(R.id.button_delete);
    }

}