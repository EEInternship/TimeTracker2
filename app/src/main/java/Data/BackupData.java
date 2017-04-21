package Data;

import java.util.ArrayList;

/**
 * Created by Klemen on 4/6/2017.
 */

public class BackupData {
    public UploadSpreadsheetData uploadSpreadsheetData;
    public ArrayList<Ticket> ticketList;
    private String userAcount;
    public NotificationData notificationData;

    public BackupData(){
        uploadSpreadsheetData = new UploadSpreadsheetData();
        ticketList = new ArrayList<>();
        notificationData = new NotificationData();
    }

    public String getUserAcount(){return userAcount;}
    public void setUserAcount(String userName){userAcount = userName;}
}
