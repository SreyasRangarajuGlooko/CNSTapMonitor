package com.sreyas.cnstapmonitor;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sreyas on 1/25/2018.
 */

public class ManageDataFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_data, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;

        public class ViewHolderItem extends RecyclerView.ViewHolder {
            TextView date, numTaps;
            ImageButton deleteItem;

            public ViewHolderItem(View view) {
                super(view);
                date = view.findViewById(R.id.date);
                numTaps = view.findViewById(R.id.num_taps);
                deleteItem = view.findViewById(R.id.delete_item);
                deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TapData.deleteTapRecord(getAdapterPosition(), context);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        public RecyclerViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_data_row, parent, false);
            ViewHolderItem viewHolderItem = new ViewHolderItem(view);
            return viewHolderItem;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            TapRecord tapRecord = TapData.getTapData(context).get(position);
            ((ViewHolderItem) viewHolder).date.setText(getResources().getString(R.string.timeStamp,Util.timeNumToString(tapRecord.getTimeStamp())));
            ((ViewHolderItem) viewHolder).numTaps.setText(getResources().getString(R.string.numTaps,tapRecord.getNumTaps()));
        }

        @Override
        public int getItemCount() {
            return TapData.getTapData(context).size();
        }
    }
}
