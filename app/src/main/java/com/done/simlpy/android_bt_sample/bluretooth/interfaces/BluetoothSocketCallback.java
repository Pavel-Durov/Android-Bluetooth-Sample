package com.done.simlpy.android_bt_sample.bluretooth.interfaces;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;


public interface BluetoothSocketCallback {
    void Connected(BluetoothSocket socket, BluetoothDevice device);
    void DataRecieved(byte[] buffer, int bytesCount);

}
