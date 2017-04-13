package RESTtest;

import java.util.ArrayList;

/**
 * Created by Ales on 4. 04. 2017.
 */

public class TestData {
    TestWorkDay work_day;
    ArrayList<TestWorkingOn> work_on;

    public TestData(TestWorkDay work_day, ArrayList<TestWorkingOn> work_on) {
        this.work_day = work_day;
        this.work_on = work_on;
    }

    public TestWorkDay getWork_day() {
        return work_day;
    }

    public void setWork_day(TestWorkDay work_day) {
        this.work_day = work_day;
    }

    public ArrayList<TestWorkingOn> getWork_on() {
        return work_on;
    }

    public void setWork_on(ArrayList<TestWorkingOn> work_on) {
        this.work_on = work_on;
    }

    @Override
    public String toString() {
        String string = this.work_day.toString()+"\n";
        for(int i = 0; i<this.work_on.size(); i++)
        {
            string+="\t"+this.work_on.get(i).toString()+"\n";
        }
        return string;
    }
}
