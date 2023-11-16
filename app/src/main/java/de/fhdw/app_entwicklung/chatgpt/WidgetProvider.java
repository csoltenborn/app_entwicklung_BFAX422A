package de.fhdw.app_entwicklung.chatgpt;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;

public class WidgetProvider extends AppWidgetProvider {
    public static final String VOICE_RESULT ="de.fhdw.app_entwicklung.chatgpt.VOICE_RESULT";
    public static final String ACTION_STOP_AUDIO = "de.fhdw.app_entwicklung.chatgpt.ACTION_STOP_AUDIO";
    public static final String ACTION_AUDIO_FINISHED = "de.fhdw.app_entwicklung.chatgpt.ACTION_AUDIO_FINISHED";

    private static TextToSpeechTool tts;

    private PrefsFacade prefs;

    Chat chat;

    private final ExecutorService backgroundExecutorService = Executors.newFixedThreadPool(2);

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int appWidgetId: appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(VOICE_RESULT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);


        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Stelle deine Frage an ChatGpt");
        voiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT, pendingIntent);

        PendingIntent voiceResult = PendingIntent.getActivity(context, 0, voiceIntent, PendingIntent.FLAG_IMMUTABLE);

        views.setOnClickPendingIntent(R.id.app_widget_button, voiceResult);

        Intent stopIntent = new Intent(context, WidgetProvider.class);
        stopIntent.setAction(ACTION_STOP_AUDIO);
        PendingIntent pendingStopIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_MUTABLE);

        views.setOnClickPendingIntent(R.id.widget_stop_button, pendingStopIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        tts = new TextToSpeechTool(context, Locale.GERMAN);
        prefs = new PrefsFacade(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("WIDGET", intent.getAction());

        if (intent.getAction().equals(VOICE_RESULT)){
            if(tts == null){
                tts = new TextToSpeechTool(context, Locale.GERMAN);
            }
            chat = new Chat();
            prefs = new PrefsFacade(context);

            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                ArrayList<String> resultList = bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
                String result = Objects.requireNonNull(resultList).get(0);

                if (!result.isEmpty()) {
                    backgroundExecutorService.execute(() -> {
                        Message userMessage = new Message(Author.User, result);
                        chat.addMessage(userMessage);
                        String apiToken = prefs.getApiToken();
                        ChatGpt chatGpt = new ChatGpt(apiToken);
                        String answer = chatGpt.getChatCompletion(chat);
                        toggleButtonVisibility(context, true);
                        tts.speak(answer);
                    });
                }
            }
        }

        if(intent.getAction().equals(ACTION_STOP_AUDIO)){
            tts.stop();
            toggleButtonVisibility(context, false);
        }

        if(intent.getAction().equals(ACTION_AUDIO_FINISHED)){
            toggleButtonVisibility(context, false);
        }

        if(intent.getAction().equals("android.appwidget.action.APPWIDGET_ENABLED")){
            Log.i("WIDGET", "enabled");
            tts = new TextToSpeechTool(context, Locale.GERMAN);
            prefs = new PrefsFacade(context);
            chat = new Chat();
        }
    }

    void toggleButtonVisibility(Context context, boolean started){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));

        if(started) {
            views.setViewVisibility(R.id.app_widget_button, View.GONE);
            views.setViewVisibility(R.id.widget_stop_button, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.app_widget_button, View.VISIBLE);
            views.setViewVisibility(R.id.widget_stop_button, View.GONE);
        }

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
}
