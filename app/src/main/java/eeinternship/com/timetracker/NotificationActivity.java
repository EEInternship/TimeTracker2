package eeinternship.com.timetracker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.sql.Time;

import Data.NotificationData;
import Data.UserData;

public class NotificationActivity extends AppCompatActivity {

    private TimePicker notificationStartingTimePicker, notificationShowOnTimePicker;
    ExpandableLayout expandableLayoutTime, expandableLayoutTimer;
    TextView linearLayoutTxtTimer, onOffTxt;
    Switch notificationTurnOnOff;
    private Button save,cancel;
    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;
    private NotificationData notificationData;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        applicationTimeTracker = (ApplicationTimeTracker) getApplication();
        userData = applicationTimeTracker.getUserData();
        notificationData = userData.getNotificationData();

        save = (Button) findViewById(R.id.btn_save_time);
        cancel=(Button)findViewById(R.id.btn_cancel_time);

        ActionBar actionBar = getSupportActionBar();
        // action bar color
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#323232")));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("NOTIFICATION");

        notificationShowOnTimePicker = (TimePicker) findViewById(R.id.timer);
        notificationShowOnTimePicker.setIs24HourView(true);

        notificationStartingTimePicker = (TimePicker) findViewById(R.id.time_picker);
        notificationStartingTimePicker.setIs24HourView(true);

        notificationTurnOnOff = (Switch) findViewById(R.id.switch_on_off);
        onOffTxt = (TextView) findViewById(R.id.on_off_txt);


        if(notificationData.isSet()){
            notificationShowOnTimePicker.setCurrentHour(notificationData.getNotificationPopupTime().getHours());
            notificationShowOnTimePicker.setCurrentMinute(notificationData.getNotificationPopupTime().getMinutes());
            notificationStartingTimePicker.setCurrentHour(notificationData.getNotificationStartTime().getHours());
            notificationStartingTimePicker.setCurrentMinute(notificationData.getNotificationStartTime().getMinutes());
            notificationTurnOnOff.setChecked(notificationData.isTurnOnOf());
            if(notificationData.isTurnOnOf()==true){
                onOffTxt.setText("On");
                onOffTxt.setTextColor(Color.parseColor("#04b795"));
            }else{
                onOffTxt.setText("Off");
                onOffTxt.setTextColor(Color.parseColor("#f1490b"));
            }

        }else{
            notificationShowOnTimePicker.setCurrentHour(0);
            notificationShowOnTimePicker.setCurrentMinute(45);
            notificationStartingTimePicker.setCurrentHour(8);
            notificationStartingTimePicker.setCurrentMinute(0);
            notificationTurnOnOff.setChecked(false);
            onOffTxt.setText("Off");
            onOffTxt.setTextColor(Color.parseColor("#f1490b"));
        }


        expandableLayoutTime = (ExpandableLayout) findViewById(R.id.time_picker_layout_ex);
        expandableLayoutTimer = (ExpandableLayout) findViewById(R.id.timer_layout_ex);
        linearLayoutTxtTimer = (TextView) findViewById(R.id.timer_text);

        linearLayoutTxtTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen == false) {
                    expandableLayoutTime.expand();
                    expandableLayoutTimer.collapse();
                    isOpen = true;
                } else if (isOpen == true) {
                    expandableLayoutTimer.expand();
                    expandableLayoutTime.collapse();
                    isOpen = false;
                }
            }
        });


        notificationTurnOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    onOffTxt.setText("On");
                    onOffTxt.setTextColor(Color.parseColor("#04b795"));
                } else {
                    onOffTxt.setText("Off");
                    onOffTxt.setTextColor(Color.parseColor("#f1490b"));
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationData = new NotificationData();
                notificationData.setTurnOnOf(notificationTurnOnOff.isChecked());
                notificationData.setNotificationStartTime(new Time(notificationStartingTimePicker.getCurrentHour(),notificationStartingTimePicker.getCurrentMinute(),0));
                notificationData.setNotificationPopupTime(new Time(notificationShowOnTimePicker.getCurrentHour(),notificationShowOnTimePicker.getCurrentMinute(),0));
                userData.setNotificationData(notificationData);
                applicationTimeTracker.setUserData(userData);
                applicationTimeTracker.startNotificationOnDay(userData.getNotificationData().isTurnOnOf());
                applicationTimeTracker.startNotificationPerMinutes(userData.getNotificationData().isTurnOnOf());
                if(!notificationData.isTurnOnOf()){
                    applicationTimeTracker.cancelNotificationPerDay();
                    applicationTimeTracker.cancelNotificationPerMinute();
                }
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
