package de.fhdw.app_entwicklung.chatgpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

public class PrefsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        ThemeHelper.checkAndApplyTheme(sharedPreferences, this);

        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SharedPreferences sharedPreferences;
        private SwitchPreferenceCompat colorSwitch;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            colorSwitch = findPreference("themechange");

            colorSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isChecked = (boolean) newValue;

                    if (isChecked) {
                        enableColorChange();
                    } else {
                        disableColorChange();
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    }, 1000);

                    ThemeHelper.checkAndApplyTheme(sharedPreferences, requireContext());

                    return true;
                }

                private void enableColorChange() {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("themechange", true).apply();
                }

                private void disableColorChange() {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("themechange", false).apply();
                }
            });
        }
    }
}