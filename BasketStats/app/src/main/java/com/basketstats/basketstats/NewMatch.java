package com.basketstats.basketstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class NewMatch extends AppCompatActivity {

    private Button NewTeamButton;
    private Button SavedTeamButton;
    private Button OKButton;
    private TextView TeamChosenText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);


        getViews();
        processControllers();

        //OKButton = (Button)findViewById(R.id.ok);
        //OKButton.setOnClickListener(OKListener);



    }

    private void getViews() {

        NewTeamButton = (Button)findViewById(R.id.newteam);
        SavedTeamButton = (Button)findViewById(R.id.saved);


        OKButton = (Button)findViewById(R.id.ok);
        TeamChosenText = (TextView)findViewById(R.id.team_chosen);

    }

    private void processControllers() {

        //add OK button listener
        View.OnClickListener OKListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // opponent
            EditText oppEditText = (EditText)findViewById(R.id.opponent_value);
            String opp = oppEditText.getText().toString();

            //team
            //String team = "台大資工";
            String team = TeamChosenText.getText().toString();

            EditText yearEditText = (EditText)findViewById(R.id.year);
            int year = Integer.parseInt(yearEditText.getText().toString());

            EditText monthEditText = (EditText)findViewById(R.id.month);
            int month = Integer.parseInt(monthEditText.getText().toString());

            EditText dayEditText = (EditText)findViewById(R.id.day);
            int day = Integer.parseInt(dayEditText.getText().toString());

            String date = "2016/04/25";

            Intent i = new Intent(NewMatch.this, statRecord.class);
            Bundle extras = new Bundle();
            extras.putString("team", team);
            extras.putString("date", date);
            extras.putString("opp", opp);
            extras.putString("0", "a");
            extras.putString("2", "b");
            extras.putString("3", "c");
            extras.putString("4", "d");
            extras.putString("1", "e");
            extras.putString("numOfPlayers", "5");
            i.putExtras(extras);
            startActivity(i);
            }
        };
        OKButton.setOnClickListener(OKListener);

        //add NewTeam Button listener(startActivityForResult)
        View.OnClickListener NewTeamListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(NewMatch.this, NewTeam.class);
                startActivityForResult(i, 0);
            }
        };
        NewTeamButton.setOnClickListener(NewTeamListener);

        //add SavedTeam Button listener(startActivityForResult)
        View.OnClickListener SavedTeamListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(NewMatch.this, SavedTeam.class);
                startActivityForResult(i, 1);
            }
        };
        SavedTeamButton.setOnClickListener(SavedTeamListener);



    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK){

            // 0 : Newteam result
            // 1 : Saveteam result
            if(requestCode==0){
                Bundle extras = data.getExtras();
                String team = extras.getString("team");
                TeamChosenText.setText(team);
            }
            else if(requestCode==1){
                TeamChosenText.setText("Saved");
            }

        }
    }






}
