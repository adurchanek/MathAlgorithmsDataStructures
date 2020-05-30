package com.dopaminequest.mathalgorithmsdatastructures.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.Queue.QueueView;

public class QueueActivity extends AppCompatActivity {

    private QueueView queueView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        queueView = (QueueView) findViewById(R.id.QueueView);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queueView.resetX();
            }
        });


        findViewById(R.id.enqueue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queueView.enqueue();
            }
        });

        findViewById(R.id.dequeue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queueView.dequeue();
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
        queueView.pauseThread();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        queueView.resumeThread();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        queueView.terminateThread();
        finish();
    }
}
