package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    private ImageView iv_profile;
    private Button btn_edit;
    private TextView tv_firstname;
    private TextView tv_gender;
    private String userImage;
    private String userFirstname;
    private String userLastname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display2);
        iv_profile = findViewById(R.id.iv_profile);
        tv_firstname = findViewById(R.id.tv_firstName);
        tv_gender = findViewById(R.id.tv_lastname);
        btn_edit = findViewById(R.id.btn_edit);
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("USER_IMAGE")){
                userImage = getIntent().getExtras().getString("USER_IMAGE");
                if(userImage != null){
                    tv_gender.setText(userImage);
                    if(userImage.equals("male")){
                        iv_profile.setImageDrawable(getDrawable(R.drawable.male));

                    }else{
                        iv_profile.setImageDrawable(getDrawable(R.drawable.female));

                    }
                }
            }
            if(getIntent().getExtras().containsKey("USER_FIRSTNAME") && getIntent().getExtras().containsKey("USER_LASTNAME")){
                userFirstname = getIntent().getExtras().getString("USER_FIRSTNAME");
                userLastname = getIntent().getExtras().getString("USER_LASTNAME");
                if(userFirstname != null && userLastname != null){
                  tv_firstname.setText("Name : " +userFirstname +" "+userLastname);
                }
            }
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent displayindent = new Intent(DisplayActivity.this,MainActivity.class);
                    displayindent.putExtra("USER_IMAGE",userImage);
                    displayindent.putExtra("USER_FIRSTNAME" , userFirstname);
                    displayindent.putExtra("USER_LASTNAME" , userLastname);
                    setResult(RESULT_OK,displayindent);
                    finish();
                }
            });
        }
    }
}
