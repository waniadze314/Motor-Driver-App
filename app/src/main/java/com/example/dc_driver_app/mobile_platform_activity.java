package com.example.dc_driver_app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class mobile_platform_activity extends AppCompatActivity implements SensorEventListener {
    Button platformBackBtn;
    ImageButton startStopBtn;
    ImageView fwdIv, bwdIv, leftIv, rightIv;
    SensorManager sMng;
    Sensor accelerometer;
    static final String TAG = "platform_activity";
    boolean motion=false;
    ServiceConnection btServiceConnection;
    protected bluetooth_service btService;
    final String stop_command = "MA50\nMB50\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_platform_activity);

        sMng = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sMng.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sMng.registerListener(this, accelerometer, sMng.SENSOR_DELAY_NORMAL);

        startStopBtn = (ImageButton) findViewById(R.id.start_stop_btn);
        startStopBtn.setImageResource(R.drawable.start);
        platformBackBtn = (Button)findViewById(R.id.platform_back_btn);
        fwdIv = (ImageView)findViewById(R.id.forward_view);
        leftIv = (ImageView)findViewById(R.id.left_view);
        bwdIv = (ImageView)findViewById(R.id.backward_view);
        rightIv = (ImageView)findViewById(R.id.right_view);

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

        Intent btServiceStart = new Intent(mobile_platform_activity.this, bluetooth_service.class);
        bindService(btServiceStart,btServiceConnection,Context.BIND_AUTO_CREATE);

        platformBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion=false;
                finish();
            }
        });

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!motion){
                    startStopBtn.setImageResource(R.drawable.stop);
                    motion = true;
                }
                else{
                    startStopBtn.setImageResource(R.drawable.start);
                    btService.sendCommandViaBluetooth(stop_command);
                    motion = false;
                }

            }
        });


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x_value, y_value;
        int x_command, y_command;
        String commandMotion;
        //X Axis
        if(motion) {
            x_value = event.values[0];
            y_value = event.values[1];

            x_command = 50 + 5*Math.round(x_value);
            y_command = 50 + 5*Math.round(y_value);
            //do poprawienia
            commandMotion = "X: " + String.valueOf(x_command) + "Y: " + String.valueOf(y_command) + "\n";
            btService.sendCommandViaBluetooth(commandMotion);




            if (x_value > 1) {
                leftIv.setImageResource(R.drawable.left_arrow_active);
                rightIv.setImageResource(R.drawable.right_arrow_inactive);
            } else if (x_value < -1) {
                leftIv.setImageResource(R.drawable.left_arrow_inactive);
                rightIv.setImageResource(R.drawable.right_arrow_active);
            } else {
                leftIv.setImageResource(R.drawable.left_arrow_inactive);
                rightIv.setImageResource(R.drawable.right_arrow_inactive);
            }

            //Y Axis
            if (y_value > 1) {
                fwdIv.setImageResource(R.drawable.forward_arrow_inactive);
                bwdIv.setImageResource(R.drawable.backward_arrow_active);
            } else if (y_value < -1) {
                fwdIv.setImageResource(R.drawable.forward_arrow_active);
                bwdIv.setImageResource(R.drawable.backward_arrow_inactive);
            } else {
                fwdIv.setImageResource(R.drawable.forward_arrow_inactive);
                bwdIv.setImageResource(R.drawable.backward_arrow_inactive);
            }
        }

    }
}

