package com.chebur.obdnotifier.settings;

public interface ISettingsReader {
    int getApplicationStartDelayMillis();
    String getPackageNameToStart();
    String getTextToSpeak();
    int getSilentNotificationMinutes();
    boolean isStartAppAllowed();
}
