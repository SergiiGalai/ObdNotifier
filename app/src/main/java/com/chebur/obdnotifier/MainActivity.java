package com.chebur.obdnotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.chebur.obdnotifier.helpers.TimeHelper;
import com.chebur.obdnotifier.settings.IRunApplicationRepository;
import com.chebur.obdnotifier.settings.ISettingsReader;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    private TextToSpeech tts;
    private BluetoothManager bluetoothManager;
    private Notification notification;
    private ApplicationLauncher launcher;
    private Button cancelButton;
    private ApplicationLauncher.DelayedStartApplication delayedApplicationStart;
    private IRunApplicationRepository runApplicationRepository;
    private ISettingsReader settingsReader;

    private void initializeVariables() {
        cancelButton = findViewById(R.id.cancelButton);

        launcher = new ApplicationLauncher(this);
        notification = new Notification(this);
        bluetoothManager = new BluetoothManager(this);
        runApplicationRepository = Factory.createRunApplicationRepository(this);
        settingsReader = Factory.createSettingsReader(this);
        tts = new TextToSpeech(this, createInitListener());
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (delayedApplicationStart != null)
                    launcher.cancelStart(delayedApplicationStart);
                finish();
            }
        });
    }

    @NonNull
    private TextToSpeech.OnInitListener createInitListener(){
        final Activity context = this;
        return new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = tts.setLanguage(Locale.US);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e(TAG, "This Language is not supported");
                    }
                    else{
                        onTextToSpeechInitialized(context);
                    }
                }
                else
                    Log.e(TAG, "Initialization Failed!");
            }
        };
    }

    private void onTextToSpeechInitialized(final Activity context) {
        //Map<String, BluetoothDevice> devices = bluetoothManager.getPairedBluetoothDevices();
        //showBluetoothDevices(devices);
        //tryConnectToObdDevice(R.string.obd_bt_device_name, devices);

        //List<String> appProcesses = launcher.getRunningAppProcesses();
        //showProcesses(appProcesses);

        String textToSpeak = settingsReader.getTextToSpeak();
        notification.showLongToast(textToSpeak);

        if (userShouldBeNotified()){
            notification.speak(tts, textToSpeak);
            runApplicationRepository.saveLastTimeNotified(TimeHelper.now());

            String packageName = settingsReader.getPackageNameToStart();
            if (appShouldBeRun(packageName)){
                int delayMillis = settingsReader.getApplicationStartDelayMillis();
                delayedApplicationStart = launcher.delayedStart(packageName, delayMillis);
            }else{
                notification.showLongToast(R.string.app_running);
            }
        } else {
            context.finish();
        }
    }

    private boolean userShouldBeNotified(){
        return true;

        //return runApplicationRepository.getLastTimeNotified() == 0
        //        || getMinutesSinceLastNotification() >= settingsReader.getSilentNotificationMinutes();
    }

    private long getMinutesSinceLastNotification(){
        long lastTimeNotified = runApplicationRepository.getLastTimeNotified();
        long diff = TimeHelper.now() - lastTimeNotified;
        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }

    private boolean appShouldBeRun(String packageName){
        return !launcher.isAppRunning(packageName);
    }

//    private void showProcesses(List<String> appProcesses) {
//        String processes = TextUtils.join("\n- ", appProcesses);
//        bluetoothDevicesText.setText(processes);
//    }
//
//    private void showBluetoothDevices(Map<String, BluetoothDevice> devices) {
//        bluetoothStatusText.setText(
//                devices == null
//                        ? R.string.bluetooth_not_available_title
//                        : R.string.bluetooth_available_title
//        );
//
//        if (devices != null){
//            String formattedDevices = TextUtils.join("\n- ", devices.keySet());
//            bluetoothDevicesText.setText(formattedDevices);
//        }
//    }

//    private void tryConnectToObdDevice(@StringRes final int deviceNameRes, Map<String, BluetoothDevice> devices) {
//        String obdDeviceName = Helper.resourceToString(this, deviceNameRes);
//
//        if (devices.containsKey(obdDeviceName)){
//            notification.showLongToast("found target device. Connecting to " + obdDeviceName);
//
//            BluetoothDevice deviceToConnect = devices.get(obdDeviceName);
//            if (bluetoothManager.tryConnect(deviceToConnect)){
//                notification.showLongToast("Connected to device " + obdDeviceName);
//            }else{
//                notification.showLongToast("Cannot connect to device " + obdDeviceName);
//            }
//        }else{
//            notification.showLongToast(obdDeviceName + " devices was not found among bonded devices");
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
