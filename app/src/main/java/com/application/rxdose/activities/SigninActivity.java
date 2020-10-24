package com.application.rxdose.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SigninActivity extends Activity implements View.OnClickListener {

    private final String TAG = SigninActivity.class.getCanonicalName();
    private Button mLoginBtn;
    private Button mGmailLoginBtn;
    private Button mFbLoginBtn;
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;
    private TextView mSignUpLinkTv;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 102450;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.aciviy_sign_in);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.application.rxdose",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        mLoginBtn = findViewById(R.id.signin_btn);
        mLoginBtn.setOnClickListener(this);
        mEmailEt = findViewById(R.id.sign_in_email_edit_tv);
        mPasswordEt = findViewById(R.id.sign_in_pwd_edit_tv);
        mSignUpLinkTv = findViewById(R.id.sign_up_link);
        mSignUpLinkTv.setOnClickListener(this);
        mGmailLoginBtn = findViewById(R.id.gmail_signin_btn);
        mGmailLoginBtn.setOnClickListener(this);
        mFbLoginBtn = findViewById(R.id.fb__signin_btn);
        mFbLoginBtn.setOnClickListener(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        init();
    }

    private void init() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.sign_in_email_edit_tv, Patterns.EMAIL_ADDRESS, R.string.enter_proper_email);
        awesomeValidation.addValidation(this, R.id.sign_in_pwd_edit_tv, Constants.PASSWORD_PATTERN.toString(), R.string.enter_proper_password);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


//        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle success
                        Toast.makeText(SigninActivity.this, "FB signed- in success", Toast.LENGTH_SHORT).show();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SigninActivity.this, "FB signedin cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(SigninActivity.this, "FB signedin error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
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
        } else if (view == mGmailLoginBtn) {
            gmailSignIn();
        } else if (view == mFbLoginBtn) {
            fbSignIn();
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

    private void fbSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
        );
    }

    private void gmailSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
