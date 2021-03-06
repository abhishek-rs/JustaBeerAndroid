package com.justagroup.justabeer;

import android.app.Fragment;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import java.lang.reflect.Field;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        //Log.d("test", "hello");

        db = FirebaseDatabase.getInstance();
        FirebaseUser curr = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference hangoutsRef = db.getReference("hangouts");
     //   List<Hangout> data = getHangoutsFromDb(hangoutsRef);
/*
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
*/
        //-------- TOP NAV ----------
        //Custom toolbar with filter drawer
        final Toolbar toolbar = (Toolbar) findViewById(R.id.top_nav);
        final TextView topTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();


        //-------- BOTTOM NAV ----------
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
                                viewPager.setCurrentItem(0);
                                topTitle.setText(R.string.title_home);
                                toolbar.inflateMenu(R.menu.menu_settings);
                                return true;
                            case R.id.navigation_myhangouts:
                                viewPager.setCurrentItem(1);
                                topTitle.setText(R.string.title_myhangouts);
                                toolbar.getMenu().clear();
                                return true;
                            case R.id.navigation_addhangout:
                                viewPager.setCurrentItem(2);
                                topTitle.setText(R.string.title_addhangout);
                                toolbar.getMenu().clear();
                                return true;
                            case R.id.navigation_notifications:
                                viewPager.setCurrentItem(3);
                                topTitle.setText(R.string.title_notifications);
                                toolbar.getMenu().clear();
                                return true;
                            case R.id.navigation_profile:
                                viewPager.setCurrentItem(4);
                                topTitle.setText(R.string.title_profile);
                                toolbar.getMenu().clear();
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

    public List<Hangout> getHangoutsFromDb(DatabaseReference ref){
        final List<Hangout> hgs = new ArrayList<Hangout>();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
         //       Hangout hg = dataSnapshot.getValue(Hangout.class);
        //        hgs.add(hg);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Read from firebase", "Does not work man!");
            }
        });
        return hgs;
    }

    public void pushDatatoDb(DatabaseReference ref, User u){
    /*    Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 3); // adds one hour
        Date fromTime = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 6);
        Date toTime = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strFromDate = dateFormat.format(fromTime).toString();
        String strToDate = dateFormat.format(toTime).toString();
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
                                    "Hangout in Sodermalm",
                                        strFromDate,
                                        strToDate,
                                    "Alright Alright Alright!",
                                        59.3123021,
                                        18.0314929,
                                        Hangout.EventType.Coffee,
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        pendingUsers,
                                        confirmedUsers,
                                        rejectedUsers,
                                        commentIds,
                                        privateMessageIds);
        newRef.setValue(hg1);
        DatabaseReference newRef2 = ref.push();
        Hangout hg2 = new Hangout(newRef2.getKey(),
                "Hangout in Kista",
                strFromDate,
                strToDate,
                "Let's have fun y'all!",
                59.3123021,
                18.0314929,
                Hangout.EventType.Food,
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                pendingUsers,
                confirmedUsers,
                rejectedUsers,
                commentIds,
                privateMessageIds);
        newRef2.setValue(hg2); */

        DatabaseReference confirmedRef = db.getReference("confirmedRequests");
        DatabaseReference pendingRef = db.getReference("pendingRequests");

        ConfirmedRequest cr = new ConfirmedRequest("-L-i7Sna4C8zm_LNRUVr", "BUzTZqwGPfgP296lywz4wryllmj2");
        ConfirmedRequest cr1 = new ConfirmedRequest("-L-iBxTyDP-PupQ7Ecr1", "BUzTZqwGPfgP296lywz4wryllmj2");
        ConfirmedRequest cr2 = new ConfirmedRequest("-L-iBxUKmDlrN_wO3J_L", "tGXXebMZaoYHwuSogRUePyF0hwj1");
        ConfirmedRequest cr3 = new ConfirmedRequest("-L-iCm9AjSCndEevCdwY", "BUzTZqwGPfgP296lywz4wryllmj2");
        PendingRequest pr = new PendingRequest("-L-i7Sna4C8zm_LNRUVr", "AFO0hncym7cZ2qZS1pA7AZyYdzW2");
        PendingRequest pr2 = new PendingRequest("-L-iBxTyDP-PupQ7Ecr1", "AFO0hncym7cZ2qZS1pA7AZyYdzW2");
        PendingRequest pr3 = new PendingRequest("-L-iBxUKmDlrN_wO3J_L", "BUzTZqwGPfgP296lywz4wryllmj2");
        PendingRequest pr4 = new PendingRequest("-L-iCm9AjSCndEevCdwY", "AFO0hncym7cZ2qZS1pA7AZyYdzW2");
        confirmedRef.push().setValue(cr);
        confirmedRef.push().setValue(cr1);
        confirmedRef.push().setValue(cr2);
        confirmedRef.push().setValue(cr3);
        pendingRef.push().setValue(pr);
        pendingRef.push().setValue(pr2);
        pendingRef.push().setValue(pr3);
        pendingRef.push().setValue(pr4);
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

    //------- SIDE NAVBAR SETTINGS ------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter1:
                //filter nearest
            case R.id.filter2:
                //filter by popularity (amount of people joining)
        }
        return super.onOptionsItemSelected(item);
    }
}


