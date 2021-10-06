package com.example.changingbluetoothname

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.bluetooth.le.BluetoothLeAdvertiser
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.AdvertiseData

import android.os.ParcelUuid
import com.example.changingbluetoothname.Constants.SERVICE_UUID
import android.bluetooth.le.AdvertiseCallback
import android.util.Log


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

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceUuid(parcelUuid)
            .build()

        val advertisingCallback: AdvertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.e(TAG, "LE Advertise Started.")
                super.onStartSuccess(settingsInEffect)
            }

            override fun onStartFailure(errorCode: Int) {
                Log.e("BLE", "Advertising onStartFailure: $errorCode")
                super.onStartFailure(errorCode)
            }
        }
        bluetoothLeAdvertiser.startAdvertising(settings, data, advertisingCallback)
    }

    companion object {
        const val TAG = "BLEAdvertisementActivi"
    }
}