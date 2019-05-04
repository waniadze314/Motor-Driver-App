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
import android.widget.EditText;

public class stepper_motor_activity extends AppCompatActivity {
    Button stepperBackBtn, moveLeftBtn, moveRightBtn;
    EditText stepsInput;
    ServiceConnection btServiceConnection;
    protected bluetooth_service btService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_motor_activity);

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

        Intent btServiceStart = new Intent(stepper_motor_activity.this, bluetooth_service.class);
        bindService(btServiceStart,btServiceConnection, Context.BIND_AUTO_CREATE);

        stepperBackBtn = (Button)findViewById(R.id.stepper_back_btn);
        moveLeftBtn = (Button)findViewById(R.id.left_btn);
        moveRightBtn = (Button)findViewById(R.id.right_btn);
        stepsInput = (EditText)findViewById(R.id.steps_input);


        stepperBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        moveRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = stepsInput.getText().toString();
                String commandsP = "sP" + val + "\n";
                btService.sendCommandViaBluetooth(commandsP);
            }
        });

        moveLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = stepsInput.getText().toString();
                String commandsN = "sN" + val + "\n";
                btService.sendCommandViaBluetooth(commandsN);
            }
        });
    }
}
