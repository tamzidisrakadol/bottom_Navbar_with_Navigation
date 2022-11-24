package com.example.bottomnavbarwithnavgation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.bottomnavbarwithnavgation.fragment.Dashboard;
import com.example.bottomnavbarwithnavgation.fragment.Manage;
import com.example.bottomnavbarwithnavgation.fragment.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chipNavigationBar = findViewById(R.id.btmnavBar);
        bottomNavigationView = findViewById(R.id.bottomNav);



        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.dashboard:
                        fragment = new Dashboard();
                        break;
                    case R.id.manage:
                        fragment = new Manage();
                        break;
                    case R.id.profile:
                        fragment = new Profile();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
            }
        });


    }
}