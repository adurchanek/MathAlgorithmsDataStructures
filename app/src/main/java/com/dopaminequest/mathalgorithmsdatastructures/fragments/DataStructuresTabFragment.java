package com.dopaminequest.mathalgorithmsdatastructures.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.activities.datastructureactivities.QueueActivity;
import com.dopaminequest.mathalgorithmsdatastructures.activities.datastructureactivities.StackActivity;
import com.dopaminequest.mathalgorithmsdatastructures.activities.datastructureactivities.HeapActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataStructuresTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataStructuresTabFragment extends Fragment {
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

    public DataStructuresTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataStructuresTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataStructuresTabFragment newInstance(String param1, String param2) {
        DataStructuresTabFragment fragment = new DataStructuresTabFragment();
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
        View v = inflater.inflate(R.layout.fragment_data_structures_tab, container, false);

        btn1 = (Button) v.findViewById(R.id.first_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), StackActivity.class);
                startActivity(intent);
            }
        });

        btn2 = (Button) v.findViewById(R.id.second_button);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), QueueActivity.class);
                startActivity(intent);
            }
        });

        btn3 = (Button) v.findViewById(R.id.third_button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getContext(), HeapActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
