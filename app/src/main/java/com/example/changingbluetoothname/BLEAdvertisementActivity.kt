package com.example.changingbluetoothname

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast

import android.os.ParcelUuid
import com.example.changingbluetoothname.Constants.SERVICE_UUID
import android.util.Log
import java.nio.charset.Charset


class BLEAdvertisementActivity : AppCompatActivity() {

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    val bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_advertisement)

        if( !bluetoothAdapter.isMultipleAdvertisementSupported) {
            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_SHORT ).show();
        }


        // Some advertising settings. We don't set an advertising timeout
       // since our device is always connected to AC power
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setConnectable(true)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
            .build()

        val parcelUuid = ParcelUuid(SERVICE_UUID)
        val messageToSend: ByteArray = "Data".toByteArray()
        "Data".toByteArray().forEach {
            Log.e(TAG, "each byte: $it")
        }

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceData(parcelUuid,messageToSend)
            .build()

        val advertisingCallback: AdvertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.e(TAG, "LE Advertise Started.")
                super.onStartSuccess(settingsInEffect)
                Toast.makeText(this@BLEAdvertisementActivity,"LE Advertise Started", Toast.LENGTH_SHORT).show()


            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                var description = ""
                if (errorCode == ADVERTISE_FAILED_FEATURE_UNSUPPORTED) {
                    description = "ADVERTISE_FAILED_FEATURE_UNSUPPORTED";
                } else if (errorCode == ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) {
                    description = "ADVERTISE_FAILED_TOO_MANY_ADVERTISERS";
                } else if (errorCode == ADVERTISE_FAILED_ALREADY_STARTED) {
                    description = "ADVERTISE_FAILED_ALREADY_STARTED";
                } else if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
                    description = "ADVERTISE_FAILED_DATA_TOO_LARGE";
                } else if (errorCode == ADVERTISE_FAILED_INTERNAL_ERROR) {
                    description = "ADVERTISE_FAILED_INTERNAL_ERROR";
                } else {
                    description = "unknown";
                }
                Log.e(TAG, "Advertising onStartFailure: $errorCode and $description")

            }
        }
        bluetoothLeAdvertiser.startAdvertising(settings, data, advertisingCallback)
    }
    companion object {
        const val TAG = "BLEAdvertisementActivi"
    }
}