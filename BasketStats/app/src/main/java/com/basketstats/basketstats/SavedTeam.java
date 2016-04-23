package com.basketstats.basketstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SavedTeam extends AppCompatActivity {

    private Button OKButton;
    private ListView TeamListView;
    private static final String[] data = {
            "team1",
            "team2",
            "team3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_team);

        getViews();
        processTeamList();
        processControllers();
    }


    private void processTeamList() {

        int layoutId = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, layoutId, data);
        TeamListView.setAdapter(adapter);


    }

    private void getViews() {

        OKButton = (Button)findViewById(R.id.ok);
        TeamListView = (ListView)findViewById((R.id.teamlist));

    }

    private void processControllers() {

        //add OK button listener
        View.OnClickListener OKListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(SavedTeam.this, NewMatch.class);
                setResult(RESULT_OK,i);
                finish();
            }
        };
        OKButton.setOnClickListener(OKListener);


    }


}
