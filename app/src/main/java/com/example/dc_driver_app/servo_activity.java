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
import android.widget.TextView;

public class servo_activity extends AppCompatActivity {
    Button servoBackBtn;
    SeekBar servoAPosition, servoBPosition;
    TextView servoAPositionValue, servoBPositionValue;
    ServiceConnection btServiceConnection;
    protected bluetooth_service btService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servo_activity);

        servoBackBtn = (Button)findViewById(R.id.dc_stepper_btn);
        servoAPosition = (SeekBar)findViewById(R.id.servo_A_position_bar);
        servoBPosition = (SeekBar)findViewById(R.id.servo_B_position_bar);
        servoAPositionValue = (TextView)findViewById(R.id.servo_A_position_text);
        servoBPositionValue = (TextView)findViewById(R.id.servo_B_position_text);
        servoAPositionValue.setText("0");
        servoBPositionValue.setText("0");

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

        Intent btServiceStart = new Intent(servo_activity.this, bluetooth_service.class);
        bindService(btServiceStart,btServiceConnection, Context.BIND_AUTO_CREATE);

        servoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        servoAPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int tmp = servoAPosition.getProgress();
                servoAPositionValue.setText(String.valueOf(tmp));
                String commandSA = "SA" + String.valueOf(tmp) + "\n";
                btService.sendCommandViaBluetooth(commandSA);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        servoBPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int tmp = servoBPosition.getProgress();
                servoBPositionValue.setText(String.valueOf(tmp));
                String commandSB = "SB" + String.valueOf(tmp) + "\n";
                btService.sendCommandViaBluetooth(commandSB);
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
