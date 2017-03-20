package Data;

/**
 * Created by Klemen on 3/20/2017.
 */

public class AdapterClass {
    private String dayOfWeek;
    private String date;
    private String hours;

    public AdapterClass(){

    }
    public AdapterClass(String day, String todayDate, String hoursOfDay){
        dayOfWeek = day;
        date = todayDate;
        hours = hoursOfDay;
    }

    public String getDayOfWeek(){
        return dayOfWeek;
    }
    public String getDate(){
        return date;
    }
    public String getHours(){
        return hours;
    }
}
