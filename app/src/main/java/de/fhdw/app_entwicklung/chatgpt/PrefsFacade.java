package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

public class PrefsFacade {

    private final Context context;

    public PrefsFacade(@NonNull Context context) {
        this.context = context;
    }

    public String getApiToken() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("api_token", "");
    }

    public String getCurrentSysMessage() {
        boolean settingsExist = PreferenceManager.getDefaultSharedPreferences(context).contains("sys_message_dropdown");
        if (!settingsExist) {
            throw new RuntimeException();
        }
        String dropdown = PreferenceManager.getDefaultSharedPreferences(context).getString("sys_message_dropdown", "Bitte antworte auf jede Frage mit 42.");
        if (dropdown.equals("Custom Value"))
        {
            return PreferenceManager.getDefaultSharedPreferences(context).getString("custom_sys_message", "Bitte antworte auf jede Frage mit 42.");
        }
        return dropdown;

    }

}