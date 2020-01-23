package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private TextView result1;
    private TextView result2;
    private Button button_calculate;
    private EditText et_weight_lb;
    private EditText et_height_feet;
    private EditText et_height_inches;
    boolean isCorrect = true;
    double coverted_height_inches;
    double calculated_bmi;
    double coverted_inches;
    double converted_inches_to_feet;
    double coverted_inches_remainder;
    private static DecimalFormat df2 = new DecimalFormat("#.##");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BMI Calculator");

        et_weight_lb = findViewById(R.id.et_weight_lb);
        et_height_feet = findViewById(R.id.et_height_feet) ;
        et_height_inches = findViewById(R.id.et_height_inches);
        button_calculate = findViewById(R.id.btn_calculate);
        result1 = findViewById(R.id.tv_result_calculatedbmi);
        result2 = findViewById(R.id.tv_result_bmi_prediction);



        button_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_weight_lb.getText().toString().equals("")){
                    et_weight_lb.setError("Hey I need a value!");
                    isCorrect = false;
                }
                if(et_height_feet.getText().toString().equals("")){
                    et_height_feet.setError("Hey I need a value!");
                    isCorrect = false;
                }
                if(et_height_inches.getText().toString().equals("")){
                    et_height_inches.setError("Hey I need a value!");
                    isCorrect = false;
                }
                if(!isCorrect){
                    Toast.makeText(getApplicationContext(), "Please enter values in all the fields", Toast.LENGTH_SHORT).show();

                }else{
                    if(Float.parseFloat(et_height_inches.getText().toString()) > 12){
                        Toast.makeText(getApplicationContext(), "Coverting inches to feet", Toast.LENGTH_SHORT).show();
                        coverted_inches= (Double.parseDouble(et_height_inches.getText().toString())/12);
                        coverted_inches_remainder = Double.parseDouble(et_height_inches.getText().toString())%12;
                        converted_inches_to_feet = Double.parseDouble(et_height_feet.getText().toString()) + Math.round(coverted_inches);

                        et_height_feet.setText((Double.toString(converted_inches_to_feet)));
                        et_height_inches.setText(Double.toString(coverted_inches_remainder));

                    }
                    coverted_height_inches = (12*Double.parseDouble(et_height_feet.getText().toString()))+Double.parseDouble(et_height_inches.getText().toString());
                    calculated_bmi = ((Double.parseDouble(et_weight_lb.getText().toString()))/(coverted_height_inches * coverted_height_inches) * 703);
                    result1.setText("Your BMI : " + df2.format(calculated_bmi));
                    if(calculated_bmi < 18.5){
                        result2.setText("You are Underweight");

                    }else if(calculated_bmi >= 18.5 && calculated_bmi <= 24.9){
                        result2.setText("You are Normalweight");

                    }else if(calculated_bmi >= 25 && calculated_bmi <= 29.9){
                        result2.setText("You are Overweight");

                    }else if(calculated_bmi >=30 ){
                        result2.setText("You are Obesity");


                    }
                }
            }
        });


    }
}
