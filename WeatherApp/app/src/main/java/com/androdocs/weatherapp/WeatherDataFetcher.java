package com.androdocs.weatherapp;

import android.os.AsyncTask;
import android.view.View;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherDataFetcher extends AsyncTask<String, Void, String> {
    MainActivity mainActivity;

    private static WeatherDataFetcher weatherDataFetcherInstance;

    public static WeatherDataFetcher getInstance(MainActivity mainActivity)
    {
        if(weatherDataFetcherInstance==null)
        {
            weatherDataFetcherInstance = new WeatherDataFetcher(mainActivity);
        }
        return weatherDataFetcherInstance;
    }

    WeatherDataFetcher(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /* Showing the ProgressBar, Making the main design GONE */
        mainActivity.findViewById(R.id.loader).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.mainContainer).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.errorText).setVisibility(View.GONE);
    }

    protected String doInBackground(String... args) {
        String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + mainActivity.CITY + "&units=metric&appid=" + mainActivity.API);
        return response;
    }

    @Override
    protected void onPostExecute(String result) {


        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject main = jsonObj.getJSONObject("main");
            JSONObject sys = jsonObj.getJSONObject("sys");
            JSONObject wind = jsonObj.getJSONObject("wind");
            JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

            Long updatedAt = jsonObj.getLong("dt");
            String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
            String temp = main.getString("temp") + "°C";
            String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
            String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
            String pressure = main.getString("pressure");
            String humidity = main.getString("humidity");

            MainActivity.currTemp = main.getString("temp");

            Long sunrise = sys.getLong("sunrise");
            Long sunset = sys.getLong("sunset");
            String windSpeed = wind.getString("speed");
            String weatherDescription = weather.getString("description");

            String address = jsonObj.getString("name") + ", " + sys.getString("country");


            /* Populating extracted data into our views */
            mainActivity.addressTxt.setText(address);
            mainActivity.updated_atTxt.setText(updatedAtText);
            mainActivity.statusTxt.setText(weatherDescription.toUpperCase());
            mainActivity.tempTxt.setText(temp);
            mainActivity.temp_minTxt.setText(tempMin);
            mainActivity.temp_maxTxt.setText(tempMax);
            mainActivity.sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
            mainActivity.sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
            mainActivity.windTxt.setText(windSpeed);
            mainActivity.pressureTxt.setText(pressure);
            mainActivity.humidityTxt.setText(humidity);

            /* Views populated, Hiding the loader, Showing the main design */
            mainActivity.findViewById(R.id.loader).setVisibility(View.GONE);
            mainActivity.findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


        } catch (JSONException e) {
            mainActivity.findViewById(R.id.loader).setVisibility(View.GONE);
            mainActivity.findViewById(R.id.errorText).setVisibility(View.VISIBLE);
        }

    }
}
