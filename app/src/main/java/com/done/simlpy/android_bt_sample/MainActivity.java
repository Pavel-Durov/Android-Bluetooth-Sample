package com.done.simlpy.android_bt_sample;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.done.simlpy.android_bt_sample.bluretooth.BluetoothService;
import com.done.simlpy.android_bt_sample.ui.ListViewDevicesAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BluetoothService _bluetoothSevice;
    ListView _bluetoothDevicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _bluetoothDevicesListView = (ListView) findViewById(R.id.blueToothDevicesListView);

        _bluetoothSevice = new BluetoothService(this);
        _bluetoothSevice.Init();


        _bluetoothDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                BluetoothDevice device = _bluetoothDevices.get(position);
                _bluetoothSevice.Connect(device);
            }
        });
    }

    ArrayList<BluetoothDevice> _bluetoothDevices;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_become_discoverable:
                _bluetoothSevice.BecomeDiscoverable();
                break;
            case  R.id.action_scan:
                _bluetoothDevices = _bluetoothSevice.ScanForDevices();
                _bluetoothDevicesListView.setAdapter(new ListViewDevicesAdapter(getApplicationContext(), _bluetoothDevices));
                break;
            case  R.id.action_start_server:
                _bluetoothSevice.StartServer();
                break;
            default:
                handled = false;
                break;
        }

        if(handled){
            return  handled;
        }
        return super.onOptionsItemSelected(item);
    }


}
