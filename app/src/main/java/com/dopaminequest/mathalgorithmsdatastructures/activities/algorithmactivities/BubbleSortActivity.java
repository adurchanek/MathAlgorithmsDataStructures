package com.dopaminequest.mathalgorithmsdatastructures.activities.algorithmactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.BubbleSort.BubbleSortView;

public class BubbleSortActivity extends AppCompatActivity {

    private BubbleSortView bubbleSortView;
    private long lastFrameTime;

    SeekBar seekBarSortSpeed;
    SeekBar seekBarNumNodes;
    TextView viewNumTerms;
    TextView viewsortSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_sort);
        bubbleSortView = (BubbleSortView) findViewById(R.id.BubbleSortView);
        viewNumTerms = (TextView) findViewById(R.id.numNodesTextView);
        viewsortSpeed = (TextView) findViewById(R.id.sortSpeedTextView);

        lastFrameTime = System.currentTimeMillis();

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bubbleSortView.resetX();
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
                bubbleSortView.autoSort();
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
                bubbleSortView.nextSort();
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
            bubbleSortView.setSortSpeed(progress);

            int sortSpeed = bubbleSortView.getSortSpeed();
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


            viewsortSpeed.setText(String.valueOf( "Speed: " +String.format(speedString)));

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
            bubbleSortView.setNumNodes(progress);
            viewNumTerms.setText(String.valueOf( "Size: " +String.format(String.valueOf((int)bubbleSortView.getNumNodes()))   ));

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
        bubbleSortView.pauseThread();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        bubbleSortView.resumeThread();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubbleSortView.terminateThread();
        finish();
    }
}
