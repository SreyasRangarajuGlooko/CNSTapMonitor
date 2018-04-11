package com.sreyas.cnstapmonitor.ManageData;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sreyas.cnstapmonitor.Models.TapData;
import com.sreyas.cnstapmonitor.Models.TapRecord;
import com.sreyas.cnstapmonitor.R;
import com.sreyas.cnstapmonitor.Models.TapDataListener;
import com.sreyas.cnstapmonitor.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sreyas on 1/25/2018.
 */

public class ManageDataFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    TapDataListener tapDataListener;
    Unbinder unbinder;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_data, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;

        class ViewHolderItem extends RecyclerView.ViewHolder {
            @BindView(R.id.date) TextView date;
            @BindView(R.id.num_taps) TextView numTaps;
            @BindView(R.id.delete_item) ImageButton deleteItem;

            ViewHolderItem(View view) {
                super(view);
                ButterKnife.bind(this, view);
                deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TapData.deleteTapRecord(getAdapterPosition(), context);
                        notifyDataSetChanged();
                        tapDataListener.onTapDataChanged();
                    }
                });
            }

        }

        RecyclerViewAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_data_row, parent, false);
            return new ViewHolderItem(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            TapRecord tapRecord = TapData.getTapData(context).get(position);
            ((ViewHolderItem) viewHolder).date.setText(String.valueOf(Util.timeNumToString(tapRecord.getTimeStamp())));
            ((ViewHolderItem) viewHolder).numTaps.setText(getResources().getString(R.string.numTaps,tapRecord.getNumTaps()));
        }

        @Override
        public int getItemCount() {
            return TapData.getTapData(context).size();
        }
    }
}
