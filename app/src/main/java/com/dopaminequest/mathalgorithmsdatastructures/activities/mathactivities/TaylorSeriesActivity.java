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
import com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.TaylorSeries.TaylorSeriesView;


public class TaylorSeriesActivity extends AppCompatActivity {

    private TaylorSeriesView taylorSeriesView;
    Switch sw1;
    Switch sw2;
    SeekBar seekBar1;
    Spinner spinner;
    TextView viewNumTerms;
    TextView viewOffsetA;
    SeekBar seekBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taylor_series);
        taylorSeriesView = (TaylorSeriesView) findViewById(R.id.TaylorSeriesView);

        viewNumTerms = (TextView) findViewById(R.id.h_val_text);
        viewOffsetA = (TextView) findViewById(R.id.point_a_view);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sw2.setChecked(false);
                spinner.setSelection(0);
                taylorSeriesView.resetX();
            }
        });

        sw1 = (Switch) findViewById(R.id.maclaurin_series);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    taylorSeriesView.toggleMaclaurinSeries();
                } else {
                    taylorSeriesView.toggleMaclaurinSeries();
                }
                viewOffsetA.setText(String.valueOf("Offset a: " + String.format("%.2f",taylorSeriesView.getPointAOffset())));
            }
        });

        sw2 = (Switch) findViewById(R.id.disable_animation);
        sw2.setChecked(false);
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    taylorSeriesView.toggleAnimation();
                } else {
                    // The toggle is disabled
                    taylorSeriesView.toggleAnimation();
                }
            }
        });

        seekBar1 = findViewById(R.id.term_seek_bar);
        seekBar1.setOnSeekBarChangeListener(seekBarChangeListener);

        seekBar2 = findViewById(R.id.point_a_seek_bar);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                taylorSeriesView.setPointAPosition(progress);
                viewOffsetA.setText(String.valueOf("Offset a: " + String.format("%.2f",taylorSeriesView.getPointAOffset())));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.taylor_series_function_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                taylorSeriesView.setFunctionNum(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            taylorSeriesView.setTermPosition(progress);
            viewNumTerms.setText(String.valueOf( "Term: " +String.format("%.2f",taylorSeriesView.getCurrentTermNum())   ));
            viewOffsetA.setText(String.valueOf("Offset a: " + String.format("%.2f",taylorSeriesView.getPointAOffset())));
        }

            @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
            sw2.setChecked(false);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
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
