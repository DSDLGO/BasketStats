package com.basketstats.basketstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class statRecord extends AppCompatActivity {

    private ArrayList<String> playerList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* get players name from register activity */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int numOfPlayers = Integer.parseInt(extras.getString("numOfPlayers"));
        for(int i = 0; i < numOfPlayers; i++)
        {
            String playerName = extras.getString(String.valueOf(i));
            playerList.add(playerName);
        }

        /* one can access to registered name in "playerList" */


        /********************below code is for test ************************/
        /* set up layout for rendering players */
        LinearLayout playerLayout = new LinearLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(FILL_PARENT, WRAP_CONTENT);
        playerLayout.setLayoutParams(params);
        playerLayout.setOrientation(VERTICAL);

        /* render players name */
        for(int i = 0; i < playerList.size(); i++)
            playerLayout.addView(typeLabel(playerList.get(i), i));
        setContentView(playerLayout);

    }

    private TextView typeLabel (String label, int id) {
        TextView view = new TextView(this);
        view.setId(id);
        view.setTextSize(20);
        view.setText(label);
        return view;
    }
}
