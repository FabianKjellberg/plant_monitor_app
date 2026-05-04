package com.example.learning_android.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel

class AddDeviceViewModel(application: Application) : AndroidViewModel(application) {

  private val context = application.applicationContext
  private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
  private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

  val foundDevices = mutableStateListOf<BluetoothDevice>()

  private val receiver = object : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
      when (intent.action) {
        BluetoothDevice.ACTION_FOUND -> {
          val device: BluetoothDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
          } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
          }

          device?.let {
            if (!foundDevices.any { existing -> existing.address == it.address }) {
              foundDevices.add(it)
            }
          }
        }
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun startDiscovery() {
    if (bluetoothAdapter == null) return

    foundDevices.clear()

    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    context.registerReceiver(receiver, filter)

    if (bluetoothAdapter.isDiscovering) {
      bluetoothAdapter.cancelDiscovery()
    }
    bluetoothAdapter.startDiscovery()
  }

  override fun onCleared() {
    super.onCleared()
    try {
      context.unregisterReceiver(receiver)
    } catch (e: Exception) {

    }
  }
}