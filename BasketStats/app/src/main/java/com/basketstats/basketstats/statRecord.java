package com.basketstats.basketstats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.ArrayList;

public class statRecord extends AppCompatActivity {

    private ArrayList<String> playerList = new ArrayList<String>();
    private String home_team, away_team, date;
    private int home_score = 0, away_score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_record);

        /* get players name from register activity */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int numOfPlayers = Integer.parseInt(extras.getString("numOfPlayers"));
        home_team = extras.getString("team");
        away_team = extras.getString("opp");
        date = extras.getString("date");

        for (int i = 0; i < numOfPlayers; i++) {
            String playerName = extras.getString(String.valueOf(i));
            playerList.add(playerName);
        }

        processViews();
    }

    private void processViews(){

        Button homeTeamButton = (Button) findViewById(R.id.button_home_team);
        homeTeamButton.setText(home_team);

        Button awayTeamButton = (Button) findViewById(R.id.button_away_team);
        awayTeamButton.setText(away_team);

        Button playerButton1 = (Button) findViewById(R.id.button_player_1);
        Button playerButton2 = (Button) findViewById(R.id.button_player_2);
        Button playerButton3 = (Button) findViewById(R.id.button_player_3);
        Button playerButton4 = (Button) findViewById(R.id.button_player_4);
        Button playerButton5 = (Button) findViewById(R.id.button_player_5);

        if(playerList.size() >= 5) {
            playerButton1.setText(playerList.get(0));
            playerButton2.setText(playerList.get(1));
            playerButton3.setText(playerList.get(2));
            playerButton4.setText(playerList.get(3));
            playerButton5.setText(playerList.get(4));
        }
        else{
            new AlertDialog.Builder(statRecord.this)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.player_not_enough)
                    .setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }
}