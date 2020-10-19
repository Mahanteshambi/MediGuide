package com.application.rxdose.model;

public class TabletModel {
    private String tabletName;
    private boolean isCosumed;

    public TabletModel() {
        
    }

    public TabletModel(String tabletName, boolean isCosumed) {
        this.tabletName = tabletName;
        this.isCosumed = isCosumed;
    }

    public String getTabletName() {
        return tabletName;
    }

    public boolean isCosumed() {
        return isCosumed;
    }

    public void setTabletName(String tabletName) {
        this.tabletName = tabletName;
    }

    public void setCosumed(boolean cosumed) {
        isCosumed = cosumed;
    }
}
