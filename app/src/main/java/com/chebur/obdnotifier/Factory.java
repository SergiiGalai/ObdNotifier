package com.chebur.obdnotifier;

import android.app.Activity;

import com.chebur.obdnotifier.settings.IRunApplicationRepository;
import com.chebur.obdnotifier.settings.ISettingsReader;
import com.chebur.obdnotifier.settings.ISettingsWriter;
import com.chebur.obdnotifier.settings.SharedPreferenceRunApplicationRepository;
import com.chebur.obdnotifier.settings.SharedPreferenceSettingsReader;
import com.chebur.obdnotifier.settings.SharedPreferenceSettingsWriter;


class Factory {

    static IRunApplicationRepository createRunApplicationRepository(Activity activity){
        return new SharedPreferenceRunApplicationRepository(activity);
    }

    static ISettingsReader createSettingsReader(Activity activity){
        return new SharedPreferenceSettingsReader(activity);
    }

    public static ISettingsWriter createSettingsWriter(Activity activity){
        return new SharedPreferenceSettingsWriter(activity);
    }
}
