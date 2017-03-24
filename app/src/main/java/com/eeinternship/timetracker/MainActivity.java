package com.eeinternship.timetracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.sql.Time;
import java.util.Calendar;

import Data.UploadSpreadsheetData;
import Data.UserData;

public class MainActivity extends AppCompatActivity {

    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;
    private UploadSpreadsheetData uploadSpreadsheetData;

    private Button btnStartWork;
    private Button btnMonthlyOverview;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationTimeTracker = (ApplicationTimeTracker) getApplication();
        userData = applicationTimeTracker.getUserData();
        uploadSpreadsheetData = new UploadSpreadsheetData();

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
        }

        // btnStartWork
        btnStartWork=(Button)findViewById(R.id.button_start_work);
        btnStartWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //INPUT START TIME
                Calendar calender = Calendar.getInstance();
                int cHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
                int cMinute = calender.get(Calendar.MINUTE);

                Time time = new Time(cHourOfDay,cMinute,00);
                uploadSpreadsheetData.startingTime = time;
                uploadSpreadsheetData.date = calender;
                userData.addUploadRepository(uploadSpreadsheetData);
                applicationTimeTracker.setUserData(userData);

                Intent startWorkActivity=new Intent(getApplication(),StartWorkActivity.class);
                startActivity(startWorkActivity);
            }
        });

        btnMonthlyOverview = (Button) findViewById(R.id.button_monthly_overview);
        btnMonthlyOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monthlyOverviewActivity = new Intent(getApplication(),MonthlyOverview.class);
                startActivity(monthlyOverviewActivity);
            }
        });

    }
}
