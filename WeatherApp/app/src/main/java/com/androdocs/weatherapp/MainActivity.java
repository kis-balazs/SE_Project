package com.androdocs.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static public String currTemp;

    String CITY = "Cluj-Napoca, RO";
    String API = "3db7564cc75598c2117cd8c49b1ab34a";

    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;

    private Intent intentPredict;
    private Intent intentPlot;

    private Button buttonPredict;
    private Button buttonPlot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentPredict = new Intent (this,FireBase.class);
        intentPlot = new Intent (this,PlotActivity.class);

        buttonPredict = findViewById(R.id.buttonPred);
        buttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentPredict.putExtra("temp",currTemp);
                startActivity(intentPredict);
            }
        });


        buttonPlot = findViewById(R.id.buttonPlot);
        buttonPlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentPlot);
            }
        });


        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);

        WeatherDataFetcher singletonObject = WeatherDataFetcher.getInstance(this);
        singletonObject.execute();
    }
}