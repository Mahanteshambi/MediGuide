package com.application.rxdose.utils;

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
