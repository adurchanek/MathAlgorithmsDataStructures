package com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.BezierCurves.BezierCurvesView;

public class BezierCurvesActivity extends AppCompatActivity {

    private BezierCurvesView bezierCurvesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_curves);
        bezierCurvesView = (BezierCurvesView) findViewById(R.id.BezierCurvesView);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bezierCurvesView.resetX();
            }
        });


        Switch sw1 = (Switch) findViewById(R.id.pause);
        //sw1.setChecked(true);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    bezierCurvesView.pause();
                } else {
                    // The toggle is disabled
                    bezierCurvesView.pause();
                }
            }
        });

        Switch sw2 = (Switch) findViewById(R.id.disable_animation);
        sw2.setChecked(true);
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    bezierCurvesView.toggleAnimation();
                } else {
                    // The toggle is disabled
                    bezierCurvesView.toggleAnimation();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        bezierCurvesView.pauseThread();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        bezierCurvesView.resumeThread();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bezierCurvesView.terminateThread();
        finish();
    }
}
