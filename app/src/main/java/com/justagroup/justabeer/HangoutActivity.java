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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

    FirebaseRecyclerAdapter commentCardAdapter, messageCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout);
     //   final Hangout hangout = getIntent().getExtras().getParcelable("hangout");
        final String hangoutId = getIntent().getExtras().getString("hangoutid");
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final FirebaseUser curr = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference hangoutsRef = db.getReference("hangouts");
        final DatabaseReference usersRef = db.getReference("users");
        final DatabaseReference commentsRef = db.getReference("comments");
        final DatabaseReference messagesRef = db.getReference("messages");
        final DatabaseReference notificationsRef = db.getReference("notifications");
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
        final RecyclerView mCommentCardRecyclerView = (RecyclerView) findViewById(R.id.comment_card_recycler_view);
        final RecyclerView mMessageCardRecyclerView = (RecyclerView) findViewById(R.id.messages_card_recycler_view);
        mCommentCardRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager mCommentCardLayoutManager = new android.support.v7.widget.LinearLayoutManager(this);
        mCommentCardRecyclerView.setLayoutManager(mCommentCardLayoutManager);
        mMessageCardRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager mMessageCardLayoutManager = new android.support.v7.widget.LinearLayoutManager(this);
        mMessageCardRecyclerView.setLayoutManager(mMessageCardLayoutManager);
        final EditText commentText = (EditText) findViewById(R.id.edittext_commentbox);
        final Button commentButton = (Button) findViewById(R.id.button_commentbox_send);
        final EditText messageText = (EditText) findViewById(R.id.edittext_messagesbox);
        final Button messageButton = (Button) findViewById(R.id.button_messagesbox_send);

        Query commentsQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("comments").orderByChild("hangoutId").equalTo(hangoutId);
        final FirebaseRecyclerOptions<Comment> commentsOptions =
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(commentsQuery, Comment.class)
                        .build();

        Query messagesQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("messages").orderByChild("hangoutId").equalTo(hangoutId);
        final FirebaseRecyclerOptions<PrivateMessage> messagesOptions =
                new FirebaseRecyclerOptions.Builder<PrivateMessage>()
                        .setQuery(messagesQuery, PrivateMessage.class)
                        .build();

        hangoutsRef.orderByChild("id").equalTo(hangoutId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Hangout h = dataSnapshot.getChildren().iterator().next().getValue(Hangout.class);
                Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(tb);
                tb.setTitle(h.getTitle());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                final Boolean isConfirmedUser = h.confirmedUsers.contains(curr.getUid());
                TextView message = (TextView) findViewById(R.id.unconfirmed_user_message);
                if(isConfirmedUser){
                    message.setVisibility(View.GONE);
                    mMessageCardRecyclerView.setVisibility(View.VISIBLE);
                }
                else{
                    message.setVisibility(View.VISIBLE);
                    mMessageCardRecyclerView.setVisibility(View.GONE);
                }

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

                commentButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(!(commentText.getText().toString()).equals("")){
                            DatabaseReference newCommentRef = commentsRef.push();
                            Calendar cal = Calendar.getInstance(); // creates calendar
                            cal.setTime(new Date());
                            Date commentTime = cal.getTime();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            String strCommentTime = dateFormat.format(commentTime).toString();
                            Comment c = new Comment(newCommentRef.getKey(), curr.getUid(), hangoutId, strCommentTime, commentText.getText().toString());
                            newCommentRef.setValue(c);
                            commentText.getText().clear();

                            if(!h.getOwner().equals(curr.getUid())){
                                DatabaseReference newNotificationRef = notificationsRef.push();
                                Notification n = new Notification(newNotificationRef.getKey(), curr.getUid(), h.getOwner(), strCommentTime, Notification.NotificationType.Comment);
                                newNotificationRef.setValue(n);
                            }
                        }
                    }
                });

                messageButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(isConfirmedUser){
                            if(!(messageText.getText().toString()).equals("")){
                                DatabaseReference newMessageRef = messagesRef.push();
                                Calendar cal = Calendar.getInstance(); // creates calendar
                                cal.setTime(new Date());
                                Date messageTime = cal.getTime();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String strMessageTime = dateFormat.format(messageTime).toString();
                                PrivateMessage p = new PrivateMessage(newMessageRef.getKey(), curr.getUid(), hangoutId, strMessageTime, messageText.getText().toString());
                                newMessageRef.setValue(p);
                                messageText.getText().clear();

                                if(!h.getOwner().equals(curr.getUid())){
                                    DatabaseReference newNotificationRef = notificationsRef.push();
                                    Notification n = new Notification(newNotificationRef.getKey(), curr.getUid(), h.getOwner(), strMessageTime, Notification.NotificationType.PrivateMessage);
                                    newNotificationRef.setValue(n);
                                }
                            }
                        }
                        else{
                            Toast.makeText(HangoutActivity.this, "You cannot send messages here just yet.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        commentCardAdapter = new FirebaseRecyclerAdapter<Comment, CommentCardHolder>(commentsOptions) {
            @Override
            public CommentCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_card, parent, false);
                return new CommentCardHolder(view);
            }

            @Override
            protected void onBindViewHolder(final CommentCardHolder holder, int position, final Comment model) {

                usersRef.orderByChild("id").equalTo(model.getOwner()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User u = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                        if(u != null) {
                            holder.userName.setText(u.getFullName());
                            if(!u.getPhoto().equals("")) {
                                Picasso
                                        .with(HangoutActivity.this)
                                        .load(u.getPhoto())
                                        .into(holder.userImage);
                            }
                        }
                        holder.userImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(u != null) {
                                    Intent intent = new Intent(HangoutActivity.this, ProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra("user", u);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(HangoutActivity.this, "User is null", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                holder.content.setText(model.getContent());
                holder.timestamp.setText(model.getTimestamp());
            }

        };

        commentCardAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = commentCardAdapter.getItemCount();
                int lastVisiblePosition =
                        mCommentCardLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mCommentCardRecyclerView.scrollToPosition(0);
                }
            }
        });

        messageCardAdapter = new FirebaseRecyclerAdapter<PrivateMessage, CommentCardHolder>(messagesOptions) {
            @Override
            public CommentCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_card, parent, false);
                return new CommentCardHolder(view);
            }

            @Override
            protected void onBindViewHolder(final CommentCardHolder holder, int position, final PrivateMessage model) {

                db.getReference("users").orderByChild("id").equalTo(model.getOwner()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User u = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                        if(u != null) {
                            holder.userName.setText(u.getFullName());
                            if(!u.getPhoto().equals("")) {
                                Picasso
                                        .with(HangoutActivity.this)
                                        .load(u.getPhoto())
                                        .into(holder.userImage);
                            }
                        }
                        holder.userImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(u != null) {
                                    Intent intent = new Intent(HangoutActivity.this, ProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra("user", u);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(HangoutActivity.this, "User is null", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                holder.content.setText(model.getContent());
                holder.timestamp.setText(model.getTimestamp());
            }

        };

        messageCardAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = messageCardAdapter.getItemCount();
                int lastVisiblePosition =
                        mMessageCardLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageCardRecyclerView.scrollToPosition(0);
                }
            }
        });

        mCommentCardRecyclerView.setAdapter(commentCardAdapter);
        mMessageCardRecyclerView.setAdapter(messageCardAdapter);

        TabLayout commentTabLayout = (TabLayout) findViewById(R.id.commentTabs);
        TabLayout.Tab commentsTab = commentTabLayout.getTabAt(0);
        TabLayout.Tab messagesTab = commentTabLayout.getTabAt(1);

        final FloatingActionButton joiningButton = (FloatingActionButton) findViewById(R.id.fab);
        joiningButton.setImageResource(R.drawable.join);
        final Button cancelHangoutButton = (Button) findViewById(R.id.cancelHangoutBtn);
        cancelHangoutButton.setVisibility(View.GONE);//show only when request sent

        final RelativeLayout commentsLayout = (RelativeLayout) findViewById(R.id.comments);
        final RelativeLayout messagesLayout = (RelativeLayout) findViewById(R.id.messages);

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
    public void onStart() {
        super.onStart();
        commentCardAdapter.startListening();
        messageCardAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        commentCardAdapter.stopListening();
        messageCardAdapter.stopListening();
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
