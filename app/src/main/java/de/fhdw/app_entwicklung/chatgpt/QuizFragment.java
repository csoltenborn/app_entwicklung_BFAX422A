package de.fhdw.app_entwicklung.chatgpt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;


public class QuizFragment extends Fragment {

    private int rightAnswers = 0;
    private int wrongAnswers = 0;

    private boolean firstquestion = true;
    private Chat quizchat;
    private ChatGpt chatgpt;
    private PrefsFacade prefs;
    public QuizFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRightAnswersTextView().setText("Right answers: " + rightAnswers);
        getWrongAnswersTextView().setText("Wrong answers: " + wrongAnswers);

        prefs = new PrefsFacade(requireContext());
        quizchat = new Chat();

        chatgpt = new ChatGpt(prefs.getApiToken());

        Intent i = new Intent(this.getContext(), MainActivity.class);
        getChatButton().setOnClickListener(v-> startActivity(i));

        getNextQuestionButton().setOnClickListener(v-> getQuestion());
        getYesButton().setOnClickListener(v-> getResponse("Ja"));
        getNoButton().setOnClickListener(v-> getResponse("Nein"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    private void getQuestion(){
        getResponseTextView().setText("");
        MainActivity.backgroundExecutorService.execute(() -> {

            Message question = new Message(Author.User, "");
            if(firstquestion = true){
                question = new Message(Author.User, "Stelle mir eine Allgemeinwissen Ja/Nein Quizfrage. Antworte auf meine Antwort nur mit 'Richtig' oder 'Falsch'");
                firstquestion = false;
            }else question = new Message(Author.User, "Noch eine");

            quizchat.addMessage(question);

            String answer = chatgpt.getChatCompletion(quizchat);
            Message answermessage = new Message(Author.Assistant, answer);
            quizchat.addMessage(answermessage);
            getQuestionTextView().setText(toString(answermessage));

        });

    }

    private void getResponse(String antwort){

        MainActivity.backgroundExecutorService.execute(() -> {
        Message quizanswer = new Message(Author.User, antwort);
        quizchat.addMessage(quizanswer);

        String response = chatgpt.getChatCompletion(quizchat);

        if(response.contains("Richtig")){
            rightAnswers += 1;
            getRightAnswersTextView().setText("Right answers: " + rightAnswers);
        }
        if(response.contains("Falsch")){
            wrongAnswers += 1;
            getWrongAnswersTextView().setText("Wrong answers: " + wrongAnswers);
        }

        Message responsemessage = new Message(Author.Assistant, response);

        quizchat.addMessage(responsemessage);
        getResponseTextView().setText(toString(responsemessage));

        });
    }

    private TextView getResponseTextView(){
        return getView().findViewById(R.id.response);
    }
    private TextView getQuestionTextView(){
        return getView().findViewById(R.id.question);
    }
    private TextView getRightAnswersTextView(){
        return getView().findViewById(R.id.rightAnswers);
    }
    private TextView getWrongAnswersTextView(){
        return getView().findViewById(R.id.wrongAnswers);
    }
    private Button getChatButton() {
        return getView().findViewById(R.id.toChat);
    }
    private Button getNextQuestionButton(){
        return getView().findViewById(R.id.nextQuestion);
    }
    private Button getYesButton(){
        return getView().findViewById(R.id.b_yes);
    }
    private Button getNoButton() {
        return getView().findViewById(R.id.b_no);
    }
    private CharSequence toString(Message message) {
        return message.message;
    }
}