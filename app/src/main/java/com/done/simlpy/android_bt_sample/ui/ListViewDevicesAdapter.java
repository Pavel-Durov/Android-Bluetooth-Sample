package com.done.simlpy.android_bt_sample.ui;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.done.simlpy.android_bt_sample.R;

import java.util.ArrayList;

/**
 * Created by pduro on 8/13/2016.
 */
public class ListViewDevicesAdapter extends ArrayAdapter<BluetoothDevice> {

    public ListViewDevicesAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDevice) {
        super(context, 0, bluetoothDevice);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BluetoothDevice device = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bluetooth_device, parent, false);
            // Lookup view for data population
            TextView deviceAddressTxt = (TextView) convertView.findViewById(R.id.deviceAddressTxt);
            TextView deviceNameTxt = (TextView) convertView.findViewById(R.id.deviceNameTxt);

            deviceNameTxt.setText(device.getName());
            deviceAddressTxt.setText(device.getAddress());
        }
        // Populate the data into the template view using the data object
        // tvName.setText(user.name);
        // tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return convertView;
    }
}