package com.justagroup.justabeer;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
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

        final BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.setIconSize(32, 32);

        bnve.setOnNavigationItemSelectedListener(
                new BottomNavigationViewEx.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch(item.getItemId()){
                            case R.id.navigation_home:
                                //mTextMessage.setText(R.string.title_home);
                                //mCardView.
                                viewPager.setCurrentItem(0);
                                return true;
                            case R.id.navigation_myhangouts:
                                //mTextMessage.setText(R.string.title_myhangouts);
                                viewPager.setCurrentItem(1);
                                return true;
                            case R.id.navigation_addhangout:
                                //mTextMessage.setText(R.string.title_addhangout);
                                viewPager.setCurrentItem(2);
                                return true;
                            case R.id.navigation_notifications:
                                //mTextMessage.setText(R.string.title_notifications);
                                viewPager.setCurrentItem(3);
                                return true;
                            case R.id.navigation_profile:
                                viewPager.setCurrentItem(4);
                                //mTextMessage.setText(R.string.title_profile);
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

        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
     //   BottomNavigationViewHelper.disableShiftMode(navigation);
     //   navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Will delete this stuff later
    /*    final ListView listView = (ListView) findViewById(R.id.listView);

        // Create a new Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the todoItems child items it the database
        final DatabaseReference myRef = database.getReference("todoItems");

        // Assign a listener to detect changes to the child items
        // of the database reference.
        myRef.addChildEventListener(new ChildEventListener(){

            // This function is called once for each child that exists
            // when the listener is added. Then it is called
            // each time a new child is added.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String value = dataSnapshot.getValue(String.class);
                adapter.add(value);
            }

            // This function is called each time a child item is removed.
            public void onChildRemoved(DataSnapshot dataSnapshot){
                String value = dataSnapshot.getValue(String.class);
                adapter.remove(value);
            }

            // The following functions are also required in ChildEventListener implementations.
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName){}
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName){}

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG:", "Failed to read value.", error.toException());
            }
        });

        // Add items via the Button and EditText at the bottom of the window.
        final EditText text = (EditText) findViewById(R.id.todoText);
        final Button button = (Button) findViewById(R.id.addButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Create a new child with a auto-generated ID.
                DatabaseReference childRef = myRef.push();

                // Set the child's data to the value passed in from the text box.
                childRef.setValue(text.getText().toString());

            }
        });

        // Delete items when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Query myQuery = myRef.orderByValue().equalTo((String)
                        listView.getItemAtPosition(position));

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                            firstChild.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                })
                ;}
        })
        */
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


