package com.dopaminequest.mathalgorithmsdatastructures.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.Stack.StackView;

public class StackActivity extends AppCompatActivity {

    private StackView stackView;
    private long lastFrameTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        stackView = (StackView) findViewById(R.id.StackView);
        lastFrameTime = System.currentTimeMillis();


        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackView.resetX();
            }
        });


        findViewById(R.id.push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(System.currentTimeMillis() - lastFrameTime < 75)
                {
                    return;
                }
                lastFrameTime = System.currentTimeMillis();
                stackView.push();
            }
        });

        findViewById(R.id.pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(System.currentTimeMillis() - lastFrameTime < 75)
                {
                    return;
                }
                lastFrameTime = System.currentTimeMillis();
                stackView.pop();
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
