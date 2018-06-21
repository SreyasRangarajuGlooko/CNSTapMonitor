package com.sreyas.cnstapmonitor;

import com.sreyas.cnstapmonitor.Models.TapRecord;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TapRecordTest {
    private TapRecord tapRecord = new TapRecord(25493149, 25);

    @Test
    public void getTimeStamp(){
        assertEquals(25493149, tapRecord.getTimeStamp());
    }

    @Test
    public void setTimeStamp(){
        tapRecord.setTimeStamp(25459483);
        assertEquals(25459483, tapRecord.getTimeStamp());
    }

    @Test
    public void getNumTaps(){
        assertEquals(25, tapRecord.getNumTaps());
    }

    @Test
    public void setNumTaps(){
        tapRecord.setNumTaps(40);
        assertEquals(40, tapRecord.getNumTaps());
    }

    @Test
    public void toStringOverride(){
        assertEquals("TimeStamp: 06/21/18 09:49 AM, NumTaps: 25", tapRecord.toString());
    }
}
