package com.sreyas.cnstapmonitor.Tap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.sreyas.cnstapmonitor.Models.FeedbackHandler;
import com.sreyas.cnstapmonitor.Models.TapDataListener;
import com.sreyas.cnstapmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sreyas on 1/24/2018.
 */

public class TapFragment extends Fragment implements TapViewModel.TapListener{

    TapViewModel tapViewModel;
    @BindView(R.id.tap_info) TextView tapInfo;
    @BindView(R.id.tap_count) TextView tapCountView;
    Unbinder unbinder;
    AlertDialog.Builder builder;
    AlertDialog saveDialog;
    TapDataListener tapDataListener;
    FeedbackHandler feedbackHandler;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tapDataListener = (TapDataListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TapDataListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(tapViewModel == null) {
            tapViewModel = new TapViewModel();
            tapViewModel.addTapListener(this);
        }
        feedbackHandler = new FeedbackHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tap_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(savedInstanceState != null && savedInstanceState.getInt("Dialog") == 1){
            showSaveDialog();
        }
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tap_count)
    void onClick(){
        tapViewModel.tap();
    }

    private void showSaveDialog(){
        createSaveDialog();
        saveDialog = builder.create();
        saveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveDialog.show();
    }

    private void createSaveDialog(){
        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage(getResources().getString(R.string.save_message, tapViewModel.getTapCount()));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                tapViewModel.addTapRecord(getActivity());
                tapDataListener.onTapDataChanged();
                resetTap();
                feedbackHandler.launchFeedbackConditionally();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.discard), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                TapData.setFakeData(getActivity());
                tapDataListener.onTapDataChanged();
                resetTap();
                dialog.cancel();
            }
        });
    }

    public void resetTap(){
        tapViewModel.reset();
    }

    @Override
    public void onTimeLeftChanged(double timeLeft) {
        if(timeLeft >= 0){
            tapInfo.setText(getString(R.string.time_left, timeLeft));
        }
        else {
            tapInfo.setText(getString(R.string.tap_info));
        }
    }

    @Override
    public void onTapCountChanged(int tapCount) {
        tapCountView.setText(String.valueOf(tapCount));
    }

    @Override
    public void onFinished() {
        showSaveDialog();
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
