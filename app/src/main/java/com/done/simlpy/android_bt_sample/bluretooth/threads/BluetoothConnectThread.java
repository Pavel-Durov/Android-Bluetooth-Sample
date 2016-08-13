package com.done.simlpy.android_bt_sample.bluretooth.threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.done.simlpy.android_bt_sample.bluretooth.BluetoothService;
import com.done.simlpy.android_bt_sample.bluretooth.interfaces.BluetoothSocketCallback;

import java.io.IOException;

public class BluetoothConnectThread extends Thread {

    private BluetoothSocket _socket;
    private final BluetoothDevice _device;
    private final BluetoothAdapter _adapter;
    private final BluetoothSocketCallback _listener;

    public BluetoothConnectThread(BluetoothAdapter adapter, BluetoothDevice device, BluetoothSocketCallback listener) {

        _listener = listener;
        _adapter = adapter;
        _device = device;
    }

    private boolean InitializeSocket() {
        boolean result = true;
        try {
            _socket = _device.createRfcommSocketToServiceRecord(BluetoothService.BLUETOOTH_UDID);
        } catch (IOException e) {
            result = false;
            Log.e(this.getClass().getName(), e.getMessage());
        } finally {
            return result;
        }
    }

    public void run() {
        if (InitializeSocket()) {
            _adapter.cancelDiscovery();

            try {
                _socket.connect();
            } catch (IOException connEx) {
                Log.e(this.getClass().getName(), connEx.getMessage());
                try {
                    _socket.close();
                } catch (IOException closeException) {
                    Log.e(this.getClass().getName(), connEx.getMessage());
                }
            }

            if (_socket != null && _socket.isConnected()) {
                _listener.Connected(_socket, _device);
            }
        }
    }

    public void cancel() {
        try {
            _socket.close();
        } catch (IOException connEx) {
            Log.e(this.getClass().getName(), connEx.getMessage());
        }
    }
}
