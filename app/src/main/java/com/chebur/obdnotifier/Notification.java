package com.chebur.obdnotifier;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.chebur.obdnotifier.helpers.Helper;

public class Notification{
    private final Context context;

    public Notification( final Context context) {
        this.context = context;
    }

    public void showLongToast(@StringRes final int resId){
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public void showLongToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showAndSpeak(TextToSpeech tts, @StringRes final int resId){
        String text = Helper.resourceToString(context, resId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

        showLongToast(resId);
    }
}
