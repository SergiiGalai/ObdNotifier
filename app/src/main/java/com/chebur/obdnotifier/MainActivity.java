package com.chebur.obdnotifier;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    private TextToSpeech tts;
    private BluetoothManager bluetoothManager;
    private Notification notification;
    private ApplicationLauncher launcher;
    private Button cancelButton;
    private ApplicationLauncher.DelayedStartApplication delayedApplicationStart;

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

    private void initializeVariables() {
        cancelButton = findViewById(R.id.cancelButton);

        launcher = new ApplicationLauncher(this);
        notification = new Notification(this);
        bluetoothManager = new BluetoothManager(this);
        tts = new TextToSpeech(this, createInitListener());
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
                    Log.e(TAG, "Initilization Failed!");
            }
        };
    }

    private void onTextToSpeechInitialized(final Activity context) {
        //Map<String, BluetoothDevice> devices = bluetoothManager.getPairedBluetoothDevices();
        //showBluetoothDevices(devices);
        //tryConnectToObdDevice(R.string.obd_bt_device_name, devices);

        //List<String> appProcesses = launcher.getRunningAppProcesses();
        //showProcesses(appProcesses);

        notification.showLongToast(R.string.turnon_obd_device_title);
        notification.showAndSpeak(tts, R.string.turnon_obd_device_title);

        if (launcher.isAppRunning(Helper.resourceToString(context, R.string.appid_to_run))){
            notification.showLongToast(R.string.app_running);
        }else{
            delayedApplicationStart = launcher.delayedStart(R.string.appid_to_run, R.integer.run_app_delay_millis);
        }
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
}
