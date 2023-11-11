package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.ChatAdapter;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.LaunchSpeechRecognition;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;

public class MainFragment extends Fragment {

    private static final String EXTRA_DATA_CHAT = "EXTRA_DATA_CHAT";

    private PrefsFacade prefs;
    private TextToSpeechTool textToSpeech;
    private Chat chat;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            this::askChatGPT
    );



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

        recyclerView = view.findViewById(R.id.recyclerView);
        chatAdapter = new ChatAdapter(chat.getMessages(), requireContext());
        recyclerView.setAdapter(chatAdapter);

        getMicButton().setOnClickListener(v -> getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(Locale.GERMAN)));

        getSendButton().setOnClickListener(v -> {
            askChatGPT(getQuestion().getText().toString());
            hideKeyboard();
            getQuestion().setText("");
            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
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

    public void askChatGPT(String query) {
        Message userMessage = new Message(Author.User, query);
        chat.addMessage(userMessage);
        chatAdapter.notifyItemInserted(chat.getMessages().size() - 1);

        MainActivity.backgroundExecutorService.execute(() -> {
            String apiToken = prefs.getApiToken();
            ChatGpt chatGpt = new ChatGpt(apiToken);
            String answer = chatGpt.getChatCompletion(chat);

            Message answerMessage = new Message(Author.Assistant, answer);
            chat.addMessage(answerMessage);
            textToSpeech.speak(answer);
            requireActivity().runOnUiThread(() -> {
                chatAdapter.notifyItemInserted(chat.getMessages().size() - 1);
                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            });
        });
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private FloatingActionButton getMicButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.button_mic);
    }

    private FloatingActionButton getSendButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.button_send);
    }

    private EditText getQuestion() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.question);
    }
}