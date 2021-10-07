package com.example.changingbluetoothname

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_discovering_bleadvertisement.*
import java.nio.charset.Charset
import android.bluetooth.le.ScanSettings
import android.widget.Toast

class DiscoveringBLEAdvertisementActivity : AppCompatActivity() {

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val bluetoothLeScanner: BluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner        //is used for scanning for Bluetooth LE packets

    private val handler: Handler = Handler()    //mHandler controls a small timer that stops discovery after a set period of time.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discovering_bleadvertisement)

        //create a ScanFilter object and place it into a List so that your application only responds to the advertising packets that you are interested in.
        val filter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(Constants.SERVICE_UUID))
            .build()

        val filters =  ArrayList<ScanFilter>()
        filters.add(filter)

        //You also need to create a ScanSettings object, which works similarly to the AdvertiseSettings
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        //Once you have your filters, settings, and callbacks in place, you can begin discovering Bluetooth LE advertisements.
        bluetoothLeScanner.startScan(filters, settings, scanCallback)

        handler.postDelayed(Runnable {
            bluetoothLeScanner.stopScan(scanCallback) }, 10000)
    }

    val scanCallback: ScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.e(TAG, "onScanResult: $result", )
            if (result?.getDevice() == null || TextUtils.isEmpty(result.getDevice().getName()))
                return
            Toast.makeText(this@DiscoveringBLEAdvertisementActivity,"$result", Toast.LENGTH_SHORT).show()

            val builder: StringBuilder = StringBuilder(result.getDevice().getName())
            builder.append("\n").append(result.getScanRecord()?.getServiceData(result.getScanRecord()?.getServiceUuids()?.get(0))?.let {
                String(it, Charset.forName("UTF-8"))
            })

            textViewAdvertisement.setText(builder.toString())
        }

        override fun onBatchScanResults(results: List<ScanResult?>?) {
            super.onBatchScanResults(results)
            results?.forEach {
                Log.e(TAG, "onBatchScanResults: ${it?.scanRecord}")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(TAG, "Discovery onScanFailed: $errorCode")
            super.onScanFailed(errorCode)
        }
    }


    companion object {
        const val TAG = "DiscoveringBLEAdvertise"
    }
}