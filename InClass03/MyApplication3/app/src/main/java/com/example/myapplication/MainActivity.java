package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_firtname;
    private EditText et_lastname;
    private RadioGroup rg_gender;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private ImageView iv_gender;
    private Button btn_save;
    private String flag_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_firtname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        rg_gender = findViewById(R.id.rg_gender);
        rb_female = findViewById(R.id.rg_female);
        rb_male = findViewById(R.id.rg_male);
        iv_gender = findViewById(R.id.iv_gender);
        btn_save = findViewById(R.id.button_save);
         rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
             @Override
             public void onCheckedChanged(RadioGroup radioGroup, int i) {
                 switch (radioGroup.getCheckedRadioButtonId()){
                     case R.id.rg_female:
                         iv_gender.setImageDrawable(getDrawable(R.drawable.female));
                         flag_image = "female";
                         break;
                     case R.id.rg_male:
                         iv_gender.setImageDrawable(getDrawable(R.drawable.male));
                         flag_image = "male";
                         break;
                      default:
                          break;
                 }

             }
         });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isError = false;
                if(et_firtname.getText().toString().equals("")){
                    et_firtname.setError("Please enter the first name");
                    isError =true;

                }
                if(et_lastname.getText().toString().equals("")){
                    et_lastname.setError("Please enter the first name");
                    isError =true;


                }
                if(flag_image == null){

                    Toast.makeText(getApplicationContext(), "Please check the radio button", Toast.LENGTH_SHORT).show();
                    isError =true;
                }

                if(!isError){
                    Intent intent = new Intent(MainActivity.this,DisplayActivity.class);
                    intent.putExtra("USER_IMAGE",flag_image);
                    intent.putExtra("USER_FIRSTNAME" , et_firtname.getText().toString());
                    intent.putExtra("USER_LASTNAME" , et_lastname.getText().toString());
                    startActivityForResult(intent,100);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        et_firtname.setText(data.getExtras().getString("USER_FIRSTNAME"));
        et_lastname.setText(data.getExtras().getString("USER_LASTNAME"));
        if(data.getExtras().getString("USER_IMAGE").equals("male")){
            rb_male.setChecked(true);
        }else {
            rb_female.setChecked(true);
        }


    }
}
