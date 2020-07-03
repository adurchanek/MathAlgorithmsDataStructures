package com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.VectorProjection.VectorProjectionView;

public class VectorProjectionActivity extends AppCompatActivity {

    private VectorProjectionView vectorProjectionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_projection);
        vectorProjectionView = (VectorProjectionView) findViewById(R.id.VectorProjectionView);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vectorProjectionView.resetX();
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
