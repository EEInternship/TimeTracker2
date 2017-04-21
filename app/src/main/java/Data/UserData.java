package Data;

import java.util.ArrayList;

/**
 * Created by Klemen on 13. 03. 2017.
 */
public  class UserData {
    ArrayList<Project> projectList;
    UploadSpreadsheetData uploadSpreadsheetData;
    ArrayList<Ticket> ticketList;
    ArrayList<ProfileDataDropdown> profileDataDropdownArrayList;
    NotificationData notificationData;

    private String userAcount;

    public UserData(){
        uploadSpreadsheetData = new UploadSpreadsheetData();
        ticketList = new ArrayList<>();
        projectList = new ArrayList<>();
        profileDataDropdownArrayList = new ArrayList<>();
        notificationData = new NotificationData();
    }


    public NotificationData getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(NotificationData notificationData) {
        this.notificationData = notificationData;
    }

    public void addUploadRepository(UploadSpreadsheetData data){
        this.uploadSpreadsheetData = data;
    }

    public UploadSpreadsheetData getUploadSpreadsheetData(){
        return this.uploadSpreadsheetData;
    }

    public ArrayList<ProfileDataDropdown> getProfileDataDropdownArrayList() {
        return profileDataDropdownArrayList;
    }

    public void setProfileDataDropdownArrayList(ArrayList<ProfileDataDropdown> profileDataDropdownArrayList) {
        this.profileDataDropdownArrayList = profileDataDropdownArrayList;
    }

    public void addProjectList(ArrayList<Project> list) {projectList = list;}
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
