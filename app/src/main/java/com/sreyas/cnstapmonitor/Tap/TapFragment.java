package com.sreyas.cnstapmonitor.Tap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.sreyas.cnstapmonitor.Models.TapDataListener;
import com.sreyas.cnstapmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sreyas on 1/24/2018.
 */

public class TapFragment extends Fragment implements TapViewModel.TapListener {

    @BindView(R.id.tap_info)
    TextView tapInfo;
    @BindView(R.id.tap_count)
    TextView tapCountView;
    private Unbinder unbinder;
    private TapViewModel tapViewModel;
    private TapDataListener tapDataListener;
    private AlertDialog alertDialog;

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
        if (tapViewModel == null) {
            tapViewModel = new TapViewModel();
            tapViewModel.addTapListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tap_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        tapViewModel.setTapDataListener(tapDataListener);
        if (savedInstanceState != null && savedInstanceState.getInt("Dialog", 0) == 1) {
            alertDialog.show();
        }
        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(alertDialog == null || !alertDialog.isShowing()){
            resetTap();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (alertDialog != null && alertDialog.isShowing()) {
            outState.putInt("Dialog", 1);
        } else {
            outState.putInt("Dialog", 0);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        tapDataListener = null;
        tapViewModel.setTapDataListener(null);
    }

    @OnClick(R.id.tap_count)
    void onClick() {
        tapViewModel.tap();
    }

    @Override
    public void onTimeLeftChanged(double timeLeft) {
        if (timeLeft >= 0) {
            tapInfo.setText(getString(R.string.time_left, timeLeft));
        } else {
            tapInfo.setText(getString(R.string.tap_info));
        }
    }

    @Override
    public void onTapCountChanged(int tapCount) {
        tapCountView.setText(String.valueOf(tapCount));
    }

    @Override
    public void onFinished() {
        showDialog(getString(R.string.save_message, tapViewModel.getTapCount()));
    }

    @Override
    public void onFeedbackReady(String message) {
        showDialog(message);
    }

    @Override
    public void onSupport(Intent intent) {
        startActivity(intent);
    }

    public void resetTap() {
        tapViewModel.reset();
    }

    private void showDialog(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                tapViewModel.respondToDialog(message, 1, getActivity());
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                tapViewModel.respondToDialog(message, 0, getActivity());
            }
        });
        alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }


}
