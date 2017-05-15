package eeinternship.com.timetracker;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.sql.Time;
import java.util.ArrayList;

import Data.ProfileDataDropdown;
import Data.ProfileDataLine;
import Data.UserData;
import RESTtest.TestData;
import RESTtest.TestWorkingOn;

import static android.graphics.Color.parseColor;
import static android.widget.Toast.LENGTH_LONG;

public class ProfileActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;
    private ProfileActivity profileActivity;
    ArrayList<ProfileDataDropdown> resultOfCall;
    SwipeRefreshLayout swipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        applicationTimeTracker = (ApplicationTimeTracker) getApplication();
        userData = applicationTimeTracker.getUserData();

        applicationTimeTracker.setColors();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PROFILE");

        // action bar color
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#323232")));

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        profileActivity = this;
        getWorkDaysAndWorkingOn(getApplicationContext(), userData.getUserAcount());

        expListView = (ExpandableListView) findViewById(R.id.expandle_listview);
        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    openEditDialog(position);
                }

                return false;
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addWorkDays(getApplicationContext(), userData.getUserAcount());
            }


        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.notification_btn:
                Intent notificationAC = new Intent(this, NotificationActivity.class);
                startActivity(notificationAC);
                break;
            case R.id.settings_btn:
                Intent settingsAC = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsAC, 88);
                break;
            case R.id.account_picker:
                chooseAccount();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    public void chooseAccount() {
        Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, 999);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            if (!accountName.equals(userData.getUserAcount())) {
                Log.i("Choosen accountName:", accountName);
                userData = new UserData();
                userData.setUserAcount(accountName);
                applicationTimeTracker.setUserData(userData);
                applicationTimeTracker.setAllData();
            }
        }
        if (requestCode == 88) {
            applicationTimeTracker.setColors();
            userData = applicationTimeTracker.getUserData();
            listAdapter.notifyDataSetChanged();
        }
    }
    public void openEditDialog(int groupPosition){

        final ProfileActivity profileActivity = this;

        LayoutInflater infalInflater = (LayoutInflater) profileActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View alertLayout = infalInflater.inflate(R.layout.edit_dialog_group, null);
        AlertDialog.Builder editDialog = new AlertDialog.Builder(profileActivity);
        editDialog.setView(alertLayout);
        final ProfileDataDropdown profileDataDropdown =userData.getProfileDataDropdownArrayList().get(groupPosition);


        final TimePicker timePicker = (TimePicker) alertLayout.findViewById(R.id.time_choose);
        timePicker.setIs24HourView(true);

        final TextView date = (TextView) alertLayout.findViewById(R.id.project_name_edit);


        date.setText(profileDataDropdown.getDate());


        final Time startingTime = getTime(profileDataDropdown.getStartingTime());
        final Time finishTime = getTime(profileDataDropdown.getFinishTime());


        final Time startingChangedTime = getTime(profileDataDropdown.getStartingTime());
        final Time finishChangedTime = getTime(profileDataDropdown.getFinishTime());


        timePicker.setCurrentHour(startingChangedTime.getHours());
        timePicker.setCurrentMinute(startingChangedTime.getMinutes());
        final TextView labelStartingFinish = (TextView) alertLayout.findViewById(R.id.starting_finsihed_time);
        final SwitchCompat pickTime = (SwitchCompat) alertLayout.findViewById(R.id.select_time);
        Button saveBtn = (Button) alertLayout.findViewById(R.id.btn_save_edit);


        pickTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (pickTime.isChecked()) {
                    labelStartingFinish.setText("FINISHED TIME");
                    labelStartingFinish.setTextColor(parseColor("#f1490b"));
                    startingChangedTime.setHours(timePicker.getCurrentHour());
                    startingChangedTime.setMinutes(timePicker.getCurrentMinute());
                    timePicker.setCurrentHour(finishChangedTime.getHours());
                    timePicker.setCurrentMinute(finishChangedTime.getMinutes());
                } else {
                    labelStartingFinish.setText("STARTING TIME");
                    labelStartingFinish.setTextColor(parseColor("#04b795"));
                    finishChangedTime.setHours(timePicker.getCurrentHour());
                    finishChangedTime.setMinutes(timePicker.getCurrentMinute());
                    timePicker.setCurrentHour(startingChangedTime.getHours());
                    timePicker.setCurrentMinute(startingChangedTime.getMinutes());
                }
            }
        });
        final AlertDialog dialog = editDialog.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickTime.isChecked()) {
                    finishChangedTime.setHours(timePicker.getCurrentHour());
                    finishChangedTime.setMinutes(timePicker.getCurrentMinute());
                } else {
                    startingChangedTime.setHours(timePicker.getCurrentHour());
                    startingChangedTime.setMinutes(timePicker.getCurrentMinute());
                }


                getTime(finishChangedTime.toString());
                profileDataDropdown.setFinishTime(timeToString(finishChangedTime.toString()));
                profileDataDropdown.setStartingTime(timeToString(startingChangedTime.toString()));
                applicationTimeTracker.updateWorkDay(profileActivity,userData.getUserAcount(),profileDataDropdown,listAdapter);
                listAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });


    }
    private String timeToString(String time){

        String[] array;
        array = time.split(":");
        return array[0] + ":"+array[1];

    }


    private Time getTime(String time){
        String[] array = new String[2];
        array = time.split(":");
        Time timer = new Time(Integer.parseInt(array[0]),Integer.parseInt(array[1]),0);
        return timer;
    }


    public void getWorkDaysAndWorkingOn(Context context, String email) {
        resultOfCall = new ArrayList<>();
        Log.i("Running:", "Fetching work days for user.");
        if (applicationTimeTracker.isNetworkAvailable()) {
            Ion.with(context)
                    .load("GET", "https://nameless-oasis-70424.herokuapp.com/getworkdaysandworkon/" + email)// + "/?format=json")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            if (result != null) {
                                Log.i("Profile:Result size:", String.valueOf(result.size()));
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                for (int i = 0; i < result.size(); i++) {
                                    ProfileDataDropdown profileDataDropdown = new ProfileDataDropdown();
                                    TestData workday = gson.fromJson(result.get(i), TestData.class);
                                    if (workday == null) {
                                        Log.e("Error", "Object is null!");
                                    } else {
                                        Log.i("Info", i + " " + workday.toString());
                                        ArrayList<ProfileDataLine> profileDataLineArrayList = new ArrayList<>();
                                        for (TestWorkingOn testWorkingOn : workday.getWork_on()) {
                                            ProfileDataLine profileDataLine = new ProfileDataLine();
                                            profileDataLine.setProjectName(testWorkingOn.getProject().getProject_name());
                                            profileDataLine.setStartingTime(testWorkingOn.getStarting_time());
                                            profileDataLine.setFinishTime(testWorkingOn.getFinish_time());
                                            profileDataLine.setWorkDescription(testWorkingOn.getDescription());
                                            profileDataLine.setWorkTime(testWorkingOn.getWorking_hours());
                                            profileDataLine.setId(testWorkingOn.getPk());
                                            profileDataLine.setDate(workday.getWork_day().getDate());
                                            profileDataLineArrayList.add(profileDataLine);
                                        }
                                        profileDataDropdown.setProfileDataLineArrayList(profileDataLineArrayList);
                                        profileDataDropdown.setDate(workday.getWork_day().getDate());
                                        profileDataDropdown.setStartingTime(workday.getWork_day().getStarting_time());
                                        profileDataDropdown.setFinishTime(workday.getWork_day().getFinish_time());
                                        profileDataDropdown.setId(workday.getWork_day().getPk());
                                        profileDataDropdown.setTotalTime(workday.getWork_day().getWorking_hours(), workday.getWork_day().getOvertime());
                                        resultOfCall.add(profileDataDropdown);
                                    }
                                }
                                Log.i("Profile:Array size", String.valueOf(resultOfCall.size()));
                                userData.setProfileDataDropdownArrayList(resultOfCall);
                                swipeRefreshLayout.setRefreshing(false);
                                applicationTimeTracker.setColors();
                                listAdapter = new ExpandableListAdapter(profileActivity, userData.getProfileDataDropdownArrayList());
                                expListView.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            } else {
                                Log.e("Error", "Result is empty!");
                            }

                        }
                    });
        } else {
            //Toast.makeText(context, "Network not available!", Toast.LENGTH_LONG).show();
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_dialog_no_internet,
                    (ViewGroup) findViewById(R.id.custom_toast_container));


            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("Network not available!");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }




    public void addWorkDays(Context context, String email) {
        resultOfCall = new ArrayList<>();
        Log.i("Running:", "Fetching work days for user.");
        if (applicationTimeTracker.isNetworkAvailable()) {
            Ion.with(context)
                    .load("GET", "https://nameless-oasis-70424.herokuapp.com/getworkdaysandworkon/" + email)// + "/?format=json")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            if (result != null) {
                                Log.i("Profile:Result size:", String.valueOf(result.size()));
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                for (int i = 0; i < result.size(); i++) {
                                    ProfileDataDropdown profileDataDropdown = new ProfileDataDropdown();
                                    TestData workday = gson.fromJson(result.get(i), TestData.class);
                                    if (workday == null) {
                                        Log.e("Error", "Object is null!");
                                    } else {
                                        Log.i("Info", i + " " + workday.toString());
                                        ArrayList<ProfileDataLine> profileDataLineArrayList = new ArrayList<>();
                                        for (TestWorkingOn testWorkingOn : workday.getWork_on()) {
                                            ProfileDataLine profileDataLine = new ProfileDataLine();
                                            profileDataLine.setProjectName(testWorkingOn.getProject().getProject_name());
                                            profileDataLine.setStartingTime(testWorkingOn.getStarting_time());
                                            profileDataLine.setFinishTime(testWorkingOn.getFinish_time());
                                            profileDataLine.setWorkDescription(testWorkingOn.getDescription());
                                            profileDataLine.setWorkTime(testWorkingOn.getWorking_hours());
                                            profileDataLine.setId(testWorkingOn.getPk());
                                            profileDataLine.setDate(workday.getWork_day().getDate());
                                            profileDataLineArrayList.add(profileDataLine);
                                        }
                                        profileDataDropdown.setProfileDataLineArrayList(profileDataLineArrayList);
                                        profileDataDropdown.setDate(workday.getWork_day().getDate());
                                        profileDataDropdown.setStartingTime(workday.getWork_day().getStarting_time());
                                        profileDataDropdown.setFinishTime(workday.getWork_day().getFinish_time());
                                        profileDataDropdown.setId(workday.getWork_day().getPk());
                                        profileDataDropdown.setTotalTime(workday.getWork_day().getWorking_hours(), workday.getWork_day().getOvertime());
                                        resultOfCall.add(profileDataDropdown);
                                    }
                                }
                                Log.i("Profile:Array size", String.valueOf(resultOfCall.size()));
                                userData.setProfileDataDropdownArrayList(resultOfCall);
                                applicationTimeTracker.setUserData(userData);
                                applicationTimeTracker.setColors();
                                userData = applicationTimeTracker.getUserData();
                                listAdapter = new ExpandableListAdapter(profileActivity, userData.getProfileDataDropdownArrayList());
                                swipeRefreshLayout.setRefreshing(false);
                                expListView.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("Error", "Result is empty!");
                            }

                        }
                    });
        } else {
            // Toast.makeText(context, "Network not available!", Toast.LENGTH_LONG).show();
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_dialog_no_internet,
                    (ViewGroup) findViewById(R.id.custom_toast_container));


            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("Network not available!");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }
}
