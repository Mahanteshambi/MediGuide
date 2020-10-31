package com.application.rxdose.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.data.model.User;

public class UserInfoModel extends ViewModel {
    private MutableLiveData<User> user;

    public LiveData<User> getUser() {
        return user;
    }

    public void setUsers(MutableLiveData<User> user) {
//        user.setValue(user);
        this.user = user;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}
