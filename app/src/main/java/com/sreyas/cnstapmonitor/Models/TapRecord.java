package com.sreyas.cnstapmonitor.Models;

import com.sreyas.cnstapmonitor.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Sreyas on 1/25/2018.
 */

public class TapRecord implements Serializable {

    private long timeStamp;
    private int numTaps;

    public TapRecord(long timeStamp, int numTaps) {
        this.timeStamp = timeStamp;
        this.numTaps = numTaps;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNumTaps() {
        return numTaps;
    }

    public void setNumTaps(int numTaps) {
        this.numTaps = numTaps;
    }

    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timeStamp", getTimeStamp());
            jsonObject.put("numTaps", getNumTaps());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String toString() {
        return String.format("TimeStamp: %s, NumTaps: %d", Util.timeNumToString(timeStamp), numTaps);
    }

}
