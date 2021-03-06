package com.example.cheq.Login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cheq.Managers.FirebaseManager;
import com.example.cheq.MainActivity;
import com.example.cheq.Managers.SessionManager;
import com.example.cheq.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // Views
    EditText inputPhone;
    Button phoneContinueBtn;
    TextView loginMsg;
    TextView loginPrompt;
    ProgressBar loginProgressBar;

    // Firebase
    FirebaseManager firebaseManager;

    String userPhone;

    final String USERPHONEKEY = "userPhone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hooks
        inputPhone = (EditText) findViewById(R.id.inputPhone);
        loginMsg = findViewById(R.id.titleMsg);
        loginPrompt = findViewById(R.id.loginPrompt);
        phoneContinueBtn = (Button) findViewById(R.id.phoneContinueBtn);
        phoneContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });
        loginProgressBar = findViewById(R.id.loginProgressBar);

        firebaseManager = new FirebaseManager();
    }

    /**
     * This method checks whether the user is an existing or new user based on the userPhone input
     */
    public void checkUser() {
        // Set progress bar to visible
        loginProgressBar.setVisibility(View.VISIBLE);

        // Get user phone input
        userPhone = inputPhone.getText().toString();

        DatabaseReference rootRef = firebaseManager.rootRef;

        // Check if the phone number input is valid
        if (InputValidation.isValidNumber(userPhone)) {

            rootRef.child("Users").child(userPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    loginProgressBar.setVisibility(View.GONE);
                    if (snapshot.exists()) {
                        moveToPasswordActivity();
                    } else {
                        moveToRegistrationActivity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loginProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                    Log.d("Error", error.getMessage());
                }
            });
        }
        else {
            loginProgressBar.setVisibility(View.GONE);
            // Display error message if phone input is invalid
            Toast.makeText(LoginActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveToPasswordActivity() {
        Intent intent = new Intent(LoginActivity.this, PasswordActivity.class);
        intent.putExtra(USERPHONEKEY, userPhone);
        Pair transition = new Pair<View, String>(phoneContinueBtn, "transitionContinueBtn");
        // Check if SDK version is high enough for animation
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, transition);
            startActivity(intent, options.toBundle());
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            startActivity(intent);
        }
    }

    public void moveToRegistrationActivity() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        intent.putExtra(USERPHONEKEY, userPhone);
        Pair transition = new Pair<View, String>(phoneContinueBtn, "transitionContinueBtn");
        // Check if SDK version is high enough for animation
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, transition);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }


}
