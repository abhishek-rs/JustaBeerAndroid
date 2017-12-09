package com.justagroup.justabeer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HangoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   final Hangout hangout = getIntent().getExtras().getParcelable("hangout");
        final String hangoutId = getIntent().getExtras().getString("hangoutid");
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser curr = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference hangoutsRef = db.getReference("hangouts");
        final DatabaseReference usersRef = db.getReference("users");
        List<String> attendees = new ArrayList<String>();
        final ListView attendeeList = findViewById(R.id.attendeeList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.hangout_attendees, attendees);
        attendeeList.setAdapter(adapter);
        final ImageView backdrop = findViewById(R.id.backdrop);
        final TextView date = findViewById(R.id.hangout_date);
        final TextView type = findViewById(R.id.hangout_type);
        final TextView place = findViewById(R.id.hangout_place);
        final Button mapBtn = findViewById(R.id.mapBtn);
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        hangoutsRef.orderByChild("id").equalTo(hangoutId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Hangout h = dataSnapshot.getChildren().iterator().next().getValue(Hangout.class);
                toolbar.setTitle(h.getTitle());
                switch(h.getType()) {
                    case Beer:
                        backdrop.setImageResource(R.drawable.bar2);
                        break;
                    case Food:
                        backdrop.setImageResource(R.drawable.food3);
                        break;
                    case Coffee:
                        backdrop.setImageResource(R.drawable.coffee1);
                        break;
                    default:
                        backdrop.setImageResource(R.drawable.bar1);
                        break;
                }

                try {
                    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    DateFormat format2 = new SimpleDateFormat("HH:mm");
                    Date toTime = format.parse(h.getToTime());
                    String toTimeStr = format2.format(toTime).toString();
                    Date fromTime = format.parse(h.getFromTime());
                    String fromTimeStr = format2.format(fromTime).toString();
                    date.setText(fromTimeStr + " - " + toTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                type.setText(h.getType().name());

                try{
                    List<Address> addresses = geocoder.getFromLocation(h.getLat(), h.getLng(), 1);
                    String address = addresses.get(0).getAddressLine(0);
                    place.setText(address);
                    mapBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri gmmIntentUri = Uri.parse("geo:" + Double.toString(h.getLat()) + "," + Double.toString(h.getLng()) + "?q=bars");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        }
                    });

                }
                catch (IOException e){
                    System.out.print(e);
                }

                for (int i=0; i<h.confirmedUsers.size(); i++){
                    db.getReference("users").orderByChild("id").equalTo(h.confirmedUsers.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User u = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                            adapter.add(u.getFullName());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TabLayout commentTabLayout = (TabLayout) findViewById(R.id.commentTabs);
        TabLayout.Tab commentsTab = commentTabLayout.getTabAt(0);
        TabLayout.Tab messagesTab = commentTabLayout.getTabAt(1);

        final FloatingActionButton joiningButton = (FloatingActionButton) findViewById(R.id.fab);
        joiningButton.setImageResource(R.drawable.join);
        final Button cancelHangoutButton = (Button) findViewById(R.id.cancelHangoutBtn);
        cancelHangoutButton.setVisibility(View.GONE);//show only when request sent

        final LinearLayout commentsLayout = (LinearLayout) findViewById(R.id.comments);
        final LinearLayout messagesLayout = (LinearLayout) findViewById(R.id.messages);

        commentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("testing", "selected tab:" + tab.getText());
                if (tab.getText().equals("Comments")) {
                    commentsLayout.setVisibility(View.VISIBLE);
                    messagesLayout.setVisibility(View.GONE);
                } else {
                    commentsLayout.setVisibility(View.GONE);
                    messagesLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("testing", "unselected tab:" + tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("testing", "reselected tab:" + tab.getText());
            }
        });

        final Context mContext = this;
        joiningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //join event
                    Snackbar.make(view, "You've signed up for the event. Await confirmation", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    joiningButton.setImageResource(R.drawable.hourglass);
                    cancelHangoutButton.setVisibility(View.VISIBLE);
                //TODO: send info to db
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cancelHangoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //cancel event
                Snackbar.make(view, "Your request has been cancelled", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                joiningButton.setImageResource(R.drawable.join);
                cancelHangoutButton.setVisibility(View.GONE);
                //TODO: send info to db
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(HangoutActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return;
    }

}
