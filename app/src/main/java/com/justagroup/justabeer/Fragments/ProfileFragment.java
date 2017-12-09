package com.justagroup.justabeer.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.justagroup.justabeer.User;
//firebase stuff
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.justagroup.justabeer.R;
import com.squareup.picasso.Picasso;


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
    public TextView email;
    public TextView profileImageURL;
    public User currentUser;


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
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // My version
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Attach a listener to read the data at our users reference
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //define view fields
                currentUser = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                fullName = getView().findViewById(R.id.profile_card_title);
                age = getView().findViewById(R.id.profile_age);
                gender = getView().findViewById(R.id.profile_gender);
                about = getView().findViewById(R.id.profile_about);
                userImage = getView().findViewById(R.id.profile_image_view);
                email = getView().findViewById(R.id.editEmail);
                profileImageURL = getView().findViewById((R.id.editPic));

                //init fields
                fullName.setText(checkTextValue(currentUser.getFullName()));
                age.setText(checkTextValue(Integer.toString(currentUser.getAge())));
                gender.setText(checkTextValue(currentUser.getGender()));
                about.setText(checkTextValue(currentUser.getAbout()));
                email.setText(checkTextValue(currentUser.getEmail()));
                profileImageURL.setText(checkTextValue(currentUser.getPhoto()));
                if (currentUser.getPhoto() != null && !currentUser.getPhoto().isEmpty()) {
                    Picasso
                            .with(getActivity())
                            .load(currentUser.getPhoto())
                            .into(userImage);
                } else {
                    Picasso
                            .with(getActivity())
                            .load(R.drawable.logo)
                            .into(userImage);
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
        usersRef.orderByChild("id").equalTo(currentFirebaseUser.getUid()).addValueEventListener(userListener);


        // ------------ EDIT MODE ----------------
        // edit mode components
        final EditText editName = (EditText) view.findViewById(R.id.editName);
        final TextView editNameCaption = (TextView) view.findViewById(R.id.editNameCaption);
        editName.setVisibility(View.GONE);
        editNameCaption.setVisibility(View.GONE);

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

        final LinearLayout emailContainer = (LinearLayout) view.findViewById(R.id.emailContainer);
        emailContainer.setVisibility(View.GONE);
        final EditText editEmail = (EditText) view.findViewById(R.id.editEmail);
        final TextView editEmailCaption = (TextView) view.findViewById(R.id.editEmailCaption);

        final LinearLayout picContainer = (LinearLayout) view.findViewById(R.id.picContainer);
        picContainer.setVisibility(picContainer.GONE);
        final EditText editPic = (EditText) view.findViewById(R.id.editPic);
        final TextView editPicCaption = (TextView) view.findViewById(R.id.editPicCaption);

        final Button editModeButton = (Button) view.findViewById(R.id.editModeButton);

        //List<Object> editComponents = Arrays.asList((Object) editAge, editGender, editInterests, saveButton, cancelButton);
        //List<TextView> viewComponents = Arrays.asList(editAge, editGender, editInterests);
        /*for(int i = 0; i < editViewComponents.size(); i++){
            editViewComponents.get
        }
                */

        editModeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //name
                editName.setText(fullName.getText());
                editName.setVisibility(View.VISIBLE);
                editNameCaption.setVisibility(View.VISIBLE);

                //age
                if (age.getText() != null) {
                    editAge.setText(setEditText(age.getText().toString()));
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
                    editInterests.setText(setEditText(about.getText().toString()));
                } else {
                    editInterests.setText(setUndefinedText());
                }
                about.setVisibility(View.GONE);
                editInterests.setVisibility(View.VISIBLE);

                if (email.getText() != null) {
                    editEmail.setText(setEditText(email.getText().toString()));
                } else {
                    editEmail.setText(setUndefinedText());
                }
                emailContainer.setVisibility(View.VISIBLE);

                if (profileImageURL.getText() != null) {
                    editPic.setText(setEditText(profileImageURL.getText().toString()));
                } else {
                    editPic.setText(setUndefinedText());
                }
                picContainer.setVisibility(View.VISIBLE);

                userImage.setVisibility(View.GONE);

                editModeButton.setVisibility(View.GONE);
                editBtnContainer.setVisibility(View.VISIBLE);
            }
        });

        // save changes to view and db
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(editName.getText() != null){
                    fullName.setText(editName.getText());
                } else {
                    fullName.setText(currentUser.getId());
                }
                editName.setVisibility(View.GONE);
                editNameCaption.setVisibility(View.GONE);

                //age
                if (editAge.getText()!= null) {
                    age.setText(editAge.getText());
                } else {
                    age.setText("0");
                }
                age.setVisibility(View.VISIBLE);
                editAge.setVisibility(View.GONE);

                //gender
                String genderString = editGender.getSelectedItem().toString();
                gender.setText(genderString);

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

                //email
                //interests/about
                if (editEmail.getText() != null) {
                    email.setText(editEmail.getText());
                } else {
                    email.setText(setUndefinedText());
                }
                emailContainer.setVisibility(View.GONE);


                //photo URL
                if (editPic.getText() != null) {
                    profileImageURL.setText(editPic.getText());
                } else {
                    editPic.setText(setUndefinedText());
                }
                picContainer.setVisibility(View.GONE);

                editModeButton.setVisibility(View.VISIBLE);
                editBtnContainer.setVisibility(View.GONE);

                userImage.setVisibility(View.VISIBLE);

                //update database IN PROGRESS
                //try 1: updates by creating new object to db
                //usersRef.child(currentFirebaseUser.getUid()).setValue(new User(currentUser.getId(), userDbId, "http://www.watoday.com.au/content/dam/images/1/m/j/7/z/9/image.related.socialLead.620x349.gtq15g.png/1484266450200.jpg", currentUser.getEmail(), Integer.parseInt(age.getText().toString()), gender.getText().toString(), about.getText().toString(), currentUser.getTimestampJoined()));

                //try 2: updates by creating new object to db
                usersRef.child(currentUser.getId()).setValue(new User(currentUser.getId(), fullName.getText().toString(), profileImageURL.getText().toString(), currentUser.getEmail(), Integer.parseInt(age.getText().toString()), gender.getText().toString(), about.getText().toString(), currentUser.getTimestampJoined()));

                //try 1: updates by creating new object to db
                //usersRef.child(currentFirebaseUser.getUid()).setValue(new User(currentUser.getId(), userDbId, "http://www.watoday.com.au/content/dam/images/1/m/j/7/z/9/image.related.socialLead.620x349.gtq15g.png/1484266450200.jpg", currentUser.getEmail(), Integer.parseInt(age.getText().toString()), gender.getText().toString(), about.getText().toString(), currentUser.getTimestampJoined()));

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editName.setVisibility(View.GONE);
                editNameCaption.setVisibility(View.GONE);

                age.setVisibility(View.VISIBLE);
                editAge.setVisibility(View.GONE);

                editGender.setVisibility(View.GONE);
                gender.setVisibility(View.VISIBLE);

                editInterests.setVisibility(View.GONE);
                about.setVisibility(View.VISIBLE);

                emailContainer.setVisibility(View.GONE);
                picContainer.setVisibility(View.GONE);

                editModeButton.setVisibility(View.VISIBLE);
                editBtnContainer.setVisibility(View.GONE);
            }
        });

        return view;

    }
    /*
    private void showComponents(Map<Object, >)
                for (Map.Entry<TextView, String> component : profileComponents.entrySet()) {
        if(component.getKey() != null){
            if(component.getValue() != null && !component.getValue().isEmpty()){
                component.getKey().setText(component.getValue());
            } else {
                component.getKey().setText("Not available");
            }
        } else {
            Log.d(TAG, "text component not defined");
        }
    }/*


    /*private void updateUserDbData(String name, String photoURL, String email, Integer age, String gender, String about) {
        FirebaseDatabase.getInstance('users/' + current).set({
                fullName: name,
                photo: photoURL,
                email: email,
                age : age,
                gender: gender,
                about: about
        });
    }*/



    private String checkTextValue(String value){
        if(value != null && !value.isEmpty()){
            return value;
        } else {
            return "Not available";
        }
    }

    private String setEditText(String value){
        if(value != null && !value.isEmpty() && !value.equals("Not available")){
            return value;
        } else {
            return "";
        }
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