package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

/*
 * Group Number  : 17
 * Members : Sathish kumar Balasubramaian ,Gokul Nithin Kumar
 *
 */

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout pizza_layout;
    private  LinearLayout layout1;
    private  LinearLayout layout2;
    private Button btn_add_topping;
    private ImageView[] imgTopping = new ImageView[10];
    private Switch switch_delivery;
    private Button btn_clear_topping;
    private Button btn_checkout;
    private Boolean isdeliveryChecked = false ;
    private ProgressBar progressBar;
    ArrayList<String> selectTopping = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Pizza Store");
        setContentView(R.layout.activity_main);

        final String [] toppings = {"Bacon", "Cheese", "Garlic", "Green pepper", "Mushroom", "Olive", "Onion", "Red pepper"};

        pizza_layout = findViewById(R.id.main_layout);
        layout1 = findViewById(R.id.linear_layout1);
        layout2 = findViewById(R.id.linear_layout2);
        btn_add_topping = findViewById(R.id.add_topping);
        btn_clear_topping =  findViewById(R.id.clear_pizza);
        switch_delivery = findViewById(R.id.switch_delivery);
        btn_checkout = findViewById(R.id.btn_checkout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(10);



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a Topping")
                .setCancelable(false)
                .setItems(toppings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(selectTopping.size() < 10) {
                            selectTopping.add(toppings[i]);
                            progressBar.setProgress(selectTopping.size());
                            addImageView();
                        }else {
                            Toast.makeText(getApplicationContext(), "Maximum limit for topping reached", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        btn_add_topping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog toppingList = builder.create();
                toppingList.show();
            }
        });
        btn_clear_topping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTopping.removeAll(selectTopping);
                progressBar.setProgress(selectTopping.size());
                addImageView();

            }
        });
        switch_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isdeliveryChecked = isChecked;
            }
        });
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectTopping.size() >0 ){
                    Intent intent = new Intent(MainActivity.this,OrderActivity.class);
                    intent.putExtra("TAG_DELIVERY",isdeliveryChecked);
                    intent.putStringArrayListExtra("TAG_SELECTED_TOPPING",selectTopping);
                    startActivityForResult(intent,100);
                }else {
                    Toast.makeText(getApplicationContext(), "Please select atleast 1 topping", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addImageView() {
        layout1.removeAllViews();
        layout2.removeAllViews();
        for (int k =0 ; k < selectTopping.size() ; k++){
            String img_id = "img_"+selectTopping.get(k);
            imgTopping[k] = new ImageView(getApplicationContext());
            imgTopping[k].setId(k);
            imgTopping[k].setPadding(20,0,20,0);
            imgTopping[k].setLayoutParams(new LinearLayout.LayoutParams(180, 180));
            String currentTopping = selectTopping.get(k);
            switchCaseForTopping(imgTopping[k],currentTopping)  ;
            imgTopping[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     selectTopping.remove(view.getId());
                    progressBar.setProgress(selectTopping.size());
                     addImageView();

                }
            });
            if(k < 5 ){
                layout1.addView(imgTopping[k]);
            }else {
                layout2.addView(imgTopping[k]);
            }

        }
    }

    private void switchCaseForTopping(ImageView imgTopping, String currentTopping) {
        switch (currentTopping){
            case "Bacon":
                imgTopping.setImageDrawable(getDrawable(R.drawable.bacon));
                break;
            case "Cheese":
                imgTopping.setImageDrawable(getDrawable(R.drawable.cheese));
                break;
            case "Garlic":
                imgTopping.setImageDrawable(getDrawable(R.drawable.garlic));
                break;
            case "Green pepper":
                imgTopping.setImageDrawable(getDrawable(R.drawable.green_pepper));
                break;
            case "Mushroom":
                imgTopping.setImageDrawable(getDrawable(R.drawable.mashroom));
                break;
            case "Olive":
                imgTopping.setImageDrawable(getDrawable(R.drawable.olive));
                break;
            case "Onion":
                imgTopping.setImageDrawable(getDrawable(R.drawable.onion));
                break;
            case "Red pepper":
                imgTopping.setImageDrawable(getDrawable(R.drawable.red_pepper));
                break;
        }


    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode < 0) {
            switch_delivery.setChecked(false);
            selectTopping.removeAll(selectTopping);
            progressBar.setProgress(selectTopping.size());
            addImageView();
        }


    }
}
