package com.application.rxdose.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.rxdose.R;
import com.application.rxdose.activities.DashboardActivity;
import com.application.rxdose.model.DailyMedicineModel;
import com.application.rxdose.model.TabletModel;

import java.util.List;

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.MyViewHolder> {
    private List<DailyMedicineModel> dailyMedicineModels;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        ImageView tablet1ImgView, tablet2ImgView, tablet3ImgView;

        MyViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.dayname);
            tablet1ImgView = view.findViewById(R.id.tablet1);
            tablet2ImgView = view.findViewById(R.id.tablet2);
            tablet3ImgView = view.findViewById(R.id.tablet3);
        }
    }

    public DailyReportAdapter(List<DashboardActivity.ViewCategory> viewCategories,
                              List<DailyMedicineModel> dailyMedicineModels) {
        this.dailyMedicineModels = dailyMedicineModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_status_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DailyMedicineModel dailyMedicineModel = dailyMedicineModels.get(position);
        holder.day.setText(dailyMedicineModel.getDay());
        List<TabletModel> tabletModelList = dailyMedicineModel.getTabletModelList();
        setImageView(tabletModelList.get(0).isCosumed(), holder.tablet1ImgView);
        setImageView(tabletModelList.get(1).isCosumed(), holder.tablet2ImgView);
        setImageView(tabletModelList.get(2).isCosumed(), holder.tablet3ImgView);
    }

    private void setImageView(boolean isConsumed, ImageView imageView) {
        if (isConsumed) {
            imageView.setImageResource(R.drawable.consumed_icon);
        } else {
            imageView.setImageResource(R.drawable.not_consumed_icon);
        }
    }

    @Override
    public int getItemCount() {
        return dailyMedicineModels.size();
    }
}