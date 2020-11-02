package com.application.rxdose.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrescriptionViewModel extends ViewModel {

    private MutableLiveData<String> name;
    private MutableLiveData<String> date;
    private MutableLiveData<String> url;

    public void setNameData(String nameData) {
        name.setValue(nameData);

/*
        If you are calling setNameData from a background thread use:
        name.postValue(nameData);
*/
    }

    public MutableLiveData<String> getNameData() {
        if (name == null) {
            name = new MutableLiveData<>();
        }

        return name;
    }

    public MutableLiveData<String> getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date.setValue(date);
    }

    public MutableLiveData<String> getUrl() {
        if (url == null) {
            url = new MutableLiveData<>();
        }
        return url;
    }

    public void setUrl(String url) {
        this.url.setValue(url);
    }
}