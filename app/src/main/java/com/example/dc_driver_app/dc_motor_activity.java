package com.example.dc_driver_app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

public class dc_motor_activity extends AppCompatActivity {
    Button dcBackBtn, stopBtn;
    Switch dc1Direction, dc2Direction;
    SeekBar dc1Velocity, dc2Velocity;
    int dc1ActiveCommand = 1;
    int dc2ActiveCommand = 1;
    ServiceConnection btServiceConnection;
    protected bluetooth_service btService;
    private final String stop_command = "MA50\nMB50\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc_motor_activity);

        dcBackBtn = (Button)findViewById(R.id.dc_menu_btn);
        stopBtn = (Button)findViewById(R.id.stop_btn);
        dc1Direction = (Switch)findViewById(R.id.dc1_direction) ;
        dc2Direction = (Switch)findViewById(R.id.dc2_direction);
        dc1Velocity = (SeekBar)findViewById(R.id.dc1_velocity);
        dc2Velocity = (SeekBar)findViewById(R.id.dc_2_velocity);

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

        final Intent btServiceStart = new Intent(dc_motor_activity.this, bluetooth_service.class);
        bindService(btServiceStart,btServiceConnection, Context.BIND_AUTO_CREATE);

        dcBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btService.sendCommandViaBluetooth(stop_command);
                finish();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btService.sendCommandViaBluetooth(stop_command);
            }
        });

        dc1Direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dc1Direction.isChecked()){
                    dc1ActiveCommand = 1;
                }
                else{
                    dc1ActiveCommand = -1;
                }
            }
        });

        dc2Direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dc2Direction.isChecked()){
                    dc2ActiveCommand = 1;
                }
                else{
                    dc2ActiveCommand = -1;
                }
            }
        });

        dc1Velocity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int tmp1 = dc1Velocity.getProgress();
                int val1 = 50 + tmp1*dc1ActiveCommand;
                String commandA = "MA" + String.valueOf(val1) + "\n";
                btService.sendCommandViaBluetooth(commandA);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dc2Velocity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int tmp2 = dc2Velocity.getProgress();
                int val2 = 50 + tmp2*dc2ActiveCommand;
                String commandB = "MB" + String.valueOf(val2) + "\n";
                btService.sendCommandViaBluetooth(commandB);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}
