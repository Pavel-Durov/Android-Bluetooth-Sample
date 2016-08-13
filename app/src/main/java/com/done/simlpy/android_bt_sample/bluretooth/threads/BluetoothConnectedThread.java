package com.done.simlpy.android_bt_sample.bluretooth.threads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.done.simlpy.android_bt_sample.bluretooth.interfaces.BluetoothSocketCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothConnectedThread extends Thread {

    private final BluetoothSocketCallback _listener;
    private final BluetoothSocket _socket;
    private InputStream _inStream;
    private OutputStream _outStream;

    public BluetoothConnectedThread(BluetoothSocket socket, BluetoothSocketCallback listener) {
        _listener = listener;
        _socket = socket;
        InitializeStreams();
    }

    private void InitializeStreams() {
        try {
            _inStream = _socket.getInputStream();
            _outStream =  _socket.getOutputStream();
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytesCount; // bytes returned from read()

        while (true) {
            try {
                bytesCount = _inStream.read(buffer);
                if(buffer != null && bytesCount > 0)
                {
                    _listener.DataRecieved(buffer, bytesCount);
                }
            } catch (IOException e) {
                Log.e(this.getClass().getName(), e.getMessage());
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            _outStream.write(bytes);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }

    public void cancel() {
        try {
            _socket.close();
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }
}