package com.dopaminequest.mathalgorithmsdatastructures.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities.BezierCurvesActivity;
import com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities.DerivativeActivity;
import com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities.IntegralActivity;
import com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities.LagrangeInterpolationActivity;
import com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities.TaylorSeriesActivity;
import com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities.VectorProjectionActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MathTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MathTabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;

    public MathTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MathTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MathTabFragment newInstance(String param1, String param2) {
        MathTabFragment fragment = new MathTabFragment();
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

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_math_tab, container, false);

        btn1 = (Button) v.findViewById(R.id.first_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), BezierCurvesActivity.class);
                startActivity(intent);
            }
        });

        btn2 = (Button) v.findViewById(R.id.second_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), VectorProjectionActivity.class);
                startActivity(intent);
            }
        });

        btn3 = (Button) v.findViewById(R.id.third_button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), IntegralActivity.class);
                startActivity(intent);
            }
        });

        btn4 = (Button) v.findViewById(R.id.fourth_button);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), TaylorSeriesActivity.class);
                startActivity(intent);
            }
        });

        btn5 = (Button) v.findViewById(R.id.fifth_button);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), DerivativeActivity.class);
                startActivity(intent);
            }
        });

        btn6 = (Button) v.findViewById(R.id.sixth_button);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), LagrangeInterpolationActivity.class);
                startActivity(intent);
            }
        });


        return v;
    }
}
