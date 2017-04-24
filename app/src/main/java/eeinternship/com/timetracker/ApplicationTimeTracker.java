package eeinternship.com.timetracker;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import io.fabric.sdk.android.Fabric;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import Data.BackupData;
import Data.ProfileDataDropdown;
import Data.ProfileDataLine;
import Data.Project;
import Data.Ticket;
import Data.UploadSpreadsheetData;
import Data.UserData;
import RESTtest.TestData;
import RESTtest.TestProject;
import RESTtest.TestWorkingOn;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class ApplicationTimeTracker extends Application {
    private static final String DATA_MAP = "TimeTracker";
    private static final String FILE_NAME = "UserData.json";
    private UserData userData;
    private BackupData backupData = new BackupData();
    private AlarmManager alarmManegerPerDay;
    private AlarmManager alarmManagerPerTime;
    private PendingIntent alarmRecvierForDay;
    private PendingIntent alramRevierPerTime;
    private ArrayList<Project> tempProjects;
    private Object lock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        if (userData == null) {
            userData = new UserData();
            if (readFromGson()) {
                userData.setUserAcount(backupData.getUserAcount());
                userData.setTicketList(backupData.ticketList);
                userData.addUploadRepository(backupData.uploadSpreadsheetData);
                userData.setNotificationData(backupData.notificationData);
                userData.addProjectList(backupData.projects);
                if(userData.getNotificationData().isSet())
                    startNotificationOnDay(userData.getNotificationData().isTurnOnOf());


            }

        }

        getActiveProjects(getApplicationContext());
       // checkForNewProjects();

       synchronized (lock){
           try {

               lock.wait(3000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }


       }

        userData.setProfileDataDropdownArrayList(getWorkDaysAndWorkingOn(getApplicationContext(),userData.getUserAcount()));

    }

    private void checkForNewProjects() {
        synchronized (lock){
            ArrayList<Project> newProjects = new ArrayList<>();
            for(Project tempProject : tempProjects){
                boolean duplicate = false;
                for(Project project:userData.getProjectList()){
                    if(project.projectName == tempProject.projectName){
                        duplicate = true;
                    }
                }
                if(duplicate){
                    newProjects.add(tempProject);
                }

            }
            ArrayList<Project> allProjects = userData.getProjectList();
            for(Project project:newProjects){
                allProjects.add(project);
            }
            userData.addProjectList(new ArrayList<Project>());
            userData.addProjectList(allProjects);
            lock.notify();
        }

    }


    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData data) {
        this.userData = data;
        backupData.setUserAcount(userData.getUserAcount());
        backupData.ticketList = userData.getTicketList();
        backupData.uploadSpreadsheetData = userData.getUploadSpreadsheetData();
        backupData.notificationData = userData.getNotificationData();
        backupData.projects = userData.getProjectList();
        saveInGson();

    }

    public void getActiveProjects(Context context) {
        Log.i("Running:", "Fetching active project list");
        final ArrayList<Project> projects = new ArrayList<>();
        if (isNetworkAvailable()) {
            Ion.with(context)
                    .load("GET", "https://nameless-oasis-70424.herokuapp.com/getactiveprojects/?format=json")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            if (result != null) {
                                Log.i("Info: Result size:", String.valueOf(result.size()));
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                for (int i = 0; i < result.size(); i++) {
                                    TestProject project = gson.fromJson(result.get(i), TestProject.class);
                                    if (project == null) {
                                        Log.e("Error", "Object is null!");
                                    } else {
                                        Log.i("Info:", i + " " + project.getProject_name());
                                        Project proj = new Project();
                                        proj.projectName = project.getProject_name();
                                        projects.add(proj);
                                    }
                                }
                                tempProjects=projects;
                                checkForNewProjects();
                            } else {
                                Log.e("Error", "Result is empty!");

                            }
                        }
                    });
        } else {
            Toast.makeText(context, "Network not available!", Toast.LENGTH_LONG).show();
        }



    }

    public ArrayList<ProfileDataDropdown> getWorkDaysAndWorkingOn(Context context, String email) {
        final ArrayList<ProfileDataDropdown> profileDataDropdownArrayList = new ArrayList<>();
        Log.i("Running:", "Fetching work days for user.");
        if (isNetworkAvailable()) {
            Ion.with(context)
                    .load("GET", "https://nameless-oasis-70424.herokuapp.com/getworkdaysandworkingon/" + email + "/?format=json")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            if (result != null) {
                                Log.i("Info: Result size:", String.valueOf(result.size()));
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
                                            profileDataLineArrayList.add(profileDataLine);
                                        }
                                        profileDataDropdown.setProfileDataLineArrayList(profileDataLineArrayList);
                                        profileDataDropdown.setDate(workday.getWork_day().getDate());
                                        profileDataDropdown.setTotalTime(workday.getWork_day().getWorking_hours(),workday.getWork_day().getOvertime());
                                        profileDataDropdownArrayList.add(profileDataDropdown);
                                    }
                                }
                            } else {
                                Log.e("Error", "Result is empty!");
                            }
                        }
                    });
        } else {
            Toast.makeText(context, "Network not available!", Toast.LENGTH_LONG).show();
            ;
        }
        return profileDataDropdownArrayList;
    }

    public void addWorkDay(final Context context, String email, UploadSpreadsheetData uploadSpreadsheetData) {
        Log.i("Running:", "Sending work day data.");
        Date date =  uploadSpreadsheetData.date.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String dateString = dateFormat.format(date);
        String startingTime = timeFormat.format(uploadSpreadsheetData.startingTime);
        String finishTime = timeFormat.format(uploadSpreadsheetData.finishTime);


        if (isNetworkAvailable()) {
            Ion.with(context)
                    .load("POST", "https://nameless-oasis-70424.herokuapp.com/workday/")
                    .setMultipartParameter("user.email", email)
                    .setMultipartParameter("date", dateString)
                    .setMultipartParameter("starting_time", startingTime)
                    .setMultipartParameter("finish_time", finishTime)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (result != null) {
                                Log.i("Info: ", result);
                            } else {
                                Log.e("Error: ", result);
                            }
                        }
                    });
        } else {
            Toast.makeText(context, "Network not available!", Toast.LENGTH_LONG).show();
        }
    }

    public void addWorkingOn(final Context context, String email, Ticket ticket) {
        Date date =  ticket.getDate().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String dateString = dateFormat.format(date);
        String startingTime = timeFormat.format(ticket.getStartingTime());
        String finishTime = timeFormat.format(ticket.getFinishTime());
        Log.i("Running:", "Sending work on data.");
        if (isNetworkAvailable()) {
            Ion.with(context)
                    .load("POST", "https://nameless-oasis-70424.herokuapp.com/workingon/")
                    .setMultipartParameter("user.email", email)
                    .setMultipartParameter("project.project_name", ticket.getProject())
                    .setMultipartParameter("date",dateString)
                    .setMultipartParameter("starting_time", startingTime)
                    .setMultipartParameter("finish_time", finishTime)
                    .setMultipartParameter("description", ticket.getDescription())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (result != null) {
                                Log.i("Info: ", result);
                            } else {
                                Log.e("Error: ", result);
                            }
                        }
                    });
        } else {
            Toast.makeText(context, "Network not available!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean saveInGson() {
        return saveInGson(backupData, FILE_NAME);
    }

    public boolean readFromGson() {
        BackupData tmp = readFromGson(FILE_NAME);
        if (tmp != null) backupData = tmp;
        else return false;
        return true;
    }


    private boolean saveInGson(BackupData a, String filename) {
        if (isExternalStorageWritable()) {
            File file = new File(this.getExternalFilesDir(DATA_MAP), ""
                    + filename);
            try {
                long start = System.currentTimeMillis();
                System.out.println("Save " + file.getAbsolutePath() + " " + file.getName());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                PrintWriter pw = new PrintWriter(file);
                String sss = gson.toJson(a);
                System.out.println("Save time gson:" + (double) (System.currentTimeMillis() - start) / 1000);
                pw.println(sss);
                pw.close();
                System.out.println("Save time s:" + (double) (System.currentTimeMillis() - start) / 1000);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Error saveInGson! (FileNotFoundException)");
            } catch (IOException e) {
                System.out.println("Error saveInGson! (IOException)");
            }
        } else {
            System.out.println(this.getClass().getCanonicalName() + " NOT Writable");
        }
        return false;
    }

    private BackupData readFromGson(String name) {
        if (isExternalStorageReadable()) {
            try {
                File file = new File(this.getExternalFilesDir(DATA_MAP), "" + name);
                System.out.println("Load " + file.getAbsolutePath() + " " + file.getName());
                FileInputStream fstream = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuffer sb = new StringBuffer();
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    sb.append(strLine).append('\n');
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                BackupData a = gson.fromJson(sb.toString(), BackupData.class);
                if (a == null) {
                    System.out.println("Error: fromJson Format error");
                } else {
                    System.out.println(a.toString());
                }
                return a;
            } catch (IOException e) {
                System.out.println("Error readFromGson " + e.toString());
            }
        }
        System.out.println("ExternalStorageAvailable is not avaliable");
        return null;
    }


    public void startNotificationOnDay(boolean work){

        if(!work)
            return;

        Calendar firingCal= Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        Time time = userData.getNotificationData().getNotificationStartTime();
        firingCal.set(Calendar.HOUR_OF_DAY, time.getHours()); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, time.getMinutes()); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second
        long intendedTime = firingCal.getTimeInMillis();
        long timeNow = currentTime.getTimeInMillis();


        Intent myIntent = new Intent(this , AlarmReciverForDay.class);
        alarmManegerPerDay = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmRecvierForDay = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        if(timeNow > intendedTime){
            firingCal.add(Calendar.DAY_OF_MONTH,1);
            intendedTime = firingCal.getTimeInMillis();
            alarmManegerPerDay.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, alarmRecvierForDay);
        }else{
            alarmManegerPerDay.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, alarmRecvierForDay);
        }


    }

    public void startNotificationPerMinutes(boolean work){
        if(!work)
            return;
        Time time = userData.getNotificationData().getNotificationPopupTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
       // cal.add(Calendar.MILLISECOND, (int) time.getTime());
        long currentTimeMillis = cal.getTimeInMillis();
        long notificationOnTimeMillis = time.getTime();
        int hourPlusMinutes = time.getHours()*60 + time.getMinutes();
        cal.add(Calendar.MILLISECOND,hourPlusMinutes * 60 * 1000);
        Intent myIntent = new Intent(this , AlarmReciverPerTime.class);
        alarmManagerPerTime = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alramRevierPerTime = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManagerPerTime.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*60*hourPlusMinutes, alramRevierPerTime);

    }

    public void cancelNotificationPerDay(){
        if(alarmManegerPerDay != null)
            alarmManegerPerDay.cancel(alarmRecvierForDay);
    }
    public void cancelNotificationPerMinute(){

        if(alarmManagerPerTime != null)
            alarmManagerPerTime.cancel(alramRevierPerTime);
}



}
