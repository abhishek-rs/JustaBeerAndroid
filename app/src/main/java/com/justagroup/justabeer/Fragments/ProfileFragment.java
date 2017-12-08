package com.justagroup.justabeer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//new imports
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.justagroup.justabeer.HangoutActivity;
import com.justagroup.justabeer.User;
//firebase stuff

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import com.justagroup.justabeer.R;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    public TextView fullName;
    public ImageView userImage;
    public TextView age;
    public TextView about;
    public TextView gender;
    public TextView comments;
    public boolean editMode = false;
    /*edit mode
    public Button editProfile;
    public EditText editAge;
    public EditText editGender;
    public EditText editInterests;*/


    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        // My version
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Attach a listener to read the data at our users reference
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object and use the values to update the UI
                User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                TextView title = getView().findViewById(R.id.profile_card_title);
                age = getView().findViewById(R.id.profile_age);
                gender = getView().findViewById(R.id.profile_gender);
                about = getView().findViewById(R.id.profile_about);
                ImageView image = getView().findViewById(R.id.profile_image_view);
                title.setText(user.getFullName());
                age.setText(Integer.toString(user.getAge()));
                gender.setText("Not available");
                about.setText(user.getAbout());
                if (!user.getPhoto().equals("")) {
                    Picasso
                            .with(getActivity())
                            .load(user.getPhoto())
                            .into(image);
                }


                //Log.w(TAG, user)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting User failed, log a message
                Log.w(TAG, "loadPUser:onCancelled", databaseError.toException());
                // ...
            }
        };
        //THE THING THAT DIDNT WORK
        usersRef.orderByChild("id").equalTo(current.getUid()).addValueEventListener(userListener);


        // ------------ EDIT MODE ----------------
        // edit mode components
        final Spinner editGender = (Spinner) view.findViewById(R.id.editGender);
        editGender.setVisibility(View.GONE);

        final EditText editAge = (EditText) view.findViewById(R.id.editAge);
        editAge.setVisibility(View.GONE);

        final EditText editInterests = (EditText) view.findViewById(R.id.editInterests);
        editInterests.setVisibility(View.GONE);

        final LinearLayout editBtnContainer = (LinearLayout) view.findViewById(R.id.editButtons);
        editBtnContainer.setVisibility(View.GONE);
        final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        final Button saveButton = (Button) view.findViewById(R.id.saveButton);

        final Button editModeButton = (Button) view.findViewById(R.id.editModeButton);

        //List<Object> editComponents = Arrays.asList((Object) editAge, editGender, editInterests, saveButton, cancelButton);
        //List<TextView> viewComponents = Arrays.asList(editAge, editGender, editInterests);
        /*for(int i = 0; i < editViewComponents.size(); i++){
            editViewComponents.get
        }
                */

        editModeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("mode", "edit");
                Log.d("age", age.toString());
                Log.d("gender", gender.toString());
                Log.d("interests", about.toString());

                //age
                if (age.getText() != null) {
                    editAge.setText(age.getText());
                } else {
                    editAge.setText(setUndefinedText());
                }
                age.setVisibility(View.GONE);
                editAge.setVisibility(View.VISIBLE);

                /*gender
                if (gender.getText() != null) {
                    if(gender.getText().equals(editGender.getItemAtPosition(0).toString())){
                        editGender.setSelection(0);
                    } else if (gender.getText().equals(editGender.getItemAtPosition(1).toString())) {
                        editGender.setSelection(1);
                    } else {
                        editGender.setSelection(2);
                    }

                } else {
                    editGender.setSelection(1);
                }*/
                gender.setVisibility(View.GONE);
                editGender.setVisibility(View.VISIBLE);

                //interests/about
                if (about.getText() != null) {
                    editInterests.setText(about.getText());
                } else {
                    editInterests.setText(setUndefinedText());
                }
                about.setVisibility(View.GONE);
                editInterests.setVisibility(View.VISIBLE);

                editModeButton.setVisibility(View.GONE);
                editBtnContainer.setVisibility(View.VISIBLE);
            }
        });

        // save changes to view and db
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //age
                if (editAge.getText()!= null) {
                    age.setText(editAge.getText());
                } else {
                    age.setText(setUndefinedText());
                }
                age.setVisibility(View.VISIBLE);
                editAge.setVisibility(View.GONE);

                //gender
                gender.setText(editGender.getSelectedItem().toString());
                editGender.setVisibility(View.GONE);
                gender.setVisibility(View.VISIBLE);

                //interests/about
                if (editInterests.getText() != null) {
                    about.setText(editInterests.getText());
                } else {
                    about.setText(setUndefinedText());
                }
                editInterests.setVisibility(View.GONE);
                about.setVisibility(View.VISIBLE);

                editModeButton.setVisibility(View.VISIBLE);
                editBtnContainer.setVisibility(View.GONE);
            }
        });

        // save changes to view and db
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                age.setVisibility(View.VISIBLE);
                age.setVisibility(View.GONE);

                editGender.setVisibility(View.GONE);
                gender.setVisibility(View.VISIBLE);

                editInterests.setVisibility(View.GONE);
                about.setVisibility(View.VISIBLE);

                editModeButton.setVisibility(View.VISIBLE);
                editBtnContainer.setVisibility(View.GONE);
            }
        });

        return view;

    }



    private String setUndefinedText() {
        return "undefined";
    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}