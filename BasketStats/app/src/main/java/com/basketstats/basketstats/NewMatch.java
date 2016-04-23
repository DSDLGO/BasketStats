package com.basketstats.basketstats;

import android.app.AlertDialog;
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
    private Button BackButton;
    private TextView TeamChosenText;

    private Bundle gameinfo = new Bundle();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);


        getViews();
        processControllers();


    }

    private void getViews() {

        NewTeamButton = (Button)findViewById(R.id.newteam);
        SavedTeamButton = (Button)findViewById(R.id.saved);

        OKButton = (Button)findViewById(R.id.ok);
        BackButton = (Button)findViewById(R.id.back);
        TeamChosenText = (TextView)findViewById(R.id.team_chosen);

    }

    private void showAlertAndFinish(int titleId, int messageId){
        new AlertDialog.Builder(NewMatch.this)
                .setTitle(titleId)
                .setMessage(messageId)
                .show();
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
            String team = TeamChosenText.getText().toString();


            Intent i = new Intent(NewMatch.this, statRecord.class);

            gameinfo.putString("team", team);
            gameinfo.putString("opp", opp);

            if(opp.matches(""))
                showAlertAndFinish(R.string.warning,R.string.no_opponent);
            else if (team.matches(""))
                showAlertAndFinish(R.string.warning,R.string.no_team);
            else {
                i.putExtras(gameinfo);
                startActivity(i);
            }


            }
        };
        OKButton.setOnClickListener(OKListener);

        //add Back Button listener
        View.OnClickListener BackListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        };
        BackButton.setOnClickListener(BackListener);

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


                int numOfPlayers = Integer.parseInt(extras.getString("numOfPlayers"));
                for (int i = 0; i < numOfPlayers; i++) {
                    String playerName = extras.getString(String.valueOf(i));
                    gameinfo.putString(String.valueOf(i), playerName);
                }
                gameinfo.putString("numOfPlayers", String.valueOf(numOfPlayers));
            }
            else if(requestCode==1){
                TeamChosenText.setText("Saved");
            }

        }
    }






}
