package Data;

import android.accounts.Account;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Klemen on 13. 03. 2017.
 */
public  class UserData {
    ArrayList<DownloadSpreadsheetData> downloadSpreadsheetDataList;
    ArrayList<Project> projectList;
    UploadSpreadsheetData uploadSpreadsheetData;

    private Account userAcount;

    public UserData(){
        uploadSpreadsheetData = new UploadSpreadsheetData();
        downloadSpreadsheetDataList = new ArrayList<DownloadSpreadsheetData>();
        scenariData();
    }

    public void scenariData(){
        Project project1 = new Project();
        project1.projectName="Project 1";
        Project project2 = new Project();
        project2.projectName="Project 2";
        Project project3 = new Project();
        project3.projectName="Project 3";

        projectList.add(project1);
        projectList.add(project2);
        projectList.add(project3);
    }


    public void addDownloadRepository(DownloadSpreadsheetData downloadSpreadsheetData){
        this.downloadSpreadsheetDataList.add(downloadSpreadsheetData);
    }
    public void addUploadRepository(UploadSpreadsheetData data){
        this.uploadSpreadsheetData = data;
    }

    public UploadSpreadsheetData getUploadSpreadsheetData(){
        return this.uploadSpreadsheetData;
    }

    public void addProject(Project project){
        projectList.add(project);
    }
    public ArrayList<String> DownloadSpreadsheetToString(){
        ArrayList<String> dataListStrings = new ArrayList<>();
        for (DownloadSpreadsheetData row: downloadSpreadsheetDataList){
            int dayOfWeek = row.date.get(Calendar.DAY_OF_WEEK);
            String dayShort = ShortDay(dayOfWeek);
            String date = String.valueOf(row.date.get(Calendar.DAY_OF_MONTH)) + "." + String.valueOf(row.date.get(Calendar.MONTH)+1) + "." +String.valueOf(row.date.get(Calendar.YEAR));
            String hours = String.valueOf(row.workingHours);
            String stringRow = dayShort + " " + date + " " + hours;
            dataListStrings.add(stringRow);
        }
        return dataListStrings;

    }
    private String ShortDay(int dayOfWeek){
        String day = "";
        switch (dayOfWeek){
            case 1:
                day = "Sun";
                break;
            case 2:
                day = "Mon";
                break;
            case 3:
                day = "Tue";
                break;
            case 4:
                day ="Wed";
                break;
            case 5:
                day="Thu";
                break;
            case 6:
                day="Fri";
                break;
            case 7:
                day="Sat";
                break;
        }
        return day;
    }




    public void setUserAcount(Account account){
        userAcount = account;
    }

    public Account getUserAcount(){
        return userAcount;
    }

}
