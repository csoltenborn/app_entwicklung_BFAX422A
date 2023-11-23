package de.fhdw.app_entwicklung.chatgpt.speech;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.EditText;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class LaunchTextfield extends ActivityResultContract<LaunchTextfield.TextfieldArgs, String> {


    public static class TextfieldArgs
    {
        public final Locale locale;
        public final String prompt;

        public TextfieldArgs(Locale locale)
        {
            this(locale, null);
        }

        public TextfieldArgs(Locale locale, String prompt)
        {
            this.locale = locale;
            this.prompt = prompt;
        }
    }
    @NonNull
    @Override// hier ist noch ein Problem
    public Intent createIntent(@NonNull Context context, TextfieldArgs textfieldArgs) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                textfieldArgs.locale.toString());

        if (textfieldArgs.prompt != null) {
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, textfieldArgs.prompt);
        }
        return intent;
    }

    @Override //hier ist noch ein problem
    public String parseResult(int i, @Nullable Intent intent) {
        if (i == RESULT_OK && intent != null) {
            ArrayList<String> result = intent.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            return Objects.requireNonNull(result).get(0);
        }
        return null;
    }
// Die Klasse ist Bewusst erhalten geblieben zur Demonstrations  der verschiedenen Lösungsansätze
}
