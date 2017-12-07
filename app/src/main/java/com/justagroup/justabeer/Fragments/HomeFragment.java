package com.justagroup.justabeer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.justagroup.justabeer.CardHolder;
import com.justagroup.justabeer.Hangout;
import com.justagroup.justabeer.HangoutActivity;
import com.justagroup.justabeer.HomeActivity;
import com.justagroup.justabeer.LoginActivity;
import com.justagroup.justabeer.R;
import com.justagroup.justabeer.User;

public class HomeFragment extends Fragment {

    FirebaseRecyclerAdapter cardAdapter;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("hangouts");
        FirebaseRecyclerOptions<Hangout> options =
                    new FirebaseRecyclerOptions.Builder<Hangout>()
                        .setQuery(query, Hangout.class)
                        .build();

        final RecyclerView mCardRecyclerView = (RecyclerView) view.findViewById(R.id.hangout_card_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mCardRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        final LinearLayoutManager mCardLayoutManager = new android.support.v7.widget.LinearLayoutManager(getActivity());
        mCardRecyclerView.setLayoutManager(mCardLayoutManager);

        // specify an adapter (see also next example)

        cardAdapter = new FirebaseRecyclerAdapter<Hangout, CardHolder>(options) {
            @Override
            public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hangout_card, parent, false);
                return new CardHolder(view);
            }

            @Override
            protected void onBindViewHolder(final CardHolder holder, int position, Hangout model) {

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
                        startActivity(intent);
                    }});
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
