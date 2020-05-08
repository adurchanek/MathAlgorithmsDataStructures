package com.dopaminequest.mathalgorithmsdatastructures.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.AStar.AStarView;
import com.dopaminequest.mathalgorithmsdatastructures.views.Template.TemplateView;
import com.dopaminequest.mathalgorithmsdatastructures.views.Dijkstras.DijkstraView;

public class TemplateActivity extends AppCompatActivity {

    private TemplateView bezierCurvesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        bezierCurvesView = (TemplateView) findViewById(R.id.TemplateView);

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
