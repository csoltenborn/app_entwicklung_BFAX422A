package de.fhdw.app_entwicklung.chatgpt;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Locale;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;

public class WidgetProvider extends AppWidgetProvider {
    private static TextToSpeechTool textToSpeech;
    public static String ACTION_BUTTON_CLICKED = "de.fhdw.app_entwicklung.chatgpt.ACTION_BUTTON_CLICKED";

    private PrefsFacade prefs;
    Chat chat;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        prefs = new PrefsFacade(context);
        textToSpeech = new TextToSpeechTool(context, Locale.GERMAN);
        chat = new Chat();

        for(int appWidgetId: appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(ACTION_BUTTON_CLICKED);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        views.setOnClickPendingIntent(R.id.app_widget_button, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("WIDGET", intent.getAction());

        if(intent.getAction().equals(ACTION_BUTTON_CLICKED)){
            Log.i("WIDGET", "button clicked");
            if(textToSpeech == null) {
                this.textToSpeech = new TextToSpeechTool(context, Locale.GERMAN);
                Toast.makeText(context, "TextToSpeech has not been initialized yet. Try again." , Toast.LENGTH_SHORT).show();
            }
            if(chat == null){
                chat = new Chat();
            }
            if(prefs == null){
                prefs = new PrefsFacade(context);
            }

            Message userMessage = new Message(Author.User, "Sag mir einen zufälligen Fakt");
            chat.addMessage(userMessage);

            MainActivity.backgroundExecutorService.execute(() -> {
                String apiToken = prefs.getApiToken();
                ChatGpt chatGpt = new ChatGpt(apiToken);
                String answer = chatGpt.getChatCompletion(chat);

                Message answerMessage = new Message(Author.Assistant, answer);
                chat.addMessage(answerMessage);
                textToSpeech.speak(answer);
            });
        }


        if(intent.getAction().equals("android.appwidget.action.APPWIDGET_ENABLED")){
            Log.i("WIDGET", "enabled");

        }
    }
}
