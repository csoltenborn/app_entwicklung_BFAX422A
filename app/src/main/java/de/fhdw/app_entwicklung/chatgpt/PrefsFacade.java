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

    public boolean isDarkModeEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("themechange", false);
    }

    public void setDarkModeEnabled(boolean isEnabled) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("themechange", isEnabled).apply();
    }

}