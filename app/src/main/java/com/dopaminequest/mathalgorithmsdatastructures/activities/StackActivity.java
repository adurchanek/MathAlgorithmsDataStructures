package com.dopaminequest.mathalgorithmsdatastructures.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.AStar.AStarView;
import com.dopaminequest.mathalgorithmsdatastructures.views.Stack.StackView;
import com.dopaminequest.mathalgorithmsdatastructures.views.Dijkstras.DijkstraView;

public class StackActivity extends AppCompatActivity {

    private StackView stackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        stackView = (StackView) findViewById(R.id.StackView);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackView.resetX();
            }
        });


        Switch sw1 = (Switch) findViewById(R.id.pause);
        //sw1.setChecked(true);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    stackView.pause();
                } else {
                    // The toggle is disabled
                    stackView.pause();
                }
            }
        });

        Switch sw2 = (Switch) findViewById(R.id.disable_animation);
        sw2.setChecked(true);
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    stackView.toggleAnimation();
                } else {
                    // The toggle is disabled
                    stackView.toggleAnimation();
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
        stackView.pauseThread();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        stackView.resumeThread();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stackView.terminateThread();
        finish();
    }
}
