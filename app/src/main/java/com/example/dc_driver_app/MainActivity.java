package com.example.dc_driver_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button connectBtn, dcBtn, servoBtn, stepperBtn, platformBtn;
    Switch btOnOffSwitch;
    ImageView btImage;
    BluetoothAdapter bt_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_adapter = BluetoothAdapter.getDefaultAdapter();
        connectBtn = (Button)findViewById(R.id.connect_btn);
        dcBtn = (Button)findViewById(R.id.dc_btn);
        servoBtn = (Button)findViewById(R.id.servo_btn);
        stepperBtn = (Button)findViewById(R.id.stepper_btn);
        platformBtn = (Button)findViewById(R.id.platform_btn);
        btOnOffSwitch = (Switch)findViewById(R.id.bt_switch);
        btImage = (ImageView)findViewById(R.id.bt_status_image_view);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceName, deviceMAC;
                if(bt_adapter.isEnabled()){
                    Set<BluetoothDevice> devices = bt_adapter.getBondedDevices();
                    for(BluetoothDevice device: devices){
                        deviceName = device.getName();
                        if(){
                            deviceMAC = device.getAddress();
//                            connect to driver
                        }
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Turn on Bluetooth first", Toast.LENGTH_LONG);
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
                    Intent platformIntent;
                }
                else
                    Toast.makeText(MainActivity.this, "Connect to driver first!", Toast.LENGTH_LONG).show();

            }
        });

        btOnOffSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btOnOffSwitch.isChecked()){
                    Toast.makeText(MainActivity.this, "Turning on Bluetooth...", Toast.LENGTH_LONG).show();
                    if(!bt_adapter.isEnabled()){
                        Intent btOnIntent = new Intent(bt_adapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(btOnIntent,1);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Turning off Bluetooth...", Toast.LENGTH_LONG).show();
                    if(bt_adapter.isEnabled()){
                        bt_adapter.disable();
                        btImage.setImageResource(R.drawable.ic_action_bt_off);
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            btImage.setImageResource(R.drawable.ic_action_bt_on);
        }

    }
}
