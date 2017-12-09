package com.justagroup.justabeer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "LOAD_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = (User) getIntent().getParcelableExtra("user");

        if(user != null) {
            Log.d(TAG, "user = " + user);
            //Toast.makeText(this, "User = " + user.getFullName(), Toast.LENGTH_SHORT).show();

            //Profile Text content
            //list of components in format <textfield, text for that textfield>
            Map<TextView, String> profileComponents = new HashMap<>();
            profileComponents.put((TextView) findViewById(R.id.user_card_title), user.getFullName());
            profileComponents.put((TextView) findViewById(R.id.user_age), user.getAge().toString());
            profileComponents.put((TextView) findViewById(R.id.user_gender), user.getGender());
            profileComponents.put((TextView) findViewById(R.id.user_about), user.getAbout());

            for (Map.Entry<TextView, String> component : profileComponents.entrySet()) {
                if(component.getKey() != null){
                    if(component.getValue() != null && !component.getValue().isEmpty()){
                        component.getKey().setText(component.getValue());
                    } else {
                        component.getKey().setText("Not available");
                    }
                } else {
                    Log.d(TAG, "text component not defined");
                }
            }

            //Profile picture
            ImageView profilePic= (ImageView) findViewById(R.id.imageView);

            //add profile picture if available
            if (profilePic != null) {
                if(user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                    Picasso
                            .with(this)
                            .load(user.getPhoto())
                            .into(profilePic);
                } else {
                    //if pic not found set logo as pic
                    Picasso
                            .with(this)
                            .load(R.drawable.logo)
                            .into(profilePic);
                }
            } else {
                Log.d(TAG, "Image not defined");
            }

        } else {
            Log.d(TAG, "User not defined");
        }
    }
}
