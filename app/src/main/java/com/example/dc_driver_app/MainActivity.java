package com.example.dc_driver_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button connectBtn, dcBtn, servoBtn, stepperBtn, platformBtn;
    Switch btOnOffSwitch;
    ImageView btImage;
    BluetoothAdapter bt_adapter;
    ListView btDevices;
    ArrayList btDevicesList;
    ArrayAdapter btListAdapter;
    String MAC;
    ServiceConnection btServiceConnection;
    boolean connectionStatus=false;
    protected bluetooth_service btService;
    private static final int MAC_Length = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btDevicesList = new ArrayList();
        bt_adapter = BluetoothAdapter.getDefaultAdapter();
        connectBtn = (Button)findViewById(R.id.connect_btn);
        dcBtn = (Button)findViewById(R.id.dc_btn);
        servoBtn = (Button)findViewById(R.id.servo_btn);
        stepperBtn = (Button)findViewById(R.id.stepper_btn);
        platformBtn = (Button)findViewById(R.id.platform_btn);
        btOnOffSwitch = (Switch)findViewById(R.id.bt_switch);
        btImage = (ImageView)findViewById(R.id.bt_status_image_view);
        btDevices = (ListView)findViewById(R.id.bt_device_list);

        btServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                bluetooth_service.BtBinder binder = (bluetooth_service.BtBinder) service;
                btService = binder.getService();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent btServiceStart = new Intent(MainActivity.this, bluetooth_service.class);
        bindService(btServiceStart,btServiceConnection,Context.BIND_AUTO_CREATE);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!connectionStatus) {
                    if (bt_adapter.isEnabled()) {
                        btDevices.setAdapter(btListAdapter);
                        btListAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, btService.getPaired());
                        btDevices.setOnItemClickListener(listClickListener);
                    } else {
                        Toast.makeText(MainActivity.this, "Turn on Bluetooth first", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if(btService.disconnectBtDevice()) {
                        btImage.setImageResource(R.drawable.ic_action_bt_on);
                        connectBtn.setText(R.string.connect_btn_label);
                        connectionStatus = false;
                    }
                }

            }
        });

        dcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_adapter.isEnabled()) {
                    Intent dcIntent = new Intent(MainActivity.this, dc_motor_activity.class);
                    startActivity(dcIntent);
                }
                else
                    Toast.makeText(MainActivity.this, "Connect to driver first!", Toast.LENGTH_LONG).show();
            }
        });

        servoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_adapter.isEnabled()) {
                    Intent servoIntent = new Intent(MainActivity.this, servo_activity.class);
                    startActivity(servoIntent);
                }
                else
                    Toast.makeText(MainActivity.this, "Connect to driver first!", Toast.LENGTH_LONG).show();
            }
        });

        stepperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_adapter.isEnabled()) {
                    Intent stepperIntent = new Intent(MainActivity.this, stepper_motor_activity.class);
                    startActivity(stepperIntent);
                }
                else
                    Toast.makeText(MainActivity.this, "Connect to driver first!", Toast.LENGTH_LONG).show();
            }
        });

        platformBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_adapter.isEnabled()){
                    Intent platformIntent = new Intent(MainActivity.this, mobile_platform_activity.class);
                    startActivity(platformIntent);
                }
                else
                    Toast.makeText(MainActivity.this, "Connect to driver first!", Toast.LENGTH_LONG).show();

            }
        });

        btOnOffSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btOnOffSwitch.isChecked()) {
                    Intent btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(btEnableIntent, 1);
                    btImage.setImageResource(R.drawable.ic_action_bt_on);
                }
                else{
                    btService.bluetoothOff();
                    btImage.setImageResource(R.drawable.ic_action_bt_off);
                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Toast.makeText(MainActivity.this, "Bluetooth enabled", Toast.LENGTH_LONG).show();
        }

    }

    private void getPairedDevices(){
        String deviceName, deviceMAC;
        if(bt_adapter.isEnabled()){
            Set<BluetoothDevice> devices = bt_adapter.getBondedDevices();
            if(devices.size()>0) {
                for (BluetoothDevice device : devices) {
                    btDevicesList.add("Name: " + device.getName() + " MAC: " + device.getAddress());
                }
                btDevices.setAdapter(btListAdapter);
            }

            btListAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1 , btDevicesList);
        }
        else{
            Toast.makeText(MainActivity.this, "Turn on Bluetooth first", Toast.LENGTH_LONG).show();
        }
    }

    private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            String listElement = ((TextView)view).getText().toString();
            MAC = listElement.substring(listElement.length() - MAC_Length);
            if(btService.connectBtDevice(MAC)){
                btImage.setImageResource(R.drawable.ic_action_bt_connected);
                connectBtn.setText(R.string.disconnect_btn_label);
                connectionStatus = true;
            }
        }
    };
}
