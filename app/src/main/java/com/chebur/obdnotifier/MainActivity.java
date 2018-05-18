package com.chebur.obdnotifier;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    private TextToSpeech tts;

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
        final Context context = this;

        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e(TAG, "This Language is not supported");
                    }
                    else{
                        remindUserTurnOnObdDevice(context);
                        delayedStartExternalApplication(context, R.string.appid_to_run, R.integer.run_app_delay_millis);
                    }
                }
                else
                    Log.e(TAG, "Initilization Failed!");
            }
        });

        //getPairedBluetoothDevices(context);
    }

    private void speakOut(final Context context, @StringRes final int sayResId) {
        tts.speak(context.getResources().getText(sayResId).toString(), TextToSpeech.QUEUE_FLUSH, null);
    }

    private void remindUserTurnOnObdDevice(final Context context){
        speakOut(context, R.string.turnon_obd_device_title);
        Toast.makeText(context, R.string.turnon_obd_device_title, Toast.LENGTH_LONG).show();
    }

    private void delayedStartExternalApplication(final Context context, @StringRes final int appResId, @IntegerRes final int delayResId){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startNewActivity(context, context.getResources().getText(appResId).toString());
                finish();
            }
        }, context.getResources().getInteger(delayResId));
    }

    private void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static final int REQUEST_ENABLE_BT = 224;
    private void getPairedBluetoothDevices(Context context){
        Log.d(TAG, "get paired bt devices start");

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            Toast.makeText(context, "Device doesn't support Bluetooth", Toast.LENGTH_LONG);
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "BT is not enabled");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Log.d(TAG, "BT is enabled, getting bonded devices");
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            Log.i(TAG, "found the following devices:");
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i(TAG, deviceName);
            }
        }else{
            Log.i(TAG, "No bonded devices");
        }
    }
}
