package com.justagroup.justabeer.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.lang.reflect.Field;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.justagroup.justabeer.Hangout;

import com.justagroup.justabeer.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateHangoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateHangoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateHangoutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateHangoutFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateHangoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateHangoutFragment newInstance(String param1, String param2) {
        CreateHangoutFragment fragment = new CreateHangoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_hangout, container, false);

        /*------- EVENT TYPE ----------
        ImageView beer = (ImageView) view.findViewById(R.id.beerIcon);
        ImageView coffee = (ImageView) view.findViewById(R.id.coffeeIcon);
        ImageView food = (ImageView) view.findViewById(R.id.foodIcon);

        RadioGroup eventTypeRadioGroup = (RadioGroup) view.findViewById(R.id.eventTypeRadio);
        RadioButton radio1 = view.findViewById(R.id.beerRadio);
        eventTypeRadioGroup.addView(radio1);
        RadioButton radio2 = view.findViewById(R.id.coffeeRadio);
        eventTypeRadioGroup.addView(radio2);
        RadioButton radio3 = view.findViewById(R.id.foodRadio);
        eventTypeRadioGroup.addView(radio3);

        /*
        //init event type radio group
        RadioGroup eventTypeRadioGroup = (RadioGroup) view.findViewById(R.id.eventTypeRadio);
        final String[] eventNames = new String[] {"Beer","Food","Sports"};
        RadioButton radio1 = view.findViewById(R.id.beerRadio);
        eventTypeRadioGroup.addView(radio1);
        RadioButton radio2 = view.findViewById(R.id.foodRadio);
        eventTypeRadioGroup.addView(radio2);
        RadioButton radio3 = view.findViewById(R.id.sportsRadio);
        eventTypeRadioGroup.addView(radio3);

        eventTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ind) {
                Log.d("event type index", ind + "");
                RadioButton button = null;

                for (int i = 0; i < eventNames.length; ++i) {
                    if (ind == i) {
                        //button selected
                        button = (RadioButton) view.findViewById(ind);
                        //button = (RadioButton) radioGroup.getChildAt(ind);
                        button.setChecked(true);
                    } else {
                        //buttons that are not selected
                        button = (RadioButton) view.findViewById(ind);
                        button.setChecked(false);
                    }
                }
            }
        });*/
        //---------- TIME ------------


        //-------- LOCATION ----------


        //-------- DESCRIPTION ----------
        /*EditText description = (EditText) view.findViewById(R.id.descriptionText);
        description.setFilters(new InputFilter[] {new InputFilter.LengthFilter(200)});
        */

        //-------- BUTTONS ----------

        final Button button = getView().findViewById(R.id.cancelHangout);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HangoutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        final Button button2 = getView().findViewById(R.id.createHangout);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HangoutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        return view;

    }

    public void pushDatatoDb(DatabaseReference ref, Hangout h){

        Hangout.EventType eventType = Hangout.EventType.Beer;
        RadioGroup eventTypeRadioGroup = (RadioGroup) getView().findViewById(R.id.eventTypeRadio);
        switch(eventTypeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.beerRadio:
                eventType = Hangout.EventType.Beer;
                break;
            case R.id.coffeeRadio:
                eventType = Hangout.EventType.Coffee;
                break;
            case R.id.foodRadio:
                eventType = Hangout.EventType.Food;
                break;
        }

        RadioGroup dateRadioGroup = (RadioGroup) getView().findViewById(R.id.dateRadio);
        Calendar calFromTime = Calendar.getInstance(); // creates calendar
        Calendar calToTime = Calendar.getInstance();
        calFromTime.setTime(new Date());
        calToTime.setTime(new Date());
        switch(dateRadioGroup.getCheckedRadioButtonId()) {
            case R.id.todayRadio:

                break;
            case R.id.tomorrowRadio:
                calFromTime.add(Calendar.DAY_OF_MONTH,1);
                calToTime.add(Calendar.DAY_OF_MONTH,1);
                break;

        }

        EditText fromTimeText = getView().findViewById(R.id.fromTimeText);
        String strFromTime = (String) fromTimeText.getText().toString();

        EditText toTimeText = getView().findViewById(R.id.toTimeText);
        String strToTime = (String) toTimeText.getText().toString();

        calFromTime.set(Calendar.HOUR,Integer.parseInt(strFromTime.substring(0,1));
        calFromTime.set(Calendar.MINUTE,Integer.parseInt(strFromTime.substring(3,5)));
        Date fromTime = calFromTime.getTime();
        calFromTime.set(Calendar.HOUR,Integer.parseInt(strToTime.substring(0,1))));
        calFromTime.set(Calendar.MINUTE,Integer.parseInt(strToTime.substring(3,5)));
        Date toTime = calToTime.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strFromDate = dateFormat.format(fromTime).toString();
        String strToDate = dateFormat.format(toTime).toString();
        DatabaseReference newRef = ref.push();
        ArrayList<String> pendingUsers = new ArrayList<String>();
        //pendingUsers.add("");
        ArrayList<String> confirmedUsers = new ArrayList<String>();
        //confirmedUsers.add("");
        ArrayList<String> rejectedUsers = new ArrayList<String>();
        //rejectedUsers.add("");
        ArrayList<String> commentIds = new ArrayList<String>();
        //commentIds.add("");
        ArrayList<String> privateMessageIds = new ArrayList<String>();
        //privateMessageIds.add("");

        String strDescriptionText = "";
        String strTitle = eventType + " @ " +calFromTime.get(Calendar.HOUR) + " - "+calToTime.get(Calendar.HOUR);
        EditText descriptionText = getView().findViewById(R.id.descriptionText);
        String strDescriptionText = (String) descriptionText.getText().toString();

        LatLng location = new LatLng(0,0);

        Hangout hg1 = new Hangout(newRef.getKey(),
                strTitle,
                fromTime,
                toTime,
                strDescriptionText,
                location,
                eventType,
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                pendingUsers,
                confirmedUsers,
                rejectedUsers,
                commentIds,
                privateMessageIds);
        newRef.setValue(hg1);
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
