package eeinternship.com.timetracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
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

    private Button btnOpen,btnStartWork,btnProfile;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationTimeTracker = (ApplicationTimeTracker)getApplication();
        userData = applicationTimeTracker.getUserData();
        uploadSpreadsheetData = userData.getUploadSpreadsheetData();

        applicationTimeTracker.getActiveProjects(getApplicationContext());
        applicationTimeTracker.getWorkDays(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBackground));
        }

        btnOpen=(Button)findViewById(R.id.btn_open_door);
        btnStartWork=(Button)findViewById(R.id.btn_start_work);
        btnProfile=(Button)findViewById(R.id.btn_profile);

        // new window StartWork
        btnStartWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                int cHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
                int cMinute = calender.get(Calendar.MINUTE);
                uploadSpreadsheetData.startingTime = new Time(cHourOfDay,cMinute,00);
                uploadSpreadsheetData.date = calender;
                userData.addUploadRepository(uploadSpreadsheetData);
                applicationTimeTracker.setUserData(userData);

                Intent startWorkActivity=new Intent(getApplication(),StartWorkActivity.class);
                startActivity(startWorkActivity);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileActivity=new Intent(getApplication(),ProfileActivity.class);
                startActivity(profileActivity);
            }
        });
    }
}
