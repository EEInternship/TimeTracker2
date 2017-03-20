package com.eeinternship.timetracker;

/**
 * Created by IsakFe on 20. 03. 2017.
 */

public class Contact {
    private String day,datum,ure;

    public Contact(String day, String datum, String ure) {
        this.day = day;
        this.datum = datum;
        this.ure = ure;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getUre() {
        return ure;
    }

    public void setUre(String ure) {
        this.ure = ure;
    }
}
