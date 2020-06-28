package com.dopaminequest.mathalgorithmsdatastructures.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.templateview.TemplateView;

public class TemplateActivity extends AppCompatActivity {

    private TemplateView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        view = (TemplateView) findViewById(R.id.TemplateView);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.resetX();
            }
        });


        Switch sw1 = (Switch) findViewById(R.id.pause);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //view.pause();
                } else {
                    // The toggle is disabled
                    //view.pause();
                }
            }
        });

        Switch sw2 = (Switch) findViewById(R.id.disable_animation);
        sw2.setChecked(true);
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //view.toggleAnimation();
                } else {
                    // The toggle is disabled
                    //view.toggleAnimation();
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
