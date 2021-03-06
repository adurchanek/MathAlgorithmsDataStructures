package com.dopaminequest.mathalgorithmsdatastructures.activities.algorithmactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.SelectionSort.SelectionSortView;

public class SelectionSortActivity extends AppCompatActivity {

    private SelectionSortView selectionSortView;
    private long lastFrameTime;

    SeekBar seekBarSortSpeed;
    SeekBar seekBarNumNodes;
    TextView viewNumTerms;
    TextView viewSortSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_sort);
        selectionSortView = (SelectionSortView) findViewById(R.id.SelectionSortView);
        viewNumTerms = (TextView) findViewById(R.id.numNodesTextView);
        viewSortSpeed = (TextView) findViewById(R.id.sortSpeedTextView);

        lastFrameTime = System.currentTimeMillis();

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionSortView.resetX();
            }
        });


        findViewById(R.id.auto_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(System.currentTimeMillis() - lastFrameTime < 75)
//                {
//                    return;
//                }
//                lastFrameTime = System.currentTimeMillis();
                selectionSortView.autoSort();
            }
        });

        findViewById(R.id.next_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(System.currentTimeMillis() - lastFrameTime < 75)
//                {
//                    return;
//                }
//                lastFrameTime = System.currentTimeMillis();
                selectionSortView.nextSort();
            }
        });

        seekBarSortSpeed = findViewById(R.id.seekBarSortSpeed);
        seekBarSortSpeed.setOnSeekBarChangeListener(seekBarSortSpeedChangeListener);

        seekBarNumNodes = findViewById(R.id.seekBarNumNodes);
        seekBarNumNodes.setOnSeekBarChangeListener(seekBarNumNodesChangeListener);

    }

    SeekBar.OnSeekBarChangeListener seekBarSortSpeedChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
//                tvProgressLabel.setText("Progress: " + progress);
            //integralView.setSeekPosition(progress);
            //viewNumRects.setText(String.valueOf(integralView.getNumRects())+ " Subdivisions");
            selectionSortView.setSortSpeed(progress);

            int sortSpeed = selectionSortView.getSortSpeed();
            String speedString = "";
            if(sortSpeed == 100)
            {
                speedString = "MAX";
            }
            else if(sortSpeed == 0)
            {
                speedString = "MIN";
            }
            else
            {
                speedString = String.valueOf(sortSpeed) + "%%";
            }


            //viewNumTerms.setText(String.valueOf( "Size: " +String.format(speedString)   ));


            viewSortSpeed.setText(String.valueOf( "Speed: " +String.format(speedString)));

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
            //sw2.setChecked(false);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarNumNodesChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
//                tvProgressLabel.setText("Progress: " + progress);
            //integralView.setSeekPosition(progress);
            //viewNumRects.setText(String.valueOf(integralView.getNumRects())+ " Subdivisions");
            selectionSortView.setNumNodes(progress);
            viewNumTerms.setText(String.valueOf( "Size: " +String.format(String.valueOf((int)selectionSortView.getNumNodes()))   ));

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
            //sw2.setChecked(false);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        selectionSortView.pauseThread();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        selectionSortView.resumeThread();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectionSortView.terminateThread();
        finish();
    }
}
