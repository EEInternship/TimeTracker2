package eeinternship.com.timetracker;

import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    String accountName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationTimeTracker = (ApplicationTimeTracker)getApplication();
        userData = applicationTimeTracker.getUserData();
        uploadSpreadsheetData = userData.getUploadSpreadsheetData();




        if(!userData.userAccountIsSet()){
            chooseAccount();
        }
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

                if(userData.getNotificationData().isSet())
                    applicationTimeTracker.startNotificationPerMinutes(userData.getNotificationData().isTurnOnOf());
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

    public void chooseAccount(){
        Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999&& resultCode == RESULT_OK) {
            accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.i("Choosen accountName:", accountName);
            userData.setUserAcount(accountName);
            applicationTimeTracker.setUserData(userData);
            applicationTimeTracker.saveInGson();
            //applicationTimeTracker.getActiveProjects(getApplicationContext());
            //applicationTimeTracker.getWorkDaysAndWorkingOn(getApplicationContext(),accountName);
            //applicationTimeTracker.addWorkDay(getApplicationContext(),accountName);
            //applicationTimeTracker.addWorkingOn(getApplicationContext(),accountName);
        }
    }




}
