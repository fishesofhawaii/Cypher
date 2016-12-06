package com.example.inchkyle.cypherback;

import java.io.Serializable;

/**
 * Created by inchkyle on 12/5/16.
 */

public class HistoryObject implements Serializable {
    String time_stamp;
    String loc_id;
    boolean passing;

    HistoryObject(String ts, String l_id, String ans) {
        this.time_stamp = ts;
        this.loc_id = l_id;

        if (ans.equals("0")){
            this.passing = false;
        }
        else {
            this.passing = true;
        }

    }

    public boolean isPassing() {
        return passing;
    }

    public String getTime_stamp() {
        return time_stamp;
    }
    public String getLoc_id() {
        return loc_id;
    }
    public void setFailing() {
        passing = false;
    }

}
