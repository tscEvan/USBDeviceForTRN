package com.example.usbdevicefortrn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;

import com.example.usbdevicefortrn.fragmentActivity.MenuFragment;
import com.example.usbdevicefortrn.fragmentActivity.OwnFragment;
import com.example.usbdevicefortrn.fragmentActivity.StoreFragment;
import com.example.usbdevicefortrn.ownDevice.DevicesKeyBaseBean;
import com.example.usbdevicefortrn.store.ProductBean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static final int LOGIN_CHECK = 5;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private OwnFragment ownFragment;
    private StoreFragment storeFragment;
    private MenuFragment menuFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginPage = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(loginPage, LOGIN_CHECK);
                    Log.d(TAG, "onAuthStateChanged: No login");
                }else {
                    Log.d(TAG, "onAuthStateChanged: " + auth.getUid());
                }
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //recheck user auth
        if (auth.getCurrentUser() ==null) {
            Intent loginPage = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(loginPage, LOGIN_CHECK);
        }else {
            ownFragment = new OwnFragment();
            storeFragment = new StoreFragment();
            menuFragment = new MenuFragment();
            navView.setSelectedItemId(R.id.navigation_home);
        }
//        addTestData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void addTestData() {
        CollectionReference products = FirebaseFirestore.getInstance().collection("products").document("forSell").collection("usb");
        CollectionReference keyBase = FirebaseFirestore.getInstance().collection("keyBase").document("usb").collection("info");
//        CollectionReference history = FirebaseFirestore.getInstance().collection("keyBase").document("usb").collection("history");
        for (int i = 0; i < 3; i++) {
            String productKey = FirebaseFirestore.getInstance().collection("pkey").document().getId();
            String deviceId = FirebaseFirestore.getInstance().collection("deviceId").document().getId();
            products.add(new ProductBean(false, productKey, deviceId));
            keyBase.add(new DevicesKeyBaseBean(deviceId, "1",null, Arrays.asList(1.234, 12.34, 123.4)));
//            history.add(new HIstoryBean(FieldValue.serverTimestamp(),));
        }
    }
        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGIN_CHECK:
                if (resultCode!=RESULT_OK) finish();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = ownFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                case R.id.navigation_store:
                    selectedFragment = storeFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                case R.id.navigation_menu:
                    selectedFragment = menuFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
            }
            return false;
        }
    };
}
