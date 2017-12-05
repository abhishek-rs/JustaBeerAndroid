package com.justagroup.justabeer;

import android.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.CardView;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.justagroup.justabeer.Fragments.CreateHangoutFragment;
import com.justagroup.justabeer.Fragments.HomeFragment;
import com.justagroup.justabeer.Fragments.MyHangoutsFragment;
import com.justagroup.justabeer.Fragments.NotificationsFragment;
import com.justagroup.justabeer.Fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        MyHangoutsFragment.OnFragmentInteractionListener,
        CreateHangoutFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener
{
    private ViewPager viewPager;
    private FirebaseAuth mAuth;

    HomeFragment homeFragment;
    MyHangoutsFragment myHangoutsFragment;
    CreateHangoutFragment createHangoutFragment;
    NotificationsFragment notificationsFragment;
    ProfileFragment profileFragment;
    MenuItem prevMenuItem;
    FirebaseDatabase db;


    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        db = FirebaseDatabase.getInstance();
        FirebaseUser curr = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference hangoutsRef = db.getReference("hangouts");
        db.getReference("users").orderByChild("email").equalTo(curr.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                pushDatatoDb(hangoutsRef, u);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     //  Map<String, Object> user = new HashMap<>();
     //   user.put("1", currentUser);


        final ActionBar ab = getSupportActionBar();
        ab.setTitle("JUST A BEER");

        final BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.setIconSize(32, 32);
        bnve.setItemHeight(100);

        bnve.setOnNavigationItemSelectedListener(
                new BottomNavigationViewEx.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch(item.getItemId()){
                            case R.id.navigation_home:
                                //mTextMessage.setText(R.string.title_home);
                                //mCardView.
                                viewPager.setCurrentItem(0);
                                ab.setTitle("JUST A BEER");//@ string not working here
                                return true;
                            case R.id.navigation_myhangouts:
                                //mTextMessage.setText(R.string.title_myhangouts);
                                viewPager.setCurrentItem(1);
                                ab.setTitle("MY HANGOUTS");
                                return true;
                            case R.id.navigation_addhangout:
                                //mTextMessage.setText(R.string.title_addhangout);
                                viewPager.setCurrentItem(2);
                                ab.setTitle("CREATE HANGOUT");
                                return true;
                            case R.id.navigation_notifications:
                                //mTextMessage.setText(R.string.title_notifications);
                                viewPager.setCurrentItem(3);
                                ab.setTitle("NOTIFICATIONS");
                                return true;
                            case R.id.navigation_profile:
                                viewPager.setCurrentItem(4);
                                //mTextMessage.setText(R.string.title_profile);
                                ab.setTitle("PROFILE");
                                return true;
                        }
                        return false;
                    }
                }
        );

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bnve.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bnve.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bnve.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        setupViewPager(viewPager);
    }

    public void pushDatatoDb(DatabaseReference ref, User u){
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
        Date fromTime = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 4);
        Date toTime = cal.getTime();
        DatabaseReference newRef = ref.push();
        List<String> pendingUsers = new ArrayList<String>();
        pendingUsers.add("AFO0hncym7cZ2qZS1pA7AZyYdzW2");
        List<String> confirmedUsers = new ArrayList<String>();
        confirmedUsers.add("BUzTZqwGPfgP296lywz4wryllmj2");
        List<String> rejectedUsers = new ArrayList<String>();
        rejectedUsers.add("");
        List<String> commentIds = new ArrayList<String>();
        commentIds.add("");
        List<String> privateMessageIds = new ArrayList<String>();
        privateMessageIds.add("");
        Hangout hg1 = new Hangout(newRef.getKey(),
                                    "Beer with Buds",
                                        fromTime,
                                        toTime,
                                    "Alright Alright Alright!",
                                        new LatLng(59.3123021,18.0314929),
                                        Hangout.EventType.Beer,
                                        u.getId(),
                                        pendingUsers,
                                        confirmedUsers,
                                        rejectedUsers,
                                        commentIds,
                                        privateMessageIds);
        newRef.setValue(hg1);
    }

    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e("User loggged in: ", currentUser.getEmail());
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        myHangoutsFragment = new MyHangoutsFragment();
        createHangoutFragment = new CreateHangoutFragment();
        notificationsFragment = new NotificationsFragment();
        profileFragment = new ProfileFragment();
        adapter.addFragment(homeFragment);
        adapter.addFragment(myHangoutsFragment);
        adapter.addFragment(createHangoutFragment);
        adapter.addFragment(notificationsFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);

    }

}


