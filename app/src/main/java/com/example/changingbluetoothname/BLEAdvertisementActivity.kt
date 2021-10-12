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
import kotlinx.android.synthetic.main.activity_ble_advertisement.*
import java.lang.Exception
import java.util.*


class BLEAdvertisementActivity : AppCompatActivity() {

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    val bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser

    var currentAdvertisingSet: AdvertisingSet? = null

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_advertisement)

        submitBtn.setOnClickListener {
            startAdvertisemet()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startAdvertisemet(){
        if( !bluetoothAdapter.isMultipleAdvertisementSupported) {
            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_SHORT ).show()
            return
        }
        bluetoothLeAdvertiser.stopAdvertisingSet(callback)
        val newBluetoothName :Boolean =  bluetoothAdapter.setName("${enterName1.text}")
        if (newBluetoothName) {
            Toast.makeText(this,"bluetooth name changed!",Toast.LENGTH_SHORT).show()
            Log.e(TAG, "localdevicename After : "+bluetoothAdapter.getName()+" localdeviceAddress : "+bluetoothAdapter.getAddress());
        }

        val settings = AdvertisingSetParameters.Builder()
            .setLegacyMode(true) // True by default, but set here as a reminder.
            .setConnectable(false)
            .setInterval(AdvertisingSetParameters.INTERVAL_MIN)
            .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MAX)
            .build()

        val parcelUuid = ParcelUuid(SERVICE_UUID)
        val messageToSend: ByteArray = "Vogo".toByteArray()
        "Vogo".toByteArray().forEach {
            Log.e(TAG, "each byte: $it")
        }

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceData(parcelUuid,messageToSend)
            .build()

        try {
            bluetoothLeAdvertiser.startAdvertisingSet(settings,data,null,null,null,callback)
        } catch (e:IllegalArgumentException){
            Log.e(TAG, "exception $e: " )
        } catch (e:Exception){
            Log.e(TAG, "exception $e: " )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        bluetoothLeAdvertiser.stopAdvertisingSet(callback)
    }


    companion object {
        const val TAG = "BLEAdvertisementActivi"
    }
}