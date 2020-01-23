package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Group Number  : 17
 * Members : Sathish kumar Balasubramaian ,Gokul Nithin Kumar
 *
 */

public class OrderActivity extends AppCompatActivity {
    private TextView tv_toppings_value;
    private TextView tv_topping_list;
    private TextView tv_delivery_value;
    private TextView tv_total_value;
    private Boolean isDeliveryEnabled;
    private Double Total = 6.5;
    private Double ToppingPrice = 0.0;
    private Button btn_finish;
    ArrayList<String> selectTopping = new ArrayList<String>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle("Pizza Store");

        tv_toppings_value = findViewById(R.id.tv_topping_value);
        tv_topping_list = findViewById(R.id.tv_toppings_list);
        tv_delivery_value = findViewById(R.id.delivery_cost_value);
        tv_total_value = findViewById(R.id.tv_total_value);
        btn_finish = findViewById(R.id.btn_finish);


        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("TAG_DELIVERY")) {
                isDeliveryEnabled = getIntent().getExtras().getBoolean("TAG_DELIVERY");
                if(isDeliveryEnabled){
                    tv_delivery_value.setText("4.0$");
                    Total = Total + 4.0;
                }else{
                    tv_delivery_value.setText("0$");
                }
            }

            if(getIntent().getExtras().containsKey("TAG_SELECTED_TOPPING")) {
                System.out.println("=======fsdfsdf"+getIntent().getExtras().getStringArrayList("TAG_SELECTED_TOPPING"));
                selectTopping = getIntent().getExtras().getStringArrayList("TAG_SELECTED_TOPPING");
                String joined = String.join(",", selectTopping);
                tv_topping_list.setText(joined);
                ToppingPrice = selectTopping.size() * 1.5;
                tv_toppings_value.setText(ToppingPrice.toString());
                Total = Total + ToppingPrice;
                tv_total_value.setText(Total.toString() + "$");
            }
        }
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderindent = new Intent(OrderActivity.this,MainActivity.class);
                setResult(RESULT_OK,orderindent);
                finish();
            }
        });


    }
}
