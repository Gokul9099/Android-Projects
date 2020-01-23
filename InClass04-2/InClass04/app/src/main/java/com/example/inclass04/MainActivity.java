package com.example.inclass04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int progressChangedValue = 0;
    Button generate;
    SeekBar seekbar;
    TextView complexity;
    TextView minimum;
    TextView maximum;
    TextView average;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generate = (Button)findViewById(R.id.b_generate);
        seekbar = (SeekBar)findViewById(R.id.sb_complexity);
        complexity = (TextView)findViewById(R.id.tv_complexity);
        minimum = (TextView)findViewById(R.id.tv_min);
        maximum = (TextView)findViewById(R.id.tv_max);
        average = (TextView)findViewById(R.id.tv_avg);
        progress = (ProgressBar)findViewById(R.id.pb_progress);

        seekbar.setMax(10);
        seekbar.setProgress(0);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                complexity.setText(progressChangedValue+" times");
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(progressChangedValue==0){
                    Toast.makeText(MainActivity.this,"value cannot be zero" , Toast.LENGTH_SHORT).show();
                    minimum.setText("");
                    maximum.setText("");
                    average.setText("");
                }
                else
                new Myclass().execute(progressChangedValue);
            }
        });
    }

    private class Myclass extends AsyncTask<Integer,Integer, ArrayList>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);


        }


        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            Double max = (Double)Collections.max(arrayList);
            Double  min= (Double)Collections.min(arrayList);
            Double avg = (Double)calculateAverage(arrayList);

            minimum.setText(String.format("%.8f", max));
            maximum.setText(String.format("%.8f", min));
            average.setText(String.format("%.8f", avg));
            progress.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        private double calculateAverage(List<Double> marks) {
            Double sum = 0.0;
            if(!marks.isEmpty()) {
                for (Double mark : marks) {
                    sum += mark;
                }
                return sum.doubleValue() / marks.size();
            }
            return sum;
        }

        @Override
        protected ArrayList doInBackground(Integer... integers) {
            int complexity = integers[0];
            ArrayList<Double> a = new ArrayList<>();
            a = HeavyWork.getArrayNumbers(complexity);

            return a;
        }
    }
}
