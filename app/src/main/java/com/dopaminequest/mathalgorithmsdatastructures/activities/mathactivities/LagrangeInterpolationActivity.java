package com.dopaminequest.mathalgorithmsdatastructures.activities.mathactivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.LagrangeInterpolation.LagrangeInterpolationView;

public class LagrangeInterpolationActivity extends AppCompatActivity {

    private LagrangeInterpolationView lagrangeInterpolationActivity;

    TextView viewArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lagrange_interpolation);
        lagrangeInterpolationActivity = (LagrangeInterpolationView) findViewById(R.id.LagrangeInterpolationView);

        viewArea = (TextView) findViewById(R.id.lagrange_interpolation_text);
        //viewArea = (TextView) findViewById(R.id.area_text);


        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lagrangeInterpolationActivity.resetX();
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lagrangeInterpolationActivity.remove();
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.derivative_function_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

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
