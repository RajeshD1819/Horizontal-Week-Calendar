package com.fosterbin.horizontalweekcalendar_demo;

import android.os.Bundle;
import android.widget.Toast;

import com.fosterbin.horizontalweekcalendar.HWeekCalendar;
import com.fosterbin.horizontalweekcalendar.OnWeekCalendarClick;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    HWeekCalendar hWeekCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hWeekCalendar = findViewById(R.id.hWeekCalendar);
        hWeekCalendar.setOnWeekCalendarClick(new OnWeekCalendarClick() {
            @Override
            public void onDateClick(DateTime date) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
                Toast.makeText(MainActivity.this, formatter.print(date), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
