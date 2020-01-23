package com.example.inclass4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
 * Group Number : Group 26
 * Team Members : Sathish Kumar Balasubramanian , Gokul Nithinkumar RajKumar
 */

public class MainActivity extends AppCompatActivity {

    private SeekBar complexity_seek_bar;
    private Integer progress_value = 0;
    private TextView tv_progress_value;
    private Button btn_generate;
    private TextView tv_minimum_value;
    private TextView tv_maximum_value;
    private TextView tv_average_value;
    ExecutorService threadPool;
    Handler handler;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Homework 03");

        complexity_seek_bar = findViewById(R.id.seek_complexity);
        tv_progress_value = findViewById(R.id.tv_complexity_value);
        btn_generate = findViewById(R.id.btn_generate);
        tv_average_value = findViewById(R.id.tv_average_value);
        tv_minimum_value = findViewById(R.id.tv_minimum_value);
        tv_maximum_value = findViewById(R.id.tv_maximum_value);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);


        complexity_seek_bar.setMax(10);
        threadPool = Executors.newFixedThreadPool(2);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                String maximum_value ;
                String minimun_value;
                String average_value;
                System.out.println("==========message======"+message.getData().getDouble("AVERAGE_VALUE"));

                switch (message.what){
                    case  DoWork.START_PROCESS :
                        refreshTextView();
                        progressBar.setIndeterminate(true);
                        progressBar.setVisibility(View.VISIBLE);


                        break;
                    case  DoWork.STOP_PROCESS :
                        if(message.getData() != null){
                            maximum_value = String.valueOf(message.getData().getDouble("MAXIMUM_VALUE"));
                            minimun_value = String.valueOf(message.getData().getDouble("MINIMUM_VALUE"));
                            average_value = String.valueOf(message.getData().getDouble("AVERAGE_VALUE"));
                            if(!maximum_value.equals("")){
                                tv_maximum_value.setText(maximum_value);
                            }
                            if(!minimun_value.equals("")){
                                tv_minimum_value.setText(minimun_value);
                            }
                            if(!average_value.equals("")){
                                tv_average_value.setText(average_value);
                            }
                        }
                        progressBar.setVisibility(View.INVISIBLE);

                        break;

                }

                return false;
            }
        });

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(progress_value > 0) {
                    threadPool.execute(new DoWork(progress_value));
                }else {
                    refreshTextView();
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(getApplicationContext(),"Progress should be greater than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

        complexity_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                complexity_seek_bar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_progress_value.setText(seekBar.getProgress() +" times");
                progress_value = seekBar.getProgress();
            }
        });
    }

    private void refreshTextView() {
        tv_average_value.setText("");
        tv_maximum_value.setText("");
        tv_minimum_value.setText("");

    }


    class DoWork implements Runnable {

        ArrayList<Double> finalArray= new ArrayList<Double>();
        private final Integer progress_value;
        static final int START_PROCESS = 0X00;
        static final int STOP_PROCESS = 0X02;
        static final int STATUS_PROCESS = 0X01;

        public DoWork(Integer progress_value) {
            this.progress_value = progress_value;
        }

        @Override
        public void run() {
            Double sum = 0.0;
            Double averageValue = 0.0;
            Double minimumValue = 0.0;
            Double maximumValue = 0.0;
            Message startMessage = new Message();
            startMessage.what = START_PROCESS;
            handler.sendMessage(startMessage);
            finalArray = HeavyWork.getArrayNumbers(this.progress_value);

            Collections.sort(finalArray);
            for(int i = 0 ; i < finalArray.size() ; i++){
                sum  += finalArray.get(i);
            }
            averageValue = sum / finalArray.size();
            minimumValue = finalArray.get(0);
            maximumValue = finalArray.get(finalArray.size()-1);

            Message stopMessage = new Message();
            stopMessage.what = STOP_PROCESS;
            Bundle bundle = new Bundle();
            bundle.putDouble("MINIMUM_VALUE",minimumValue);
            bundle.putDouble("MAXIMUM_VALUE",maximumValue);
            bundle.putDouble("AVERAGE_VALUE",averageValue);
            stopMessage.setData(bundle);
            handler.sendMessage(stopMessage);
        }
    }


}
