package com.done.simlpy.android_bt_sample.bluretooth.threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.done.simlpy.android_bt_sample.bluretooth.BluetoothService;
import com.done.simlpy.android_bt_sample.bluretooth.interfaces.BluetoothSocketCallback;

import java.io.IOException;

public class BluetoothServerThread extends Thread {

    private static final String SERVER_NAME = "WICKED_BLUE_TOOTH";

    private BluetoothServerSocket _serverSocket;
    private final BluetoothAdapter _adapter;
    private BluetoothSocketCallback _callback;

    public BluetoothServerThread(BluetoothAdapter adapter, BluetoothSocketCallback callback) {
        _callback = callback;
        _adapter = adapter;
    }

    boolean InitializeSocket() {
        boolean result = true;
        try {
            _serverSocket = _adapter.listenUsingRfcommWithServiceRecord(SERVER_NAME, BluetoothService.BLUETOOTH_UDID);
        } catch (IOException e) {
            result = false;
            Log.e(this.getClass().getName(), e.getMessage());
        } finally {
            return result;
        }
    }

    public void run() {
        if (InitializeSocket()) {
            BluetoothSocket socket = null;

            while (true) {
                try {
                    socket = _serverSocket.accept();
                } catch (IOException e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                    break;
                }

                if (socket != null && socket.isConnected()) {
                    _callback.Connected(socket, socket.getRemoteDevice());
                }
            }
        }
    }

    public void cancel() {
        try {
            _serverSocket.close();
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }
}

