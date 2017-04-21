package Data;

import java.sql.Time;

/**
 * Created by Klemen on 4/18/2017.
 */

public class NotificationData {
    private boolean TurnOnOf;
    private Time notificationStartTime;
    private Time notificationPopupTime;

    public NotificationData() {

    }

    public boolean isSet(){
        if(notificationPopupTime == null || notificationStartTime == null)
            return false;
        return true;
    }

    public boolean isTurnOnOf() {
        return TurnOnOf;
    }

    public void setTurnOnOf(boolean turnOnOf) {
        TurnOnOf = turnOnOf;
    }

    public Time getNotificationStartTime() {
        if(notificationStartTime != null)
            return notificationStartTime;
        else
            return new Time(8,0,0);
    }

    public void setNotificationStartTime(Time notificationStartTime) {
        this.notificationStartTime = notificationStartTime;
    }

    public Time getNotificationPopupTime() {
        return notificationPopupTime;
    }

    public void setNotificationPopupTime(Time notificationPopupTime) {
        this.notificationPopupTime = notificationPopupTime;
    }
}
