package de.fhdw.app_entwicklung.chatgpt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

public class ThemeHelper {
    public static void checkAndApplyTheme(SharedPreferences sharedPreferences, Context context) {
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("themechange", false);

        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

        int currentTheme = isNightMode ? R.style.DarkTheme : R.style.LightTheme;

        if (isDarkModeEnabled && !isNightMode) {
            ((Activity) context).setTheme(R.style.DarkTheme);
        } else if (!isDarkModeEnabled && isNightMode) {
            ((Activity) context).setTheme(R.style.LightTheme);
        }

        int backgroundColor = isNightMode ? R.color.colorBackgroundDark : R.color.colorBackgroundLight;
        ((Activity) context).getWindow().setBackgroundDrawableResource(backgroundColor);

        int appliedTheme = isNightMode ? R.style.DarkTheme : R.style.LightTheme;

        if (appliedTheme != currentTheme) {
            ((Activity) context).recreate();
        }
    }
}
