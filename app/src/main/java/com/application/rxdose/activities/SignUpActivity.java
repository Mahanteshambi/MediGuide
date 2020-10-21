package com.application.rxdose.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.rxdose.R;
import com.application.rxdose.utils.Constants;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;

public class SignUpActivity extends Activity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Button mLoginButton;
    private EditText emailEditTxt;
    private EditText passwordEditTxt;
    private EditText confirmPasswordEditTxt;
    private AwesomeValidation awesomeValidation;
    private final String TAG = SignUpActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mLoginButton = findViewById(R.id.signup_btn);
        mLoginButton.setOnClickListener(this);
        emailEditTxt = findViewById(R.id.sign_up_email_edit_tv);
        passwordEditTxt = findViewById(R.id.sign_up_pwd_edit_tv);
        confirmPasswordEditTxt = findViewById(R.id.sign_up_confirm_pwd_edit_tv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.sign_up_email_edit_tv, Patterns.EMAIL_ADDRESS, R.string.enter_proper_email);
        awesomeValidation.addValidation(this, R.id.sign_up_pwd_edit_tv, Constants.PASSWORD_PATTERN.toString(), R.string.enter_proper_password);
        awesomeValidation.addValidation(this, R.id.sign_up_confirm_pwd_edit_tv, Constants.PASSWORD_PATTERN.toString(), R.string.enter_proper_password);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void signUp() {
        if (!validateEntries()) {
            return;
        }
        String email = emailEditTxt.getText().toString();
        String password = passwordEditTxt.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUpActivity.this, R.string.acc_create_success,
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private boolean validateEntries() {
        boolean isEmailValid = true;
        boolean isPasswordValid = true;
        boolean isConfimPasswordValid = true;
        boolean status = awesomeValidation.validate();
        String emailId = emailEditTxt.getText().toString();
        String password = passwordEditTxt.getText().toString();
        String confirmPassword = confirmPasswordEditTxt.getText().toString();
        if (!isValidEmail(emailId)) {
            isEmailValid = false;
        }
        if (password.length() < 8 && !isValidPassword(password)) {
            isPasswordValid = false;
        }
        if (confirmPassword.length() < 8
                && !isValidPassword(confirmPassword)
                && passwordEditTxt.getText().toString().equals(confirmPassword)) {
            Toast.makeText(this, R.string.passwords_not_matching, Toast.LENGTH_SHORT).show();
            isConfimPasswordValid = false;
        }
        if (!isEmailValid || !isPasswordValid || !isConfimPasswordValid) {
            Toast.makeText(this, R.string.enter_proper_details, Toast.LENGTH_SHORT).show();
            return false;
        }
        return status;
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isValidPassword(final String password) {
        Matcher matcher = Constants.PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }


    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            Intent dashboardIntent = new Intent(SignUpActivity.this, DashboardActivity.class);
            dashboardIntent.putExtra(Constants.UserNameTag, user);
            startActivity(dashboardIntent);
        } else {
            /*
            Something went wrong
             */
        }
    }

    private void hideProgressBar() {

    }

    @Override
    public void onClick(View view) {
        if (view == mLoginButton) {
            signUp();
        }
    }
}
