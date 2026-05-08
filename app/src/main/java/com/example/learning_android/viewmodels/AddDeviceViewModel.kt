package com.example.learning_android.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.domain.model.AddDeviceState
import com.example.learning_android.domain.model.EspWifiConnectionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import android.Manifest
import androidx.lifecycle.SavedStateHandle
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.dto.AddDeviceToHomeDto
import com.example.learning_android.data.remote.dto.CreateUserDeviceDto


class AddDeviceViewModel(
  application: Application,
  private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

  private val homeId: String = checkNotNull(savedStateHandle["homeId"])

  companion object {
    private const val SERVICE_UUID_STRING = "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
    private const val WIFI_CHAR_UUID_STRING = "beb5483e-36e1-4688-b7f5-ea07361b26a8"
    private const val STATUS_CHAR_UUID_STRING = "e3237190-2c7b-449e-862d-6060c410313a"
    private const val MAC_CHAR_UUID_STRING = "d2ed6f67-a068-4501-8b06-44e21ed90382"

    val SERVICE_UUID: UUID = UUID.fromString(SERVICE_UUID_STRING)
    val WIFI_CHAR_UUID: UUID = UUID.fromString(WIFI_CHAR_UUID_STRING)
    val STATUS_CHAR_UUID: UUID = UUID.fromString(STATUS_CHAR_UUID_STRING)
    val MAC_CHAR_UUID: UUID = UUID.fromString(MAC_CHAR_UUID_STRING)
  }

  var uiState by mutableStateOf(AddDeviceState.SCANNING)
    private set

  fun updateUiState (state: AddDeviceState) {
    uiState = state;
  }

  private val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
  private val adapter = bluetoothManager.adapter
  private val bleScanner = adapter.bluetoothLeScanner
  private var bluetoothGatt: BluetoothGatt? = null

  val foundDevice = mutableStateOf<BluetoothDevice?>(null)
  var macAddr = mutableStateOf<String?>(null);

  var userInputSsid = mutableStateOf("")
  var userInputPassword = mutableStateOf("")
  var deviceProvisioned = mutableStateOf(false);
  var wifiConnectionStatus = mutableStateOf<EspWifiConnectionStatus?>(null)

  var userInputName = mutableStateOf("")
  var namingErrorText = mutableStateOf("")
  var namingLoading = mutableStateOf(false)

  var userInputPlaceId = mutableStateOf<String?>(null)



  private val scanCallback = object : ScanCallback() {
    @SuppressLint("MissingPermission")
    override fun onScanResult(callbackType: Int, result: ScanResult) {
      val device = result.device

      foundDevice.value = device

      stopScanning()

      bluetoothGatt = device.connectGatt(getApplication(), false, gattCallback)
    }
    override fun onScanFailed(errorCode: Int) {
      Log.e("BLE", "scan failed with error code: ${errorCode}")
    }
  }

  @SuppressLint("MissingPermission")
  fun startScanning() {
    if (bleScanner == null) return

    foundDevice.value = null;

    val settings = ScanSettings.Builder()
      .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
      .build()

    val filter = ScanFilter.Builder()
      .setServiceUuid(ParcelUuid(SERVICE_UUID))
      .build()

    bleScanner.startScan(listOf(filter), settings, scanCallback)
  }

  @SuppressLint("MissingPermission")
  fun stopScanning() {
    bleScanner.stopScan(scanCallback)
  }

  private val gattCallback = object : BluetoothGattCallback() {
    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
      if (newState == BluetoothProfile.STATE_CONNECTED) {
        Log.d("BLE", "Connected! Discovering services...")
        gatt.discoverServices()
      } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
        if(!deviceProvisioned.value) {
          uiState = AddDeviceState.SCANNING
        }
      }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
      if (status == BluetoothGatt.GATT_SUCCESS) {
        val service = gatt.getService(SERVICE_UUID)
        val statusChar = service?.getCharacteristic(STATUS_CHAR_UUID)
        val macChar = service?.getCharacteristic(MAC_CHAR_UUID)

        if (statusChar != null) {
          gatt.setCharacteristicNotification(statusChar, true)

          val descriptor = statusChar.getDescriptor(
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
          )
          gatt.writeDescriptor(
            descriptor,
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
          )
        }

        if (macChar != null) {
          gatt.readCharacteristic(macChar)
        }

        uiState = AddDeviceState.DEVICE_FOUND
      }
    }

    @SuppressLint("MissingPermission")
    override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
      if (status == BluetoothGatt.GATT_SUCCESS) {
        Log.d("BLE", "Descriptor written! Safe to read MAC now.")
        val service = gatt.getService(SERVICE_UUID)
        val macChar = service?.getCharacteristic(MAC_CHAR_UUID)
        if (macChar != null) {
          gatt.readCharacteristic(macChar)
        }
      }
    }

    @SuppressLint("MissingPermission")
    override fun onCharacteristicRead(
      gatt: BluetoothGatt,
      characteristic: BluetoothGattCharacteristic,
      status: Int
    ) {
      if (status == BluetoothGatt.GATT_SUCCESS && characteristic.uuid == MAC_CHAR_UUID) {
        // Read the value safely based on API level
        val macBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          characteristic.value // characteristic.value is fine here in the callback
        } else {
          @Suppress("DEPRECATION")
          characteristic.value
        }

        val macString = macBytes?.let { String(it) } ?: "Unknown"

        Log.d("BLE", "MAC Address received: $macString")

        viewModelScope.launch(Dispatchers.Main) {
          macAddr.value = macString
        }
      }
    }

    @SuppressLint(value = ["deprecated"])
    override fun onCharacteristicChanged(
      gatt: BluetoothGatt,
      characteristic: BluetoothGattCharacteristic
    ) {
      if (characteristic.uuid == STATUS_CHAR_UUID) {
        val statusString = characteristic.getStringValue(0) ?: ""

        //provisioning finished
        if(statusString == "1") {
          deviceProvisioned.value = true;
          uiState = AddDeviceState.NAMING
        }

        viewModelScope.launch(Dispatchers.Main) {
          wifiConnectionStatus.value =
            EspWifiConnectionStatus.fromCode(statusString)
        }
      }
    }

    @SuppressLint("MissingPermission")
    override fun onCharacteristicRead(
      gatt: BluetoothGatt,
      characteristic: BluetoothGattCharacteristic,
      value: ByteArray,
      status: Int
    ) {
      if (status == BluetoothGatt.GATT_SUCCESS && characteristic.uuid == MAC_CHAR_UUID) {
        val mac = String(value)
        Log.d("BLE", "MAC Address received (Modern): $mac")
        viewModelScope.launch(Dispatchers.Main) {
          macAddr.value = mac
        }
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun sendWifiCredentials() {
    val service = bluetoothGatt?.getService(SERVICE_UUID)
    val characteristic = service?.getCharacteristic(WIFI_CHAR_UUID)

    if (characteristic == null) {
      Log.e("BLE", "Wi-Fi characteristic not found!")
      return
    }

    val data = "${userInputSsid.value}|${userInputPassword.value}".toByteArray()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      bluetoothGatt?.writeCharacteristic(
        characteristic,
        data,
        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
      )
    } else {
      @Suppress("DEPRECATION")
      characteristic.value = data
      bluetoothGatt?.writeCharacteristic(characteristic)
    }
  }

  fun createUserDevice(onSuccess: () -> Unit) {
    namingLoading.value = true

    viewModelScope.launch {
      try {
        val mac = macAddr.value
        val name = userInputName.value.trim()
        val placeId = userInputPlaceId.value

        if(mac == null) return@launch

        val res = ApiClient.deviceApiService.addDeviceToHome(AddDeviceToHomeDto(mac, name, homeId, placeId ))

        if (res.isSuccessful){
          onSuccess()
        }
        else {
          Log.e("API_TEST", "create user device failed: ${res.message()}")
        }
      }
      catch (e: Error) {
        Log.e("API_TEST", "Error: $e")
      }
      finally {
        namingLoading.value = false
      }
    }
  }

  fun hasBluetoothPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
    } else {
      context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
  }

  fun isBluetoothEnabled(): Boolean {
    return adapter?.isEnabled == true
  }

  override fun onCleared() {
    super.onCleared()
    stopScanning()
  }
}