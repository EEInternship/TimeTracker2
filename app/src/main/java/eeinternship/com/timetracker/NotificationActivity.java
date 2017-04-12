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

    private TimePicker timePicker,timerPicker;
    ExpandableLayout expandableLayoutTime, expandableLayoutTimer;
    TextView linearLayoutTxtTimer,onOffTxt;
    Switch onOff;

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

        timerPicker=(TimePicker)findViewById(R.id.timer);
        timerPicker.setIs24HourView(true);

        timePicker=(TimePicker)findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        expandableLayoutTime = (ExpandableLayout) findViewById(R.id.time_picker_layout_ex);
        expandableLayoutTimer = (ExpandableLayout) findViewById(R.id.timer_layout_ex);
        linearLayoutTxtTimer = (TextView) findViewById(R.id.timer_text);

        linearLayoutTxtTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen == false) {
                    expandableLayoutTime.expand();
                    expandableLayoutTimer.collapse();
                    isOpen=true;
                }else if(isOpen==true){
                    expandableLayoutTimer.expand();
                    expandableLayoutTime.collapse();
                    isOpen=false;
                }
            }
        });

        onOff=(Switch)findViewById(R.id.switch_on_off);
        onOffTxt=(TextView)findViewById(R.id.on_off_txt);
        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    onOffTxt.setText("On");
                    onOffTxt.setTextColor(Color.parseColor("#04b795"));
                }else{
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
