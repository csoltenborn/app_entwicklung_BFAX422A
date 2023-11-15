package de.fhdw.app_entwicklung.chatgpt.speech;

import android.speech.tts.TextToSpeech;
import android.content.Context;

public class LauchTextToSpeech implements TextToSpeech.OnInitListener{
    private TextToSpeech ttp;
    public LauchTextToSpeech(Context context){
        ttp = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status){

    }

    public void Speak(String text){
        ttp.speak(text, 0, null ,null);
    }


}
