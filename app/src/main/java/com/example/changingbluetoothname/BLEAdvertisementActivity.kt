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
import android.bluetooth.le.AdvertisingSetParameters
import android.os.Build
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertisingSet

import android.bluetooth.le.AdvertisingSetCallback
import androidx.annotation.RequiresApi
import java.lang.Exception


class BLEAdvertisementActivity : AppCompatActivity() {

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    val bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser

    var currentAdvertisingSet: AdvertisingSet? = null

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_advertisement)

        if( !bluetoothAdapter.isMultipleAdvertisementSupported) {
            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_SHORT ).show()
            return
        }
        var b:Boolean =  bluetoothAdapter.setName("VOGO")
        if (b){
            Toast.makeText(this,"bluetooth name changed!",Toast.LENGTH_SHORT).show()
            Log.e(TAG, "localdevicename After : "+bluetoothAdapter.getName()+" localdeviceAddress : "+bluetoothAdapter.getAddress());

        }


        val settings = AdvertisingSetParameters.Builder()
                .setLegacyMode(true) // True by default, but set here as a reminder.
                .setConnectable(false)
                .setInterval(AdvertisingSetParameters.INTERVAL_MIN)
                .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MAX)
                .build()

            /*// Some advertising settings. We don't set an advertising timeout
            // since our device is always connected to AC power
             AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
                .build()*/



        val parcelUuid = ParcelUuid(SERVICE_UUID)
        val messageToSend: ByteArray = "Vogo".toByteArray()
        "Vogo".toByteArray().forEach {
            Log.e(TAG, "each byte: $it")
        }

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceData(parcelUuid,messageToSend)
            .build()

        /*val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .build()
*/
       /* val advertisingCallback: AdvertiseCallback = object : AdvertiseCallback() {
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
        }*/

        try {
            bluetoothLeAdvertiser.startAdvertisingSet(settings,data,null,null,null,callback)
        }catch (e:IllegalArgumentException){
            Log.e(TAG, "exception $e: " )
        }
        catch (e:Exception){
            Log.e(TAG, "exception $e: " )
        }

        // When done with the advertising:
        //bluetoothLeAdvertiser.stopAdvertisingSet(callback);
    }

    val callback: AdvertisingSetCallback = @RequiresApi(Build.VERSION_CODES.O)
    object : AdvertisingSetCallback() {
        override fun onAdvertisingSetStarted(advertisingSet: AdvertisingSet, txPower: Int,
                                             status: Int){
            Log.e(TAG, "onAdvertisingSetStarted(): txPower:" + txPower + " , status: " + status)
            currentAdvertisingSet = advertisingSet
        }

        override fun onAdvertisingDataSet(advertisingSet: AdvertisingSet, status: Int) {
            Log.e(TAG, "onAdvertisingDataSet() :status:$status  $advertisingSet")
        }

        override fun onScanResponseDataSet(advertisingSet: AdvertisingSet, status: Int) {
            Log.e(TAG, "onScanResponseDataSet(): status:$status")
        }

        override fun onAdvertisingSetStopped(advertisingSet: AdvertisingSet) {
            Log.e(TAG, "onAdvertisingSetStopped():")
        }
    }
    companion object {
        const val TAG = "BLEAdvertisementActivi"
    }
}