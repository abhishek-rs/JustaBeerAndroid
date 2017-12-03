package com.justagroup.justabeer.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
