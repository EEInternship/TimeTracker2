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

    private String userAcount;

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



    public void setUserAcount(String account){
        userAcount = account;
    }

    public boolean userAccountIsSet(){
        if(userAcount == null)
            return false;
        else
            return true;

    }
    public String getUserAcount(){return userAcount;}

}
