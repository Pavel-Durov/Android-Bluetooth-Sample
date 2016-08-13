package com.done.simlpy.android_bt_sample.bluretooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;

import com.done.simlpy.android_bt_sample.bluretooth.interfaces.BluetoothSocketCallback;
import com.done.simlpy.android_bt_sample.bluretooth.threads.BluetoothConnectThread;
import com.done.simlpy.android_bt_sample.bluretooth.threads.BluetoothConnectedThread;
import com.done.simlpy.android_bt_sample.bluretooth.threads.BluetoothServerThread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothService implements BluetoothSocketCallback {

    public static final UUID BLUETOOTH_UDID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int DISCOVERABLE_DURATION = 5000;

    BluetoothServerThread _bluetoothServer;
    BluetoothConnectThread _bluetoothConnectThread;
    BluetoothConnectedThread _connectedThread;

    BluetoothAdapter _bluetoothAdapter;
    ArrayList<BluetoothDevice> _previouslyDescoveredDevices;
    Activity _context;

    public BluetoothService(Activity context){
        _context = context;
    }

    public void Init(){
        _bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        if (_bluetoothAdapter != null) {
            EnableBluetooth();
        }
    }

    public void EnableBluetooth(){
        if (!_bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            _context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void Connect(BluetoothDevice device){
        _bluetoothConnectThread = new BluetoothConnectThread(_bluetoothAdapter, device, this);
        _bluetoothConnectThread.start();
    }

    public ArrayList<BluetoothDevice> ScanForDevices(){
        Set<BluetoothDevice> bondedDevices = _bluetoothAdapter.getBondedDevices();

        if (bondedDevices.size() > 0) {

            _previouslyDescoveredDevices = new ArrayList<BluetoothDevice>();
            for ( BluetoothDevice device : bondedDevices)
            {
                _previouslyDescoveredDevices.add(device);
            }
        }

        return _previouslyDescoveredDevices;
    }

    public void StartServer(){
        _bluetoothServer = new BluetoothServerThread(_bluetoothAdapter, this);
        _bluetoothServer.run();
    }

    public void StopServer(){
        if (_bluetoothServer == null){
            _bluetoothServer.cancel();
        }
    }

    public void BecomeDiscoverable(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);

        _context.startActivity(discoverableIntent);
    }

    private void SendGreatings(){
        byte[] bytes = "Sony says hi".getBytes();
        _connectedThread.write(bytes);
    }

    @Override
    public void Connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {

        _connectedThread = new BluetoothConnectedThread(mmSocket, this);
        _connectedThread.start();

        int a = 5;
        while(a > 0){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SendGreatings();
            a--;
        }
    }

    @Override
    public void DataRecieved(byte[] byteArray, int bytesCount) {
        try {

            final String str = new String(byteArray, "UTF-8");
            _context.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(_context.getBaseContext(), str, Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
