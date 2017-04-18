package eeinternship.com.timetracker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import net.cachapa.expandablelayout.ExpandableLayout;

public class NotificationActivity extends AppCompatActivity {

    private TimePicker notificationStartingTimePicker, notificationShowOnTimePicker;
    ExpandableLayout expandableLayoutTime, expandableLayoutTimer;
    TextView linearLayoutTxtTimer, onOffTxt;
    Switch notificationTurnOnOff;

    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ActionBar actionBar = getSupportActionBar();
        // action bar color
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#323232")));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("NOTIFICATION");

        notificationShowOnTimePicker = (TimePicker) findViewById(R.id.timer);
        notificationShowOnTimePicker.setIs24HourView(true);
        notificationShowOnTimePicker.setCurrentHour(0);
        notificationShowOnTimePicker.setCurrentMinute(45);


        notificationStartingTimePicker = (TimePicker) findViewById(R.id.time_picker);
        notificationStartingTimePicker.setIs24HourView(true);
        notificationStartingTimePicker.setCurrentHour(8);
        notificationStartingTimePicker.setCurrentMinute(0);

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

        notificationTurnOnOff = (Switch) findViewById(R.id.switch_on_off);
        onOffTxt = (TextView) findViewById(R.id.on_off_txt);
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
