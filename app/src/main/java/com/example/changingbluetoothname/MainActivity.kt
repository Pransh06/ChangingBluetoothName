package com.example.changingbluetoothname

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("MainActivity", "localdevicename Before : "+bluetoothAdapter.getName()+" localdeviceAddress : "+bluetoothAdapter.getAddress());

        submitBtn.setOnClickListener {
            if (checkInput() && bluetoothAdapter!=null){
                bluetoothAdapter.setName(enterName1.text.toString())
                Log.e("MainActivity", "localdevicename After : "+bluetoothAdapter.getName()+" localdeviceAddress : "+bluetoothAdapter.getAddress());
            }
        }


    }

    private fun checkInput():Boolean{

        if (enterName1.text.isNullOrBlank()){
            Toast.makeText(this@MainActivity,"please enter bluetooth name!",Toast.LENGTH_SHORT).show()
            return false
        }
        else if (bluetoothAdapter.state==BluetoothAdapter.STATE_OFF){
            Toast.makeText(this@MainActivity,"please turn on bluetooth!",Toast.LENGTH_SHORT).show()
            return false
        }
        else if (!bluetoothAdapter.isEnabled){
            Toast.makeText(this@MainActivity,"bluetooth is not enabled!",Toast.LENGTH_SHORT).show()
            return false
        }
        else if (bluetoothAdapter.state == BluetoothAdapter.STATE_CONNECTING){
            Toast.makeText(this@MainActivity,"bluetooth is connecting..",Toast.LENGTH_SHORT).show()
        }
        return true
    }
}