package com.chebur.obdnotifier.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceSettingsReader implements ISettingsReader
{
    private final SharedPreferences preferences;

    public SharedPreferenceSettingsReader(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public long getLastTimeNotified(){
        return preferences.getLong("last_time_notified", 0);
    }
}
