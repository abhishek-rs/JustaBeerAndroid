package com.justagroup.justabeer.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
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
import java.util.TimeZone;
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
import java.util.Locale;
import java.util.Map;
import android.location.Geocoder;

import com.justagroup.justabeer.Hangout;
import com.justagroup.justabeer.HomeActivity;

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
    private Context ctx;

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

        final Button button = view.findViewById(R.id.cancelHangout);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        final Button button2 = view.findViewById(R.id.createHangout);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pushDatatoDb();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        String selectedStartTime = "";

        //---------- TIME ------------
        final EditText startTime = (EditText) view.findViewById(R.id.startTime);
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String timeString = "";
                        if(selectedHour < 10) timeString += "0";
                        timeString += selectedHour + ":";
                        if(selectedMinute < 10) timeString += "0";
                        timeString += selectedMinute;
                        startTime.setText( timeString);
                        //selectedStartTime = startTime.getText().toString();
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Starting Time");
                mTimePicker.show();

            }
        });

        String selectedEndTime = "";

        //---------- TIME ------------
        final EditText endTime = (EditText) view.findViewById(R.id.endTime);
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String timeString = "";
                        if(selectedHour < 10) timeString += "0";
                        timeString += selectedHour + ":";
                        if(selectedMinute < 10) timeString += "0";
                        timeString += selectedMinute;
                        endTime.setText(timeString);
                        //selectedEndTime = startTime.getText().toString();
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Starting Time");
                mTimePicker.show();

            }
        });
        return view;

    }

    public void pushDatatoDb(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference ref = db.getReference("hangouts");

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
        Calendar calFromTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Stockholm"), new Locale("sv","se")); // creates calendar
        Calendar calToTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Stockholm"), new Locale("sv","se"));
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

        EditText fromTimeText = getView().findViewById(R.id.startTime);
        String strFromTime = (String) fromTimeText.getText().toString();

        EditText toTimeText = getView().findViewById(R.id.endTime);
        String strToTime = (String) toTimeText.getText().toString();

        Log.i("abc", ""+strFromTime);
        calFromTime.set(Calendar.HOUR_OF_DAY,Integer.parseInt(strFromTime.substring(0,2)));
        calFromTime.set(Calendar.MINUTE,Integer.parseInt(strFromTime.substring(3,5)));
        Date fromTime = calFromTime.getTime();
        Log.i("","Time: "+fromTime.toString());
        calToTime.set(Calendar.HOUR_OF_DAY,Integer.parseInt(strToTime.substring(0,2)));
        calToTime.set(Calendar.MINUTE,Integer.parseInt(strToTime.substring(3,5)));
        Date toTime = calToTime.getTime();
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strFromDate = dateFormat.format(calFromTime).toString();
        String strToDate = dateFormat.format(calToTime).toString();*/
        String strFromDate = calFromTime.get(Calendar.YEAR) + "/" +
                String.format("%02d",calFromTime.get(Calendar.MONTH)) + "/" +
                String.format("%02d",calFromTime.get(Calendar.DATE)) + " " +
                String.format("%02d",calFromTime.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.format("%02d",calFromTime.get(Calendar.MINUTE)) + ":" +
                String.format("%02d",calFromTime.get(Calendar.SECOND));
        String strToDate = calToTime.get(Calendar.YEAR) + "/" +
                String.format("%02d",calToTime.get(Calendar.MONTH)) + "/" +
                String.format("%02d",calToTime.get(Calendar.DATE)) + " " +
                String.format("%02d",calToTime.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.format("%02d",calToTime.get(Calendar.MINUTE)) + ":" +
                String.format("%02d",calToTime.get(Calendar.SECOND));
        Log.i("pushDataToDb","fromtime: "+strFromDate);

        DatabaseReference newRef = ref.push();
        List<String> pendingUsers = new ArrayList<String>();
        pendingUsers.add("");
        List<String> confirmedUsers = new ArrayList<String>();
        confirmedUsers.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
        List<String> rejectedUsers = new ArrayList<String>();
        rejectedUsers.add("");
        List<String> commentIds = new ArrayList<String>();
        commentIds.add("");
        List<String> privateMessageIds = new ArrayList<String>();
        privateMessageIds.add("");


        String strTitle = eventType + " @ " +calFromTime.get(Calendar.HOUR_OF_DAY) + " - "+calToTime.get(Calendar.HOUR_OF_DAY);

        String strDescriptionText = "";
        EditText descriptionText = getView().findViewById(R.id.descriptionText);
        strDescriptionText = (String) descriptionText.getText().toString();

        EditText locationText = getView().findViewById(R.id.eventPlace);

        LatLng location = new LatLng(0,0);
        Geocoder geocoder = new Geocoder(ctx);
        List<Address> addresses;
        double latitude = 0;
        double longitude = 0;
        try {
            addresses = geocoder.getFromLocationName(locationText.getText().toString(), 1);
            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }
        } catch (Exception e) {}

        Hangout hg1 = new Hangout(newRef.getKey(),
                strTitle,
                strFromDate,
                strToDate,
                strDescriptionText,
                latitude,
                longitude,
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
        this.ctx = context;
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
