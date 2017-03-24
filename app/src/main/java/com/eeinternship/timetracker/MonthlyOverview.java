package com.eeinternship.timetracker;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import Data.DownloadSpreadsheetData;
import Data.UserData;
import pub.devrel.easypermissions.EasyPermissions;

public class MonthlyOverview extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_AUTHORIZATION = 2;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 3;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 4;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };

    private ApplicationTimeTracker applicationTimeTracker;
    private UserData userData;
    private String[] namesOfMonths;
    private Calendar currentDate;


    private RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_overview);

        applicationTimeTracker = (ApplicationTimeTracker)getApplication();
        userData = applicationTimeTracker.getUserData();
        namesOfMonths = new String[]{"Januar", "Februar", "Marec", "April","Maj","Junij","Julij","Avgust","September","Oktober","November","December"};
        currentDate= Calendar.getInstance();


        // status bar color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //  window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar)); - Moj komentar - Prestar OS
        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
        mCredential=GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        if(userData.getUserAcount() != null)
                mCredential.setSelectedAccount(userData.getUserAcount());
        getResultsFromApi();
    }
    private void getResultsFromApi()
    {
        if(!isGooglePlayServicesAvailable()) {
            //checks if Google Play Services are available,
            //if they are not available, acquire them
            acquireGooglePlayServices();
        } else if(mCredential.getSelectedAccountName()==null){
            //checks the current selected account which will perform the action
            //if no account is found it opens up the account picker
            chooseAccount();
        } else if (!isDeviceOnline()) {
            //checks if device has internet connection
        } else {
            //if everything is OK, execute the Read request for the sheet
            new MakeRequestRead(mCredential).execute();
        }

    }

    //Function that checks if the Play Service is Available
    private boolean isGooglePlayServicesAvailable(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    //Function that acquires the Play Services if they are not available
    private void acquireGooglePlayServices(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (googleApiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServiceAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    //Called when the acquireGooglePlayServices runs into problems
    void showGooglePlayServiceAvailabilityErrorDialog(int connectionStatusCode){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = googleApiAvailability.getErrorDialog(
                MonthlyOverview.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES
        );
        dialog.show();
    }

    //Function that triggers the Account picker.
    private void chooseAccount(){
        if(EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)){
            startActivityForResult(mCredential.newChooseAccountIntent(),REQUEST_ACCOUNT_PICKER);
        }
        else
        {
            EasyPermissions.requestPermissions(this,"This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }
    //Function that checks if the device is connected to the internet.

    private boolean isDeviceOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    // textViewMonthlyOverview.setText("This app requires Google Play Services. Please install Google Play Servies and re-launch this app!");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        userData.setUserAcount(mCredential.getSelectedAccount());
                        applicationTimeTracker.setUserData(userData);
                        Log.i("MonthlyOverviewActivity","Name of selected account: "+mCredential.getSelectedAccountName());
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //Async function that makes the reading run in the background
    private class MakeRequestRead extends AsyncTask<Void,Void,ArrayList<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestRead(GoogleAccountCredential credential) {
            HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets
                    .Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("")
                    .build();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                return getDataFromSheet();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        //Function that gets the data from the sheet
        private ArrayList<String>getDataFromSheet() throws IOException, ParseException {
            //Test spreadsheet

            String spreadsheetId="1IeH8kq3znoWEA7-BG8iGBC3IQqUzfnxE_dsGliy1hyo";
            String range = namesOfMonths[currentDate.get(Calendar.MONTH)]+"!A3:H"+
                    String.valueOf(currentDate.get(Calendar.DAY_OF_MONTH)+2);
            ArrayList<String> results = new ArrayList<String>();
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values != null) {
                results.add("Datum");
                for (List row : values) {

                    DownloadSpreadsheetData downloadSpreadsheetData = new DownloadSpreadsheetData();
                    String date = row.get(0).toString();
                    int workingHours = -1;
                    int workingMinutes = -1;
                    int overMinutes = -1;
                    int overHours = -1;
                    try{
                        workingHours = Integer.parseInt(row.get(5).toString());
                    }catch (Exception ex){
                        String timeString = row.get(5).toString();
                        String[] spilted = timeString.split(":");

                        Time time = new Time(Integer.parseInt(spilted[0]),Integer.parseInt(spilted[1]),0);
                        workingHours = time.getHours();
                        workingMinutes = time.getMinutes();
                    }
                    try{
                        overHours = Integer.parseInt(row.get(6).toString());
                    }catch (Exception ex){
                        String timeString = row.get(6).toString();
                        String[] spilted = timeString.split(":");
                        Time time = new Time(Integer.parseInt(spilted[0]),Integer.parseInt(spilted[1]),0);
                        overHours = time.getHours();
                        overMinutes = time.getMinutes();
                    }
                    String description = "";
                    try{
                        description = row.get(7).toString();
                    }catch (Exception ex){

                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    Date dates = simpleDateFormat.parse(date);// all done

                    Calendar cal = new GregorianCalendar();
                    cal.setTime(dates);
                    downloadSpreadsheetData.date = cal;
                    downloadSpreadsheetData.description = description;

                    if(workingHours + overHours >=0)
                        downloadSpreadsheetData.workingHours = workingHours+overHours;
                    else
                        downloadSpreadsheetData.workingHours = 0;

                    if(workingMinutes + overMinutes >=0)
                        downloadSpreadsheetData.workingMinutes = workingMinutes + workingHours;
                    else
                        downloadSpreadsheetData.workingMinutes = 0;

                    userData.addDownloadRepository(downloadSpreadsheetData);



                    String temp = "";
                    for (int i = 0; i < row.size(); i++) {
                        temp += row.get(i) + " ";
                    }
                    results.add(temp);
                }
            }



            return results;
        }

        @Override
        protected void onPreExecute() {
            //
        }
        //Called when the data is downloaded from the sheet
        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            if (strings == null || strings.size() == 0) {
//                textViewMonthlyOverview.setText("No results returned.");
            } else {
                strings.add(0, "Data retrieved using the Google Sheets API:");
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                adapter=new MonthlyAdapter(userData.setAdapterList());
                recyclerView.setAdapter(adapter);

                userData.cleanAdapterList();
                applicationTimeTracker.setUserData(userData);
                }
        };



        @Override
        protected void onCancelled(ArrayList<String> strings) {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServiceAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MonthlyOverview.REQUEST_AUTHORIZATION);
                } else {
                    //       textViewMonthlyOverview.setText("The following error occurred:\n"+ mLastError.getMessage());
                }
            } else {
                //textViewMonthlyOverview.setText("Request cancelled.");
            }
        }
    }

}
