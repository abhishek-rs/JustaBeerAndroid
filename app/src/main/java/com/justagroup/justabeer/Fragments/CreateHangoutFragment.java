package com.justagroup.justabeer.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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

    public String selectedType;
    public String selectedPlace;
    public String selectedDate;
    public String selectedStartTime;
    public String selectedEndTime;
    public String selectedDescription;


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

        //------- EVENT TYPE ----------
        final RadioGroup eventTypeRadioGroup = (RadioGroup) view.findViewById(R.id.eventTypeRadio);
        RadioButton beerRadio = (RadioButton) view.findViewById(R.id.beerRadio);
        RadioButton coffeeRadio = (RadioButton) view.findViewById(R.id.coffeeRadio);
        RadioButton foodRadio = (RadioButton) view.findViewById(R.id.foodRadio);

        //init event type radio group
        final String[] eventNames = new String[] {"Beer","Coffee","Food"};

        eventTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ind) {
                Log.d("event type index", ind + "");

                if(eventTypeRadioGroup.getCheckedRadioButtonId()!=-1){
                    int id= eventTypeRadioGroup.getCheckedRadioButtonId();
                    View radioButton = eventTypeRadioGroup.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) eventTypeRadioGroup.getChildAt(radioId);
                    selectedType = (String) btn.getText();
                }
            }
        });
        //---------- DATE ------------
        final RadioGroup eventDate = (RadioGroup) view.findViewById(R.id.dateRadio);

        //init event type radio group
        eventDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ind) {
                Log.d("event type index", ind + "");

                if(eventDate.getCheckedRadioButtonId()!=-1){
                    int id= eventDate.getCheckedRadioButtonId();
                    View radioButton = eventDate.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) eventDate.getChildAt(radioId);
                    selectedDate = (String) btn.getText();
                }
            }
        });
        //---------- TIME ------------


        //-------- LOCATION ----------
        EditText eventLocation = view.findViewById(R.id.eventPlace);
        eventLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null){
                    selectedPlace = s.toString();
                } else {
                    selectedPlace = "Mystery Location";
                }
            }
        });


        //-------- DESCRIPTION ----------
        EditText description = (EditText) view.findViewById(R.id.descriptionText);
        description.setFilters(new InputFilter[] {new InputFilter.LengthFilter(200)});

        description.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null){
                    selectedDescription = s.toString();
                } else {
                    selectedDescription = "Mystery Location";
                }
            }
        });


        return view;

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
