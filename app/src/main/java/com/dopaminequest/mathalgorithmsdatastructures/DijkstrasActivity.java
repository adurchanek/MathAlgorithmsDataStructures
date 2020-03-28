package com.dopaminequest.mathalgorithmsdatastructures;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dopaminequest.mathalgorithmsdatastructures.views.Dijkstras.DijkstraView;

public class DijkstrasActivity extends AppCompatActivity {

    private DijkstraView dijkstraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dijkstras);

        dijkstraView = (DijkstraView) findViewById(R.id.DijkstraView);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dijkstraView.resetX();
            }
        });


        findViewById(R.id.find_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dijkstraView.findShortestPath();
            }
        });

        findViewById(R.id.blocked).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dijkstraView.editBlocked();
            }
        });

//        findViewById(R.id.explored).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dijkstraView.editExplored();
//            }
//        });
//
//        findViewById(R.id.unexplored).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dijkstraView.editUnexplored();
//            }
//        });

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dijkstraView.editStart();
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dijkstraView.editEnd();
            }
        });


    }
}
