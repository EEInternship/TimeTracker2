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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import Data.ProfileDataDropdown;
import Data.ProfileDataLine;
import Data.UserData;
import RESTtest.TestData;
import RESTtest.TestWorkingOn;

public class ProfileActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PROFILE");

        // action bar color
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#323232")));

        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        resultOfCall=getWorkDaysAndWorkingOn(getApplicationContext(),userData.getUserAcount());

        expListView = (ExpandableListView) findViewById(R.id.expandle_listview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resultOfCall=getWorkDaysAndWorkingOn(getApplicationContext(),userData.getUserAcount());
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
                startActivityForResult(settingsAC,88);
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
            Log.i("Choosen accountName:", accountName);
            userData = new UserData();
            userData.setUserAcount(accountName);
            applicationTimeTracker.setUserData(userData);
            applicationTimeTracker.setAllData();
        }
        if(requestCode == 88){
            applicationTimeTracker.setColors();
            listAdapter.notifyDataSetChanged();
        }
    }

    public ArrayList<ProfileDataDropdown> getWorkDaysAndWorkingOn(Context context, String email) {
        resultOfCall = new ArrayList<>();
        Log.i("Running:", "Fetching work days for user.");
        if (applicationTimeTracker.isNetworkAvailable()) {
            Ion.with(context)
                    .load("GET", "https://nameless-oasis-70424.herokuapp.com/getworkdaysandworkon/" + email + "/?format=json")
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
                                        for(TestWorkingOn testWorkingOn : workday.getWork_on()){
                                            ProfileDataLine profileDataLine = new ProfileDataLine();
                                            profileDataLine.setProjectName(testWorkingOn.getProject().getProject_name());
                                            profileDataLine.setStartingTime(testWorkingOn.getStarting_time());
                                            profileDataLine.setFinishTime(testWorkingOn.getFinish_time());
                                            profileDataLine.setWorkDescription(testWorkingOn.getDescription());
                                            profileDataLine.setWorkTime(testWorkingOn.getWorking_hours());
                                            profileDataLineArrayList.add(profileDataLine);
                                        }
                                        profileDataDropdown.setProfileDataLineArrayList(profileDataLineArrayList);
                                        profileDataDropdown.setDate(workday.getWork_day().getDate());
                                        profileDataDropdown.setTotalTime(workday.getWork_day().getWorking_hours(),workday.getWork_day().getOvertime());
                                        resultOfCall.add(profileDataDropdown);
                                    }
                                }
                                Log.i("Profile:Array size",String.valueOf(resultOfCall.size()));
                                swipeRefreshLayout.setRefreshing(false);
                                listAdapter = new ExpandableListAdapter(getApplicationContext(),resultOfCall);
                                expListView.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("Error", "Result is empty!");
                            }

                        }
                    });
        } else {
            Toast.makeText(context, "Network not available!", Toast.LENGTH_LONG).show();
        }
        return resultOfCall;
    }
}
