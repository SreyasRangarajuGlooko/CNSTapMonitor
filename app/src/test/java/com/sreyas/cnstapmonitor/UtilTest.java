package com.sreyas.cnstapmonitor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void toStringOverride(){
        assertEquals("06/21/18 09:49 AM", Util.timeNumToString(25493149));
    }
}
