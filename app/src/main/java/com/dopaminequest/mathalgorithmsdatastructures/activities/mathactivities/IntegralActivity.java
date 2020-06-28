package com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.Integral.IntegralView;

public class IntegralActivity extends AppCompatActivity {

    private IntegralView integralView;
    //Switch sw1;
    Switch sw2;
    SeekBar seekBar;
    Spinner spinner;
    TextView viewNumRects;
    TextView viewArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        integralView = (IntegralView) findViewById(R.id.IntegralView);

        viewNumRects = (TextView) findViewById(R.id.num_rects_text);
        viewArea = (TextView) findViewById(R.id.area_text);
        //viewArea = (TextView) findViewById(R.id.area_text);


        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sw2.setChecked(false);
                spinner.setSelection(0);
                integralView.resetX();

            }
        });



//        sw1 = (Switch) findViewById(R.id.pause);
//        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled
//                    //view.pause();
//                } else {
//                    // The toggle is disabled
//                    //view.pause();
//                }
//            }
//        });

        sw2 = (Switch) findViewById(R.id.disable_animation);
        sw2.setChecked(false);
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    integralView.toggleAnimation();
                } else {
                    // The toggle is disabled
                    integralView.toggleAnimation();
                }
            }
        });

        seekBar = findViewById(R.id.seekBarSortSpeed);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);


        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.integral_function_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("test");
                integralView.setFunctionNum(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });




        //SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

    }



    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
//                tvProgressLabel.setText("Progress: " + progress);
            integralView.setSeekPosition(progress);
            viewNumRects.setText(String.valueOf(integralView.getNumRects())+ " Subdivisions");

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
            sw2.setChecked(false);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        //view.pauseThread();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //view.resumeThread();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //view.terminateThread();
        finish();
    }
}
