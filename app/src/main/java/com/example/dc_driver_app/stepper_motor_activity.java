package com.example.dc_driver_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class stepper_motor_activity extends AppCompatActivity {
    Button stepperBackBtn, moveLeftBtn, moveRightBtn;
    EditText stepsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_motor_activity);

        stepperBackBtn.findViewById(R.id.stepper_back_btn);
        stepsInput.findViewById(R.id.steps_input);
        moveLeftBtn.findViewById(R.id.left_btn);
        moveRightBtn.findViewById(R.id.right_btn);


        stepperBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}
