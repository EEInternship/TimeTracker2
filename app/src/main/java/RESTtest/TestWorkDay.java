package RESTtest;

/**
 * Created by Ales on 4. 04. 2017.
 */

public class TestWorkDay {
    String date;
    String starting_time;
    String finish_time;
    String working_hours;
    String overtime;

    public TestWorkDay(String date, String starting_time, String finish_time, String working_hours, String overtime) {
        this.date = date;
        this.starting_time = starting_time;
        this.finish_time = finish_time;
        this.working_hours = working_hours;
        this.overtime = overtime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStarting_time() {
        return starting_time;
    }

    public void setStarting_time(String starting_time) {
        this.starting_time = starting_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(String working_hours) {
        this.working_hours = working_hours;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    @Override
    public String toString() {
        return this.date+" "+this.starting_time+" "+this.finish_time+" WH: "+this.working_hours+" OT: "+this.overtime;
    }
}
