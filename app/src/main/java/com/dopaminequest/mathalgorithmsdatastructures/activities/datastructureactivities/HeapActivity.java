package com.dopaminequest.mathalgorithmsdatastructures.activities.datastructureactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.views.datastructureviews.Heap.HeapView;

public class HeapActivity extends AppCompatActivity {

    private HeapView heapView;
    private long lastFrameTime;

    SeekBar seekBarSortSpeed;
    SeekBar seekBarNumNodes;
    TextView viewNumTerms;
    TextView viewSortSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heap);
        heapView = (HeapView) findViewById(R.id.HeapView);
        viewNumTerms = (TextView) findViewById(R.id.numNodesTextView);
        viewSortSpeed = (TextView) findViewById(R.id.sortSpeedTextView);

        lastFrameTime = System.currentTimeMillis();

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heapView.resetX();
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
                heapView.autoSort();
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
                heapView.nextSort();
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
            heapView.setSortSpeed(progress);

            int sortSpeed = heapView.getSortSpeed();
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
            heapView.setNumNodes(progress);
            viewNumTerms.setText(String.valueOf( "Size: " +String.format(String.valueOf((int) heapView.getNumNodes()))   ));

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
        heapView.pauseThread();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        heapView.resumeThread();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        heapView.terminateThread();
        finish();
    }
}
