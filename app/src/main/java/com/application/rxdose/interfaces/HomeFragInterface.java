package com.application.rxdose.interfaces;

import com.google.firebase.auth.FirebaseUser;

public interface HomeFragInterface {
    void setUser(FirebaseUser user);

    FirebaseUser getUser();
}
