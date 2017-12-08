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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HangoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout);
        Hangout hangout = getIntent().getExtras().getParcelable("hangout");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(hangout.getTitle());
        setSupportActionBar(toolbar);
        ImageView backdrop = findViewById(R.id.backdrop);

        switch(hangout.getType()) {
            case Beer:
                backdrop.setImageResource(R.drawable.bar2);
                break;
            case Food:
                backdrop.setImageResource(R.drawable.food3);
                break;
            case Coffee:
                backdrop.setImageResource(R.drawable.coffee1);
            default:
                backdrop.setImageResource(R.drawable.bar1);
                break;
        }

        TextView date = findViewById(R.id.hangout_date);

        try {
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            DateFormat format2 = new SimpleDateFormat("HH:mm");
            Date toTime = format.parse(hangout.getToTime());
            String toTimeStr = format2.format(toTime).toString();
            Date fromTime = format.parse(hangout.getFromTime());
            String fromTimeStr = format2.format(fromTime).toString();
            date.setText(fromTimeStr + " - " + toTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView type = findViewById(R.id.hangout_type);
        type.setText(hangout.getType().name());

        TextView place = findViewById(R.id.hangout_place);

        TextView people = findViewById(R.id.hangout_people);


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
