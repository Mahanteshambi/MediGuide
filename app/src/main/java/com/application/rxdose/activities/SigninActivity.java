package com.application.rxdose.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.rxdose.R;
import com.application.rxdose.utils.Constants;
import com.application.rxdose.utils.Utils;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends Activity implements View.OnClickListener {

    private final String TAG = SigninActivity.class.getCanonicalName();
    private Button mLoginBtn;
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;
    private TextView mSignUpLinkTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aciviy_sign_in);

        mLoginBtn = findViewById(R.id.signin_btn);
        mLoginBtn.setOnClickListener(this);
        mEmailEt = findViewById(R.id.sign_in_email_edit_tv);
        mPasswordEt = findViewById(R.id.sign_in_pwd_edit_tv);
        mSignUpLinkTv = findViewById(R.id.sign_up_link);
        mSignUpLinkTv.setOnClickListener(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        init();
    }

    private void init() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.sign_in_email_edit_tv, Patterns.EMAIL_ADDRESS, R.string.enter_proper_email);
        awesomeValidation.addValidation(this, R.id.sign_in_pwd_edit_tv, Constants.PASSWORD_PATTERN.toString(), R.string.enter_proper_password);
    }

    @Override
    public void onClick(View view) {
        if (view == mLoginBtn) {
            if (!validateEntries(mEmailEt.getText().toString(), mPasswordEt.getText().toString())) {
                return;
            }
            signInUser();
        } else if (view == mSignUpLinkTv) {
            Intent signUpIntent = new Intent(SigninActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }
    }

    private boolean validateEntries(String emailId, String password) {
        boolean isEmailValid = true;
        boolean isPasswordValid = true;
        boolean status = awesomeValidation.validate();
        if (!Utils.isValidEmail(emailId)) {
            isEmailValid = false;
        }
        if (password.length() < 8 && !Utils.isValidPassword(password)) {
            isPasswordValid = false;
        }
        if (!isEmailValid || !isPasswordValid) {
            Toast.makeText(this, R.string.enter_proper_details, Toast.LENGTH_SHORT).show();
            return false;
        }
        return status;
    }

    private void signInUser() {
        String email = mEmailEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(SigninActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Log.d(TAG, "onComplete: invalid_email");
                                Toast.makeText(SigninActivity.this, R.string.invalid_email,
                                        Toast.LENGTH_SHORT).show();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                Log.d(TAG, "onComplete: wrong_password");
                                Toast.makeText(SigninActivity.this, R.string.wrong_password,
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                                Toast.makeText(SigninActivity.this, R.string.went_wrong,
                                        Toast.LENGTH_SHORT).show();
                            }


                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent dashboardIntent = new Intent(SigninActivity.this, DashboardActivity.class);
            dashboardIntent.putExtra(Constants.UserNameTag, user);
            startActivity(dashboardIntent);
        } else {
            /*
            Something went wrong
             */
        }
    }
}
