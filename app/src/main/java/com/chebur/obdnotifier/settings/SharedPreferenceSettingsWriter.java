package com.chebur.obdnotifier.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceSettingsWriter implements ISettingsWriter {
    private final SharedPreferences preferences;

    public SharedPreferenceSettingsWriter(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void saveLastTimeNotified(long value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("last_time_notified", value);
        editor.commit();
    }
}

