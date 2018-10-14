package com.chebur.obdnotifier.settings;

public interface IRunApplicationRepository {
    long getLastTimeNotified();
    void saveLastTimeNotified(long value);
}
