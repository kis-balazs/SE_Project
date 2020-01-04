package com.androdocs.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);


        final LineChart[] mChart = {findViewById(R.id.linechart)};
        final EditText start_date = findViewById(R.id.start_date);
        final EditText end_date = findViewById(R.id.end_date);
        final Button plot = findViewById(R.id.plotButton);

        mChart[0].setDragEnabled(true);
        mChart[0].setScaleEnabled(true);



        plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean[] goodInput = {true};
                final String startDate = start_date.getText().toString();
                final String endDate = end_date.getText().toString();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("WeatherData");

                // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Float> vals = new ArrayList<>();

                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                        Date startD = null;
                        Date endD = null;
                        try {
                            startD = formatter.parse(startDate);
                            endD = formatter.parse(endDate);
                        } catch (ParseException | NullPointerException e) {
                            e.printStackTrace();
                        }

                        long interval =  1000 * 60 * 60 * 24 ; // a day
                        long endTime = (endD != null) ? endD.getTime() : 0;
                        long curTime = (startD != null) ? startD.getTime() : 0;

                        final HashMap<Integer, String> numMap = new HashMap<>();
                        int i = 0;

                        if(curTime == endTime)
                            goodInput[0] = false;
                        else {
                            while (curTime <= endTime) {
                                Date d = new Date(curTime);
                                numMap.put(i, formatter.format(d.getTime()));
                                i++;
                                String v = dataSnapshot.child(formatter.format(d.getTime())).getValue(String.class);
                                try {
                                    vals.add(Float.parseFloat(v));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                if (v == null)
                                    goodInput[0] = false;
                                curTime += interval;
                            }
                        }

                        // add the values to the graphic
                        ArrayList<Entry> values = new ArrayList<>();

                        if (goodInput[0] && vals.size() != 0) {
                            i = 0;
                            for(float val : vals) {
                                values.add(new Entry(i, val));
                                i++;
                            }

                            LineDataSet set = new LineDataSet(values, "Temperatures between " + startDate + " and " + endDate);

                            set.setFillAlpha(110);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(set);

                            LineData ld = new LineData(dataSets);

                            XAxis xAxis = mChart[0].getXAxis();
                            xAxis.setValueFormatter(new IAxisValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                    return numMap.get((int)value);
                                }
                            });
                            // make visible labels, as clear as possible
                            xAxis.setLabelCount(i);
                            xAxis.setCenterAxisLabels(false);
                            xAxis.setGranularity(1f);
                            xAxis.setLabelRotationAngle(-90f);

                            mChart[0].setData(ld);
                        } else
                            Toast.makeText(getApplicationContext(),
                                    "INPUT ERROR!",
                                    Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) { }
                });
            }
        });

    }
}