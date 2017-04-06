package Data;

import android.accounts.Account;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Klemen on 13. 03. 2017.
 */
public  class UserData {
    ArrayList<Project> projectList;
    UploadSpreadsheetData uploadSpreadsheetData;
    ArrayList<Ticket> ticketList;

    private Account userAcount;

    public UserData(){
        uploadSpreadsheetData = new UploadSpreadsheetData();
        ticketList = new ArrayList<>();
        projectList = new ArrayList<>();

    }

    public void scenariData(){
        Project project1 = new Project();
        project1.projectName="TIME TRACKER";
        Project project2 = new Project();
        project2.projectName="BUG REPORTER";
        Project project3 = new Project();
        project3.projectName="TEST PROJECT";

        projectList.add(project1);
        projectList.add(project2);
        projectList.add(project3);
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


    public ArrayList<Project> getProjectList()
    {
        return projectList;
    }

    public ArrayList<Ticket> getTicketList(){
        return ticketList;
    }
    public void setTicketList(ArrayList<Ticket> ticketArrayList){
        ticketList = ticketArrayList;
    }



    public void setUserAcount(Account account){
        userAcount = account;
    }

    public Account getUserAcount(){
        return userAcount;
    }

}
