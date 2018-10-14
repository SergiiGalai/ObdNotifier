package com.chebur.obdnotifier.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceSettingsWriter implements ISettingsWriter {
    private final SharedPreferences preferences;

    public SharedPreferenceSettingsWriter(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


}

