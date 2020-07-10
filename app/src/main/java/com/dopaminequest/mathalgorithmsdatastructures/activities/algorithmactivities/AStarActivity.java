package com.dopaminequest.mathalgorithmsdatastructures.activities.algorithmactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.AStar.AStarView;

public class AStarActivity extends AppCompatActivity {

    private AStarView aStarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_star);

        aStarView = (AStarView) findViewById(R.id.AStarView);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aStarView.resetX();
            }
        });

        findViewById(R.id.find_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aStarView.findShortestPath();
            }
        });

        findViewById(R.id.blocked).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aStarView.editBlocked();
            }
        });

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aStarView.editStart();
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aStarView.editEnd();
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
        aStarView.pauseThread();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        aStarView.resumeThread();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        aStarView.terminateThread();
        finish();
    }
}
