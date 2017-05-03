package Data;

import java.sql.Time;

/**
 * Created by Klemen on 16. 03. 2017.
 */

public class Project {
    public String projectName;
    public Time startingTime;
    public Time finishTime;
    public String description;
    private String ticketColor;

    public Project(){
        ticketColor = "#1E90FF";
    }



    public String getTicketColor() {
        return ticketColor;
    }

    public void setTicketColor(String ticketColor) {
        this.ticketColor = ticketColor;
    }
}
