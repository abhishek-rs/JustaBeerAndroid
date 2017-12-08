package com.justagroup.justabeer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.justagroup.justabeer.CardHolder;
import com.justagroup.justabeer.ConfirmedRequest;
import com.justagroup.justabeer.Hangout;
import com.justagroup.justabeer.HangoutActivity;
import com.justagroup.justabeer.PendingRequest;
import com.justagroup.justabeer.R;
import com.justagroup.justabeer.User;

public class MyHangoutsFragment extends Fragment {

    FirebaseRecyclerAdapter joiningAdapter, pendingAdapter, hostingAdapter;

    private OnFragmentInteractionListener mListener;

    public MyHangoutsFragment() {
        // Required empty public constructor
    }

    public static MyHangoutsFragment newInstance(String param1, String param2) {
        MyHangoutsFragment fragment = new MyHangoutsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_hangouts, container, false);


        final FirebaseDatabase db = FirebaseDatabase.getInstance();

        FirebaseUser curr = FirebaseAuth.getInstance().getCurrentUser();
        Query confirmedQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("confirmedRequests").orderByChild("guestId").equalTo(curr.getUid());
        Query pendingQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("pendingRequests").orderByChild("guestId").equalTo(curr.getUid());
        Query hostingQuery = FirebaseDatabase.getInstance()
                .getReference()
                .child("hangouts").orderByChild("owner").equalTo(curr.getUid());

        FirebaseRecyclerOptions<ConfirmedRequest> joiningOptions =
                new FirebaseRecyclerOptions.Builder<ConfirmedRequest>()
                        .setQuery(confirmedQuery, ConfirmedRequest.class)
                        .build();

        FirebaseRecyclerOptions<PendingRequest> pendingOptions =
                new FirebaseRecyclerOptions.Builder<PendingRequest>()
                        .setQuery(pendingQuery, PendingRequest.class)
                        .build();

        FirebaseRecyclerOptions<Hangout> hostingOptions =
                new FirebaseRecyclerOptions.Builder<Hangout>()
                        .setQuery(hostingQuery, Hangout.class)
                        .build();

        final RecyclerView joiningRecyclerView = (RecyclerView) view.findViewById(R.id.joining_recycler_view);
        final RecyclerView pendingRecyclerView = (RecyclerView) view.findViewById(R.id.pending_recycler_view);
        final RecyclerView hostingRecyclerView = (RecyclerView) view.findViewById(R.id.hosting_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        joiningRecyclerView.setHasFixedSize(true);
        pendingRecyclerView.setHasFixedSize(true);
        hostingRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        final LinearLayoutManager joiningLayoutManager = new android.support.v7.widget.LinearLayoutManager(getActivity());
        joiningRecyclerView.setLayoutManager(joiningLayoutManager);
        final LinearLayoutManager pendingLayoutManager = new android.support.v7.widget.LinearLayoutManager(getActivity());
        pendingRecyclerView.setLayoutManager(pendingLayoutManager);
        final LinearLayoutManager hostingLayoutManager = new android.support.v7.widget.LinearLayoutManager(getActivity());
        hostingRecyclerView.setLayoutManager(hostingLayoutManager);

        // specify an adapter (see also next example)

        joiningAdapter = new FirebaseRecyclerAdapter<ConfirmedRequest, CardHolder>(joiningOptions) {
            @Override
            public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hangout_card, parent, false);
                return new CardHolder(view);
            }

