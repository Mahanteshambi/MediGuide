package com.application.rxdose.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.rxdose.adapters.DailyReportAdapter;
import com.application.rxdose.R;
import com.application.rxdose.model.DailyMedicineModel;
import com.application.rxdose.model.TabletModel;
import com.application.rxdose.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private List<DailyMedicineModel> dailyReportList = new ArrayList<>();
    private List<ViewCategory> viewCategories = new ArrayList<>();
    private DailyReportAdapter dAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        dAdapter = new DailyReportAdapter(viewCategories, dailyReportList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(dAdapter);
        prepareDailyReport();
    }

    private void prepareDailyReport() {
        viewCategories.add(ViewCategory.DOCTOR_INFO);
        viewCategories.add(ViewCategory.REPORT);
        TabletModel tabletModel1 = new TabletModel("xyz", true);
        TabletModel tabletModel2 = new TabletModel("abc", true);
        TabletModel tabletModel3 = new TabletModel("abc", false);

        DailyMedicineModel mondayReport = new DailyMedicineModel(Utils.getPreviousDateString(0));
        mondayReport.addTabletInfo(tabletModel1);
        mondayReport.addTabletInfo(tabletModel2);
        mondayReport.addTabletInfo(tabletModel3);

        DailyMedicineModel tuesdayReport = new DailyMedicineModel(Utils.getPreviousDateString(1));
        tabletModel1 = new TabletModel("xyz", true);
        tabletModel2 = new TabletModel("abc", true);
        tabletModel3 = new TabletModel("abc", true);
        tuesdayReport.addTabletInfo(tabletModel1);
        tuesdayReport.addTabletInfo(tabletModel2);
        tuesdayReport.addTabletInfo(tabletModel3);

        DailyMedicineModel wedReport = new DailyMedicineModel(Utils.getPreviousDateString(2));
        tabletModel2.setCosumed(false);
        wedReport.addTabletInfo(tabletModel1);
        wedReport.addTabletInfo(tabletModel2);
        wedReport.addTabletInfo(tabletModel3);

        DailyMedicineModel thursdayReport = new DailyMedicineModel(Utils.getPreviousDateString(3));
        thursdayReport.addTabletInfo(tabletModel1);
        thursdayReport.addTabletInfo(tabletModel2);
        tabletModel3.setCosumed(false);
        thursdayReport.addTabletInfo(tabletModel3);

        DailyMedicineModel fridayReport = new DailyMedicineModel(Utils.getPreviousDateString(4));
        fridayReport.addTabletInfo(tabletModel1);
        fridayReport.addTabletInfo(tabletModel2);
        fridayReport.addTabletInfo(tabletModel3);

        DailyMedicineModel saturdayReport = new DailyMedicineModel(Utils.getPreviousDateString(5));
        tabletModel1.setCosumed(false);
        saturdayReport.addTabletInfo(tabletModel1);
        saturdayReport.addTabletInfo(tabletModel2);
        saturdayReport.addTabletInfo(tabletModel3);

        DailyMedicineModel sundayReport = new DailyMedicineModel(Utils.getPreviousDateString(6));
        sundayReport.addTabletInfo(tabletModel1);
        sundayReport.addTabletInfo(tabletModel2);
        tabletModel3.setCosumed(false);
        sundayReport.addTabletInfo(tabletModel3);

        dailyReportList.add(mondayReport);
        dailyReportList.add(tuesdayReport);
        dailyReportList.add(wedReport);
        dailyReportList.add(thursdayReport);
        dailyReportList.add(fridayReport);
        dailyReportList.add(saturdayReport);
        dailyReportList.add(sundayReport);

    }

    public enum ViewCategory {
        DOCTOR_INFO(0),
        REPORT(1);
        private int id;

        ViewCategory(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }
}