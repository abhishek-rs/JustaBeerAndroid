package com.justagroup.justabeer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HangoutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Beer with Andrea @ 7PM");
        setSupportActionBar(toolbar);
        ImageView backdrop = findViewById(R.id.backdrop);
        TextView date = findViewById(R.id.hangout_date);
        date.setText("7-11 PM");
        TextView place = findViewById(R.id.hangout_place);
        place.setText("Sodermalm, Stockholm");
        TextView people = findViewById(R.id.hangout_people);
        people.setText("Andrea");
        TextView type = findViewById(R.id.hangout_type);
        type.setText("Beer");

        TabLayout commentTabLayout = (TabLayout) findViewById(R.id.commentTabs);
        TabLayout.Tab commentsTab = commentTabLayout.getTabAt(0);
        TabLayout.Tab messagesTab = commentTabLayout.getTabAt(1);


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






        backdrop.setImageResource(R.drawable.bar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context mContext = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You've signed up for the event. Await confirmation", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.fab);
                fb.setImageResource(R.drawable.ic_cancel_black_24dp);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
