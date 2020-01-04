package com.androdocs.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.TimeZone;


public class FireBase extends AppCompatActivity {





    private String getStringDate(int date)
    {
        if(date >= 1 && date <= 9)
            return "0" + date;
        else return Integer.toString(date);
    }

    private boolean isLeapYear(int year)
    {
        if(year == 2016 || year == 2020 || year == 2024 || year == 2028 || year == 2032)
            return true;
        else return false;

    }

    private float predictWeather(DataSnapshot dataSnapshot, int currDay, int currMonth, int currYear, float todayTemp)
    {


        float yesterdayMean = 0, todayMean = 0, tomorrowMean = 0, diffMean = 0;
        int startYear = 2015;


        int yesterdayDay, yesterdayMonth, yesterdayYear, tomorrowDay, tomorrowMonth, tomorrowYear;



        if(currDay == 1)
        {
            if(currMonth == 1)
            {
                yesterdayDay = 31;
                yesterdayMonth = 12;
                yesterdayYear = currYear - 1;
            }
            else
            {
                if(currMonth == 3) //March
                {
                    if(isLeapYear(currYear))
                        yesterdayDay = 29;
                    else yesterdayDay = 28;
                }
                else if (currMonth == 2 || currMonth == 4 || currMonth == 6 || currMonth == 8 || currMonth == 9 || currMonth == 11)
                    yesterdayDay = 31;
                else yesterdayDay = 30;


                yesterdayMonth = currMonth - 1;
                yesterdayYear = currYear;
            }
        }
        else {
            yesterdayDay = currDay - 1;
            yesterdayMonth = currMonth;
            yesterdayYear = currYear;

        }

        if(currDay == 28 || currDay == 29 || currDay == 30 || currDay == 31)
        {
            if(currMonth == 12 && currDay == 31)
            {
                tomorrowDay = 1;
                tomorrowMonth = 1;
                tomorrowYear = currYear + 1;
            }
            else
            {
                tomorrowYear = currYear;
                tomorrowMonth = currMonth + 1;
                tomorrowDay = 1;

                if(currDay == 28 && isLeapYear(currYear) && currMonth == 2)
                {
                    tomorrowDay = 29;
                    tomorrowMonth = 2;
                }
                else if(currDay == 28 && currMonth != 2)
                {
                    tomorrowDay = 29;
                    tomorrowMonth = currMonth;
                }
                else if(currDay == 29 && currMonth != 2)
                {
                    tomorrowDay = 30;
                    tomorrowMonth = currMonth;
                }
                else if(currDay == 30 && !(currMonth == 4 || currMonth == 6 || currMonth == 9 || currMonth == 11))
                {
                    tomorrowDay = 31;
                    tomorrowMonth = currMonth;
                }
            }
        }
        else
        {
            tomorrowDay = currDay + 1;
            tomorrowMonth = currMonth;
            tomorrowYear = currYear;
        }


        if(currDay == 29 && currMonth == 2)
        {
            currDay = 28;
            yesterdayDay = 27;
        }

        if(yesterdayDay == 29 && yesterdayMonth == 2)
        {
            currDay = 1;
            currMonth = 3;
            yesterdayDay = 28;
        }

        if(tomorrowDay == 29 && tomorrowMonth == 2)
        {
            currDay = 1;
            currMonth = 3;
            tomorrowDay = 28;
        }



        for(int i = 0; i < currYear - 2015; i++)
        {


            yesterdayMean += Float.parseFloat(dataSnapshot.child( getStringDate(yesterdayDay) + "-" + getStringDate(yesterdayMonth) + "-" + startYear).getValue(String.class));

            todayMean += Float.parseFloat(dataSnapshot.child( getStringDate(currDay) + "-" + getStringDate(currMonth) + "-" + startYear).getValue(String.class));
            tomorrowMean += Float.parseFloat(dataSnapshot.child( getStringDate(tomorrowDay) + "-" + getStringDate(tomorrowMonth) + "-" + startYear).getValue(String.class));

            startYear++;
        }

        yesterdayMean = yesterdayMean/(float)4;
        todayMean = todayMean/(float)4;
        tomorrowMean = tomorrowMean/(float)4;
        diffMean = todayMean - yesterdayMean;



        float today = todayTemp;

        float yesterday = Float.parseFloat(dataSnapshot.child( getStringDate(yesterdayDay) + "-" + getStringDate(yesterdayMonth) + "-" + yesterdayYear).getValue(String.class));
        float diff =  today - yesterday;
        float error = diff - diffMean;

        float diffTomorrow = tomorrowMean - todayMean;

        return (today + yesterday + error)/2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);

        final String temp;
        Intent intent = getIntent();
        temp = intent.getStringExtra("temp");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("WeatherData");

        final TextView tv = findViewById(R.id.tw);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DATE);
                //Note: +1 the month for current month
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);

                tv.setText("Tomorrow's temperature :\n             " + predictWeather(dataSnapshot,day,month,year,Float.parseFloat(temp)) + "°C\n\n");


            }

            @Override
            public void onCancelled(DatabaseError error) {

                // Failed to read value

            }
        });

    }


}
