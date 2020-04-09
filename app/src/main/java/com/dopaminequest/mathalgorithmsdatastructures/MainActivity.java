package com.dopaminequest.mathalgorithmsdatastructures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import com.dopaminequest.mathalgorithmsdatastructures.views.MainView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.first_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(), DijkstrasActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        //finish();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }
}
