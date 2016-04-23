package com.basketstats.basketstats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button newMatchButton, loadMatchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: show previous games

        /*  Create a button for new page activity */
        newMatchButton = (Button)findViewById(R.id.newMatch);
        newMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // run register activity on click
                Intent runRegisterActivity = new Intent(MainActivity.this, NewMatch.class);
                MainActivity.this.startActivity(runRegisterActivity);
            }
        });

        loadMatchButton = (Button)findViewById(R.id.loadMatch);
        loadMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // run register activity on click
                Intent runRegisterActivity = new Intent(MainActivity.this, LoadActivity.class);
                MainActivity.this.startActivity(runRegisterActivity);
            }
        });
    }
}
