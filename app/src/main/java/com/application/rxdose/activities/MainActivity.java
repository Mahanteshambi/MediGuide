package com.application.rxdose.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.application.rxdose.R;
import com.application.rxdose.fragments.HomeFragment;
import com.application.rxdose.fragments.MedicationFragment;
import com.application.rxdose.fragments.PrescriptionFragment;
import com.application.rxdose.fragments.ProfileFragment;
import com.application.rxdose.fragments.TimelineFragment;
import com.application.rxdose.interfaces.HomeFragInterface;
import com.application.rxdose.utils.Constants;
import com.application.rxdose.viewmodel.PrescriptionViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeFragInterface {
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private TextView mPatientName;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_timeline, R.id.medication_fragment)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getParcelable(Constants.UserNameTag);
        }
        prescriptionViewModel = ViewModelProviders.of(this)
                .get(PrescriptionViewModel.class);
//        UserInfoModel model = new ViewModelProvider(this).get(UserInfoModel.class);
//        model.setUsers(new MutableLiveData(user));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            navController.navigate(R.id.nav_home);
        } else if (id == R.id.nav_medication) {
            navController.navigate(R.id.medication_fragment);
        } else if (id == R.id.nav_profile) {
            navController.navigate(R.id.profile_fragment);
        } else if (id == R.id.nav_timeline) {
            navController.navigate(R.id.timeline_fragment);
        } else if (id == R.id.nav_prescription) {
            EnableRuntimePermission();
            navController.navigate(R.id.prescription_fragment);
        } else if (id == R.id.nav_item_exit) {
            finish();
        }
//        switchFragment(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(int item) {
        Fragment fragment = null;
        switch (item) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_medication:
                fragment = new MedicationFragment();
                break;
            case R.id.nav_timeline:
                fragment = new TimelineFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_prescription:
                fragment = new PrescriptionFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    @Override
    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    @Override
    public FirebaseUser getUser() {
        return user;
    }

    public static final int RequestPermissionCode = 1;
    private PrescriptionViewModel prescriptionViewModel;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String imageName = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + currentDateandTime + ".jpg";
            try (FileOutputStream out = new FileOutputStream(imageName)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }
            prescriptionViewModel.setUrl(imageName);
        }
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}