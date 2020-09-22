package com.example.clientnetworking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.clientnetworking.views.fragment.HomeFragment;
import com.example.clientnetworking.views.fragment.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.navigations);
        frameLayout = findViewById(R.id.frameLayoutcontainer);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListen);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutcontainer,new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListen = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.nav_profile:
                    fragment = new HomeFragment();
                    break;
                case R.id.nav_post:
                    fragment = new ProductFragment();
                    break;
                default:
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutcontainer,fragment).commit();
            return true;
        }
    };

}