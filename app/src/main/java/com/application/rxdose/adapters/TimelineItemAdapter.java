package com.application.rxdose.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.rxdose.R;
import com.application.rxdose.model.DailyMedicineModel;

import java.util.List;

public class TimelineItemAdapter extends RecyclerView.Adapter<TimelineItemAdapter.MyViewHolder> {

    private List<List<DailyMedicineModel>> dataSet;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        LinearLayout timeContainerLayout;
        RecyclerView recyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.report_title);
            this.recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            timeContainerLayout = itemView.findViewById(R.id.time_container);
        }
    }

    public TimelineItemAdapter(List<List<DailyMedicineModel>> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_item, parent, false);
        mContext = parent.getContext();
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        RecyclerView recyclerView = holder.recyclerView;
        LinearLayout timelistLL = holder.timeContainerLayout;
        int tabletFreqInaDay = dataSet.get(listPosition).get(0).getTabletTimeList().size();
        for (int i = 0; i < tabletFreqInaDay; i++) {
            TextView timeTv = new TextView(mContext);
            timeTv.setText(dataSet.get(listPosition).get(0).getTabletTimeList().get(i));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                layoutParams.topMargin = (int) mContext.getResources()
                        .getDimension(R.dimen.timeline_tablet_name_size);
            } else {
                layoutParams.topMargin = (int) mContext.getResources()
                        .getDimension(R.dimen.timeline_time_top_margin);
            }
            float myTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    5F, mContext.getResources().getDisplayMetrics());
            timeTv.setTypeface(Typeface.DEFAULT_BOLD);
            timeTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, myTextSize);
            timeTv.setLayoutParams(layoutParams);
            timelistLL.addView(timeTv);
        }
        DailyReportAdapter dAdapter = new DailyReportAdapter(dataSet.get(listPosition));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dAdapter);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}