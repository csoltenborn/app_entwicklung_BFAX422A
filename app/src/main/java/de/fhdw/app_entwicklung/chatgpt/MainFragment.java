package de.fhdw.app_entwicklung.chatgpt;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.roomDB.ChatDTO;
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
    protected ChatDTO dataTransferObject;
    protected ChatDTO.OnChatsLoadedListener chatsLoadedListener;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
                Message userMessage = new Message(Author.User, query);
                selectedChat.addMessage(userMessage);
                if (selectedChat.getMessages().size() > 1) {
                    requireActivity().runOnUiThread(() -> getTextView().append(CHAT_SEPARATOR));
                }
                requireActivity().runOnUiThread(() -> {
                    getTextView().append(toString(userMessage));

                    MainActivity.backgroundExecutorService.execute(() -> {

                        String apiToken = prefs.getApiToken();
                        ChatGpt chatGpt = new ChatGpt(apiToken);

                        String answer = chatGpt.getChatCompletion(selectedChat);
                        Message answerMessage = new Message(Author.Assistant, answer);

                        selectedChat.addMessage(answerMessage);

                        requireActivity().runOnUiThread(() -> {
                            getTextView().append(CHAT_SEPARATOR);
                            getTextView().append(toString(answerMessage));
                            textToSpeech.speak(answer);
                        });

                    });

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatsLoadedListener = new ChatDTO.OnChatsLoadedListener() {
            @Override
            public void onChatsLoaded(List<Chat> loadedChats) {

                requireActivity().runOnUiThread(() -> {
                    chats = loadedChats;

                    // Prevent doubling selected Chat in Spinner through overwriting it with the
                    // current one.
                    Predicate<Chat> predicate =
                            c -> c.getCreationDate().equals(selectedChat.getCreationDate());
                    chats.removeIf(predicate);
                    chats.add(selectedChat);

                    // Setting Spinner Items
                    if(spinner.getAdapter() == null)
                        spinner.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, chats));
                });

            }

            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() -> setErrorMessage("Error loading Chats: " + e.getMessage()));
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);

            prefs = new PrefsFacade(requireContext());
            textToSpeech = new TextToSpeechTool(requireContext(), Locale.GERMAN);
            selectedChat = new Chat();
            if (savedInstanceState != null) {
                selectedChat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
            }

            dataTransferObject = new ChatDTO(requireContext());


            getNewButton().setOnClickListener(v -> chats.add(new Chat()));

            getStopButton().setOnClickListener(v -> textToSpeech.stop());

            getAskButton().setOnClickListener(v ->
                    getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(Locale.GERMAN)));

            getDeleteButton().setOnClickListener(v -> {
                if(chats.size() == 1){
                    setErrorMessage("There is only one Chat available. You can't delete it!");
                    updateTextView();
                    return;
                }

                chats.remove(selectedChat);

                ArrayAdapter<Chat> adapter = (ArrayAdapter<Chat>) spinner.getAdapter();
                adapter.notifyDataSetChanged();

                spinner.setSelection(0);
                selectedChat = chats.get(0);

                updateTextView();
            });

            updateTextView();

            if(spinner == null){
                spinner = getView().findViewById(R.id.spinner);
            }

            if(spinner.getOnItemSelectedListener() == null)
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedChat = (Chat) adapterView.getItemAtPosition(i);
                        updateTextView();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

            dataTransferObject.getAllChats(chatsLoadedListener);

            // Making TextView Scrollable
            getTextView().setMovementMethod(new ScrollingMovementMethod());

        } catch (Exception e) {
            setErrorMessage(e.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Object o = dataTransferObject.saveAllChats(chats);
        if(o.getClass() != Integer.class)
            setErrorMessage(((Exception)o).getMessage());

        textToSpeech.stop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        /*Saving Chats in Database*/
        Object returnValue = dataTransferObject.saveAllChats(this.chats).getClass();
        if(returnValue.getClass() == Integer.class) {
            int finished = (int) returnValue;
            if(finished == 0){
                requireActivity().finish();
            }
        }

        outState.putParcelable(EXTRA_DATA_CHAT, selectedChat);
    }

    @Override
    public void onDestroy() {
        textToSpeech.destroy();
        textToSpeech = null;
        super.onDestroy();
    }

    private void updateTextView() {
        requireActivity().runOnUiThread(() -> {
            getTextView().setText("");
            List<Message> messages = selectedChat.getMessages();
            if (!messages.isEmpty()) {
                getTextView().append(toString(messages.get(0)));
                for (int i = 1; i < messages.size(); i++) {
                    getTextView().append(CHAT_SEPARATOR);
                    getTextView().append(toString(messages.get(i)));
                }
            }
        });
    }

    private CharSequence toString(Message message) {
        return message.message;
    }

    private TextView getTextView() {
        return getView().findViewById(R.id.textView);
    }

    private TextView getErrorBox() {
        return getView().findViewById(R.id.errorTextView);
    }
    private void setErrorMessage(String errorMessage){
        getErrorBox().append(errorMessage);
    }

    private ImageButton getAskButton(){
        Object temp = getView().findViewById(R.id.button_ask);
        if(temp != null){
            return (ImageButton) temp;
        }
        else{
            throw new RuntimeException("Fatal GUI Error: Ask-Button not found.");
        }
    }

    private ImageButton getNewButton() {
        Object temp = getView().findViewById(R.id.button_new);
        if(temp != null){
            return (ImageButton) temp;
        }
        else{
            throw new RuntimeException("Fatal GUI Error: New-Button not found.");
        }
    }

    private ImageButton getDeleteButton() {
        Object temp = getView().findViewById(R.id.button_delete);
        if(temp != null){
            return (ImageButton) temp;
        }
        else{
            throw new RuntimeException("Fatal GUI Error: Delete-Button not found.");
        }
    }

    private ImageButton getStopButton(){
        Object temp = getView().findViewById(R.id.button_stop);
        if(temp != null){
            return (ImageButton) temp;
        }
        else{
            throw new RuntimeException("Fatal GUI Error: Stop-Button not found.");
        }
    }

}
