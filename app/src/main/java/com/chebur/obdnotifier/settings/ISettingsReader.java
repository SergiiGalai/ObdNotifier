package com.chebur.obdnotifier.settings;

public interface ISettingsReader {
    int getApplicationStartDelaySeconds();
    String getPackageNameToStart();
    String getTextToSpeak();
    int getSilentNotificationMinutes();
    boolean isStartAppAllowed();
}