            @Override
            protected void onBindViewHolder(final CardHolder holder, int position, ConfirmedRequest model) {

                db.getReference("hangouts").orderByChild("id").equalTo(model.getHangoutId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Hangout h = dataSnapshot.getChildren().iterator().next().getValue(Hangout.class);

                        db.getReference("users").orderByChild("id").equalTo(h.getOwner()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User u = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                                if(u != null) holder.owner.setText(u.getFullName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        holder.hangoutName.setText(h.getTitle());
                        switch(h.getType()){
                            case Beer:
                                holder.hangoutImage.setImageResource(R.drawable.bar1);
                                break;
                            case Food:
                                holder.hangoutImage.setImageResource(R.drawable.food2);
                                break;
                            case Coffee:
                                holder.hangoutImage.setImageResource(R.drawable.coffee2);
                            default :
                                holder.hangoutImage.setImageResource(R.drawable.bar3);
                                break;
                        }
                        //    holder.owner.setText(model.getOwner());
                        holder.desc.setText(h.getDescription());
                        holder.attendees.setText(Integer.toString(h.getConfirmedUsers().size()));
                        holder.comments.setText(Integer.toString(h.getCommentIds().size() - 1));
                        holder.seeMore.setText("Check it out");
                        holder.seeMore.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), HangoutActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("hangout", h);
                                startActivity(intent);
                            }});
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        };

        joiningAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = joiningAdapter.getItemCount();
                int lastVisiblePosition =
                        joiningLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    joiningRecyclerView.scrollToPosition(0);
                }
            }
        });

        pendingAdapter = new FirebaseRecyclerAdapter<PendingRequest, CardHolder>(pendingOptions) {
            @Override
            public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hangout_card, parent, false);
                return new CardHolder(view);
            }

            @Override
            protected void onBindViewHolder(final CardHolder holder, int position, PendingRequest model) {

                db.getReference("hangouts").orderByChild("id").equalTo(model.getHangoutId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Hangout h = dataSnapshot.getChildren().iterator().next().getValue(Hangout.class);

                        db.getReference("users").orderByChild("id").equalTo(h.getOwner()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User u = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                                if(u != null) holder.owner.setText(u.getFullName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        holder.hangoutName.setText(h.getTitle());
                        switch(h.getType()){
                            case Beer:
                                holder.hangoutImage.setImageResource(R.drawable.bar1);
                                break;
                            case Food:
                                holder.hangoutImage.setImageResource(R.drawable.food2);
                                break;
                            case Coffee:
                                holder.hangoutImage.setImageResource(R.drawable.coffee2);
                            default :
                                holder.hangoutImage.setImageResource(R.drawable.bar3);
                                break;
                        }
                        //    holder.owner.setText(model.getOwner());
                        holder.desc.setText(h.getDescription());
                        holder.attendees.setText(Integer.toString(h.getConfirmedUsers().size()));
                        holder.comments.setText(Integer.toString(h.getCommentIds().size() - 1));
                        holder.seeMore.setText("Check it out");
                        holder.seeMore.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), HangoutActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("hangout", h);
                                startActivity(intent);
                            }});
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        };

        pendingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = pendingAdapter.getItemCount();
                int lastVisiblePosition =
                        pendingLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    pendingRecyclerView.scrollToPosition(0);
                }
            }
        });

        hostingAdapter = new FirebaseRecyclerAdapter<Hangout, CardHolder>(hostingOptions) {
            @Override
            public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hangout_card, parent, false);
                return new CardHolder(view);
            }

            @Override
            protected void onBindViewHolder(final CardHolder holder, int position, final Hangout model) {

                db.getReference("users").orderByChild("id").equalTo(model.getOwner()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                        if(u != null) holder.owner.setText(u.getFullName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                holder.hangoutName.setText(model.getTitle());
                switch(model.getType()){
                    case Beer:
                        holder.hangoutImage.setImageResource(R.drawable.bar1);
                        break;
                    case Food:
                        holder.hangoutImage.setImageResource(R.drawable.food2);
                        break;
                    case Coffee:
                        holder.hangoutImage.setImageResource(R.drawable.coffee2);
                    default :
                        holder.hangoutImage.setImageResource(R.drawable.bar3);
                        break;
                }
                //    holder.owner.setText(model.getOwner());
                holder.desc.setText(model.getDescription());
                holder.attendees.setText(Integer.toString(model.getConfirmedUsers().size()));
                holder.comments.setText(Integer.toString(model.getCommentIds().size() - 1));
                holder.seeMore.setText("Check it out");
                holder.seeMore.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HangoutActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("hangout", model);
                        startActivity(intent);
                    }});
            }

        };

        hostingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = hostingAdapter.getItemCount();
                int lastVisiblePosition =
                        hostingLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    hostingRecyclerView.scrollToPosition(0);
                }
            }
        });

        joiningRecyclerView.setAdapter(joiningAdapter);
        pendingRecyclerView.setAdapter(pendingAdapter);
        hostingRecyclerView.setAdapter(hostingAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        TabLayout.Tab joiningTab = tabLayout.getTabAt(0);
        TabLayout.Tab pendingTab = tabLayout.getTabAt(1);
        TabLayout.Tab hostingTab = tabLayout.getTabAt(2);

        final LinearLayout joiningContent = (LinearLayout) view.findViewById(R.id.joiningContent);
        final LinearLayout pendingContent = (LinearLayout) view.findViewById(R.id.pendingContent);
        final LinearLayout hostingContent = (LinearLayout) view.findViewById(R.id.hostingContent);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("testing", "selected tab:" + tab.getText());
                if (tab.getText().equals("JOINING")) {
                    joiningContent.setVisibility(View.VISIBLE);
                    pendingContent.setVisibility(View.GONE);
                    hostingContent.setVisibility(View.GONE);
                } else if (tab.getText().equals("PENDING")) {
                    joiningContent.setVisibility(View.GONE);
                    pendingContent.setVisibility(View.VISIBLE);
                    hostingContent.setVisibility(View.GONE);
                } else {
                    joiningContent.setVisibility(View.GONE);
                    pendingContent.setVisibility(View.GONE);
                    hostingContent.setVisibility(View.VISIBLE);
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

        return view;
    }

    /*Tab logic
    public void onClick(View v) {
        v.findViewById(R.id.joiningContent).setVisibility(View.VISIBLE);
        v.findViewById(R.id.pendingContent).setVisibility(View.VISIBLE);
        v.findViewById(R.id.hostingContent).setVisibility(View.VISIBLE);
    }*/


    // TODO: Rename method, update argument and hook method into UI event
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        joiningAdapter.startListening();
        pendingAdapter.startListening();
        hostingAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        joiningAdapter.stopListening();
        pendingAdapter.stopListening();
        hostingAdapter.stopListening();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
