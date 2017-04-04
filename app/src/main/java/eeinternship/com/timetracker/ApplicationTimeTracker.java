package eeinternship.com.timetracker;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import Data.UserData;
import RESTtest.TestData;
import RESTtest.TestProject;


public class ApplicationTimeTracker extends Application{
    private UserData userData;

    @Override
    public void onCreate() {
        super.onCreate();
        if (userData == null){
            userData = new UserData();
        }
        userData.scenariData();
    }

    public UserData getUserData(){
        return userData;
    }

    public void setUserData(UserData data) {
        this.userData = data;
    }

    public void getActiveProjects(Context context){
        Log.i("Running:", "Fetching active project list");
        if(isNetworkAvailable()){
            Ion.with(context)
                    .load("GET","https://nameless-oasis-70424.herokuapp.com/getactiveprojects/?format=json")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            if(result != null){
                                Log.i("Info: Result size:",String.valueOf(result.size()));
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                for(int i=0;i<result.size();i++) {
                                    TestProject project = gson.fromJson(result.get(i), TestProject.class);
                                    if (project == null) {
                                        Log.e("Error","Object is null!");
                                    } else {
                                        Log.i("Info:",i+" "+project.getProject_name());
                                    }
                                }
                            } else {
                                Log.e("Error","Result is empty!");
                            }
                        }
                    });
        } else {
            Toast.makeText(context,"Network not available!",Toast.LENGTH_LONG).show();

        }

    }

    public void getWorkDaysAndWorkingOn(Context context){
        Log.i("Running:", "Fetching work days for user.");
        if(isNetworkAvailable()){
            Ion.with(context)
                    .load("GET","https://nameless-oasis-70424.herokuapp.com/getworkingon/test@test.com/?format=json")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            if(result != null){
                                Log.i("Info: Result size:",String.valueOf(result.size()));
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                for(int i=0;i<result.size();i++) {
                                    TestData workday = gson.fromJson(result.get(i), TestData.class);
                                    if (workday == null) {
                                        Log.e("Error","Object is null!");
                                    } else {
                                        Log.i("Info",i+" "+workday.toString());
                                    }
                                }
                            } else {
                                Log.e("Error","Result is empty!");
                            }
                        }
                    });
        } else {
            Toast.makeText(context,"Network not available!",Toast.LENGTH_LONG).show();;
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
