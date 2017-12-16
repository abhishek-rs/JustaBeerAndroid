package com.justagroup.justabeer;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.justagroup.justabeer.Fragments.ProfileFragment;
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
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //----- TOOLBAR ------
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_nav);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Profile");
        //make back button to work
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String userId = user.getId();
        String currentUserId = currentUser.getUid();

        // ----- PROFILE INFO ------
        //if (!userId.equals(currentUserId)) {
            if (user != null) {
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
                    if (component.getKey() != null) {
                        if (component.getValue() != null && !component.getValue().isEmpty()) {
                            component.getKey().setText(component.getValue());
                        } else {
                            component.getKey().setText("Not available");
                        }
                    } else {
                        Log.d(TAG, "text component not defined");
                    }
                }

                //Profile picture
                ImageView profilePic = (ImageView) findViewById(R.id.imageView);

                //add profile picture if available
                if (profilePic != null) {
                    if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
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
        }/* else {
            //user is current user -> go to my profile
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ProfileFragment profile = new ProfileFragment();
            fragmentTransaction.replace(R.id.profileContainer, profile);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }*/
}
