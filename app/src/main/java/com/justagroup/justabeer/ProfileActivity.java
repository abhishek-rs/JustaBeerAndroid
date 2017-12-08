package com.justagroup.justabeer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = (User) getIntent().getParcelableExtra("user");
        Log.d("user", "user = " + user);
        Toast.makeText(this, "User = " + user.getFullName(), Toast.LENGTH_SHORT).show();
    }
}
