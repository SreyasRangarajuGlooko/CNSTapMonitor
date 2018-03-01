package com.sreyas.cnstapmonitor.Tap;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.sreyas.cnstapmonitor.MainActivity;
import com.sreyas.cnstapmonitor.R;
import com.sreyas.cnstapmonitor.Models.TapData;
import com.sreyas.cnstapmonitor.Models.TapRecord;

/**
 * Created by Sreyas on 1/24/2018.
 */

public class TapFragment extends Fragment {

    TapViewLogic tapViewLogic;
    TextView tapInfo, tapCount;
    AlertDialog.Builder builder;
    AlertDialog saveDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(tapViewLogic == null) {
            tapViewLogic = new TapViewLogic(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tap_view, container, false);
        tapInfo = view.findViewById(R.id.tap_info);
        tapCount = view.findViewById(R.id.tap_count);
        tapCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapViewLogic.incrementTapCount();
                tapCount.setText(String.valueOf(tapViewLogic.getTapCount()));
            }
        });

        if(savedInstanceState != null && savedInstanceState.getInt("Dialog") == 1){
            showSaveDialog();
        }
        return view;
    }

    public void timeIntervalUpdate(double timeLeft){
        if(getActivity() != null){
            tapInfo.setText(getString(R.string.time_left, timeLeft));
        }
    }

    public void timeFinished(){
        if(getActivity() != null){
            tapInfo.setText(getString(R.string.tap_info));
            showSaveDialog();
        }
    }

    public void showSaveDialog(){
        createSaveDialog();
        saveDialog = builder.create();
        saveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveDialog.show();
    }

    private void createSaveDialog(){
        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage(getResources().getString(R.string.save_message, tapViewLogic.getTapCount()));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TapData.addTapRecord(new TapRecord(System.currentTimeMillis() / 60000, tapViewLogic.getTapCount()), getActivity());
                ((MainActivity) getActivity()).redrawGraph();
                resetTap();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.discard), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                TapData.setFakeData(getActivity());
                ((MainActivity) getActivity()).redrawGraph();
                resetTap();
                dialog.cancel();
            }
        });
    }

    public void resetTap(){
        tapViewLogic.resetTapCount();
        tapInfo.setText(getResources().getString(R.string.tap_info));
        tapCount.setText(String.valueOf(tapViewLogic.getTapCount()));
        tapViewLogic.setReady();
    }

    public void resetTimer(){
        tapViewLogic.resetTimer();
        resetTap();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(saveDialog != null && saveDialog.isShowing()){
            outState.putInt("Dialog",1);
        }
        else{
            outState.putInt("Dialog",0);
        }

    }
}
