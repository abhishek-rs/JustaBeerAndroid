package com.justagroup.justabeer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.justagroup.justabeer.CardHolder;
import com.justagroup.justabeer.ConfirmedRequest;
import com.justagroup.justabeer.Hangout;
import com.justagroup.justabeer.HangoutActivity;
import com.justagroup.justabeer.Notification;
import com.justagroup.justabeer.NotificationCardHolder;
import com.justagroup.justabeer.PendingRequest;
import com.justagroup.justabeer.ProfileActivity;
import com.justagroup.justabeer.R;
import com.justagroup.justabeer.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {
    FirebaseRecyclerAdapter cardAdapter;
    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() {
    }

    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final FirebaseUser curr = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference hangoutsRef = db.getReference("hangouts");
        final DatabaseReference pendingRef = db.getReference("pendingRequests");
        final DatabaseReference confirmedRef = db.getReference("confirmedRequests");
        final DatabaseReference notificationsRef = db.getReference("notifications");
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("notifications").orderByChild("toUser").equalTo(curr.getUid());
        FirebaseRecyclerOptions<Notification> options =
                new FirebaseRecyclerOptions.Builder<Notification>()
                        .setQuery(query, Notification.class)
                        .build();

        final RecyclerView mCardRecyclerView = (RecyclerView) view.findViewById(R.id.notification_card_recycler_view);
        mCardRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager mCardLayoutManager = new android.support.v7.widget.LinearLayoutManager(getActivity());
        mCardRecyclerView.setLayoutManager(mCardLayoutManager);

        cardAdapter = new FirebaseRecyclerAdapter<Notification, NotificationCardHolder>(options) {
            @Override
            public NotificationCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notification_card, parent, false);
                return new NotificationCardHolder(view);
            }

            @Override
            protected void onBindViewHolder(final NotificationCardHolder holder, int position, final Notification model) {

                db.getReference("users").orderByChild("id").equalTo(model.getFromUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User u = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                        if(u != null) {
                            switch(model.getType()){
                                case Comment:
                                    holder.commentsMessagesContent.setText(u.getFullName() + " has commented on your hangout.");
                                    break;
                                case PrivateMessage:
                                    holder.commentsMessagesContent.setText(u.getFullName() + " has sent a private message on your hangout.");
                                    break;
                                case AcceptedRequest:
                                    holder.commentsMessagesContent.setText(u.getFullName() + " has accepted your request to join hangout.");
                                    break;
                                case RejectedRequest:
                                    holder.commentsMessagesContent.setText(u.getFullName() + " has rejected your request to join hangout.");
                                    break;
                                case CancelConfirmation:
                                    holder.commentsMessagesContent.setText(u.getFullName() + " has cancelled from your hangout.");
                                    break;
                                case CancelRequest:
                                    holder.commentsMessagesContent.setText(u.getFullName() + " has cancelled the request to join your hangout.");
                                    break;
                                case Request:
                                    holder.requestsContent.setText(u.getFullName() + " has requested to join your hangout.");
                                    break;
                                default:
                            }

                            holder.notificationTime.setText(model.getTimestamp());

                            if(model.getType() == Notification.NotificationType.Request){
                                holder.commentsMessagesLayout.setVisibility(View.GONE);
                                holder.requestsLayout.setVisibility(View.VISIBLE);
                                holder.requestsProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("user", u);
                                        startActivity(intent);
                                    }
                                });
                                Calendar cal = Calendar.getInstance(); // creates calendar
                                cal.setTime(new Date());
                                Date requestTime = cal.getTime();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                final String strRequestTime = dateFormat.format(requestTime).toString();
                                holder.requestsAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hangoutsRef.orderByChild("id").equalTo(model.getHangoutId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                final Hangout h = dataSnapshot.getChildren().iterator().next().getValue(Hangout.class);

                                                List<String> currentPendingUsers = h.getPendingUsers();
                                                int pos = currentPendingUsers.indexOf(model.getFromUser());
                                                if(pos != -1) currentPendingUsers.remove(pos);
                                                h.setPendingUsers(currentPendingUsers);

                                                List<String> currentConfirmedUsers = h.getConfirmedUsers();
                                                currentConfirmedUsers.add(model.getFromUser());
                                                h.setConfirmedUsers(currentConfirmedUsers);

                                                Map<String, Object> childUpdates = new HashMap<>();
                                                childUpdates.put("/hangouts/" + h.getId(), h);
                                                db.getReference().updateChildren(childUpdates);

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        pendingRef.orderByChild("hangoutId").equalTo(model.getHangoutId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child : dataSnapshot.getChildren())
                                                {
                                                    PendingRequest c = child.getValue(PendingRequest.class);
                                                    if(c != null ) {
                                                        if(c.getGuestId().equals(model.getFromUser())){
                                                            DatabaseReference delRef = db.getReference("pendingRequests/" + child.getKey());
                                                            delRef.removeValue();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        ConfirmedRequest cr = new ConfirmedRequest(model.getHangoutId(), model.getFromUser());
                                        confirmedRef.push().setValue(cr);

                                        DatabaseReference newNotificationRef = notificationsRef.push();
                                        Notification n = new Notification(newNotificationRef.getKey(), curr.getUid(), model.getFromUser(), strRequestTime, model.getHangoutId(), Notification.NotificationType.AcceptedRequest);
                                        newNotificationRef.setValue(n);

                                        DatabaseReference delRef = db.getReference( "notifications/" + model.getId());
                                        delRef.removeValue();

                                    }
                                });
                                holder.requestsReject.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hangoutsRef.orderByChild("id").equalTo(model.getHangoutId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                final Hangout h = dataSnapshot.getChildren().iterator().next().getValue(Hangout.class);

                                                List<String> currentPendingUsers = h.getPendingUsers();
                                                int pos = currentPendingUsers.indexOf(model.getFromUser());
                                                if(pos != -1) currentPendingUsers.remove(pos);
                                                h.setPendingUsers(currentPendingUsers);

                                                List<String> currentRejectedUsers = h.getRejectedUsers();
                                                currentRejectedUsers.add(model.getFromUser());
                                                h.setRejectedUsers(currentRejectedUsers);

                                                Map<String, Object> childUpdates = new HashMap<>();
                                                childUpdates.put("/hangouts/" + h.getId(), h);
                                                db.getReference().updateChildren(childUpdates);

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        pendingRef.orderByChild("hangoutId").equalTo(model.getHangoutId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child : dataSnapshot.getChildren())
                                                {
                                                    PendingRequest c = child.getValue(PendingRequest.class);
                                                    if(c != null ) {
                                                        if(c.getGuestId().equals(model.getFromUser())){
                                                            DatabaseReference delRef = db.getReference("pendingRequests/" + child.getKey());
                                                            delRef.removeValue();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        DatabaseReference newNotificationRef = notificationsRef.push();
                                        Notification n = new Notification(newNotificationRef.getKey(), curr.getUid(), model.getFromUser(), strRequestTime, model.getHangoutId(), Notification.NotificationType.RejectedRequest);
                                        newNotificationRef.setValue(n);
                                        DatabaseReference delRef = db.getReference( "notifications/" + model.getId());
                                        delRef.removeValue();
                                    }
                                });
                            }
                            else{
                                holder.commentsMessagesLayout.setVisibility(View.VISIBLE);
                                holder.requestsLayout.setVisibility(View.GONE);
                                holder.commentsMessagesShow.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), HangoutActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("hangoutid", model.getHangoutId());
                                        startActivity(intent);
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        };

        cardAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = cardAdapter.getItemCount();
                int lastVisiblePosition =
                        mCardLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mCardRecyclerView.scrollToPosition(0);
                }
            }
        });

        mCardRecyclerView.setAdapter(cardAdapter);
        return view;

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cardAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        cardAdapter.stopListening();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
