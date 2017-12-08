package com.justagroup.justabeer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName;
    private TextView userAge;
    private TextView userGender;
    private TextView userAbout;
    //private TextView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = (User) getIntent().getParcelableExtra("user");
        Log.d("user", "user = " + user);
        //Toast.makeText(this, "User = " + user.getFullName(), Toast.LENGTH_SHORT).show();
        userName = (TextView) findViewById(R.id.user_card_title);
        userAge = (TextView) findViewById(R.id.user_age);
        userGender = (TextView) findViewById(R.id.user_gender);
        userAbout = (TextView) findViewById(R.id.user_about);
        //userImage = (ImageView).findViewById(R.id.user_image_view);


        userName.setText(user.getFullName());
        userAge.setText(Integer.toString(user.getAge()));
        userGender.setText("Not defined in db");
        userAbout.setText(user.getAbout());
/*
        if(!user.getPhoto().equals("")) {
            Picasso
                    //.with(ProfileActivity.this)
                    .with(this)
                    .load(user.getPhoto())
                    .into(image);

    }
*/
    }
}
