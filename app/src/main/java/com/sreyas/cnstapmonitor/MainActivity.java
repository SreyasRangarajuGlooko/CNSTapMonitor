package com.sreyas.cnstapmonitor;

import android.app.Activity;
import android.os.Bundle;

import com.sreyas.cnstapmonitor.TapView.TapFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.fragment_holder, new TapFragment()).commit();
        }
    }
}
