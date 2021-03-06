package com.example.cheq.Users;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cheq.Login.LoginActivity;
import com.example.cheq.MainActivity;
import com.example.cheq.Managers.SessionManager;
import com.example.cheq.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        BottomNavigationView bottomNav = findViewById(R.id.btm_nav_user);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserHomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new UserHomeFragment();
                    break;
                case R.id.nav_activities:
                    selectedFragment = new UserActivitiesFragment();
                    break;
                case R.id.nav_account:
                    selectedFragment = new UserAccountFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.btm_nav_users, menu);
        return true;
    }

    public void logout(View view) {
        // Remove session and open login screen
        SessionManager sessionManager = SessionManager.getSessionManager(UserActivity.this);
        sessionManager.removeSession();
        moveToLoginActivity();
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}