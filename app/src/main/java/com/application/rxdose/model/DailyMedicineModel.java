package com.application.rxdose.model;

import java.util.ArrayList;
import java.util.List;

public class DailyMedicineModel {
    private String day;

    public DailyMedicineModel(String day) {
        this.day = day;
    }

    private List<TabletModel> tabletModelList = new ArrayList<>();

    public void addTabletInfo(TabletModel tabletModel) {
        tabletModelList.add(tabletModel);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<TabletModel> getTabletModelList() {
        return tabletModelList;
    }

    public void setTabletModelList(List<TabletModel> tabletModelList) {
        this.tabletModelList = tabletModelList;
    }
}
