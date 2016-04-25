package com.basketstats.basketstats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewTeam extends AppCompatActivity {


    private Button OKButton;
    private Button BackButton;
    private LinearLayout StartingLayout;
    private LinearLayout BenchLayout;

    private List<EditText> playerListEditText = new ArrayList<EditText>();      // store the name of input players
    private List<String> playerList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        getViews();
        createPlayList();
        processControllers();
    }

    private void getViews() {

        OKButton = (Button)findViewById(R.id.ok);
        BackButton = (Button)findViewById(R.id.back);
        StartingLayout = (LinearLayout)findViewById(R.id.starting_list);
        BenchLayout = (LinearLayout)findViewById(R.id.bench_list);

    }

    private void createPlayList() {

        for(int i=1;i<=5;i++)
            StartingLayout.addView(createEditText(i));
        for(int i=1;i<=7;i++)
            BenchLayout.addView(createEditText(i));

    }

    private EditText createEditText(int num) {
        EditText playerEditText = new EditText(this);
        playerEditText.setId(Integer.valueOf(num));
        playerEditText.setHint("Player " + num);
        playerListEditText.add(playerEditText);
        return playerEditText;
    }

    private void showAlertAndFinish(int titleId, int messageId){
        new AlertDialog.Builder(NewTeam.this)
                .setTitle(titleId)
                .setMessage(messageId)
                .show();
    }

    private void processControllers() {

        //add OK button listener
        View.OnClickListener OKListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewTeam.this, NewMatch.class);
                Bundle extras = new Bundle();

                // get new team name from EditText
                EditText teamEditText = (EditText)findViewById(R.id.team_value);
                String team = teamEditText.getText().toString();
                extras.putString("team", team);


                // get playerlist
                int count=0;
                for(EditText player : playerListEditText) {
                    String playerName = player.getText().toString();
                    if (playerName.matches(""))
                        continue;
                    extras.putString(String.valueOf(count), playerName);
                    playerList.add(playerName);
                    count++;
                }
                extras.putString("numOfPlayers", String.valueOf(count));

                // showAlert
                if (team.matches(""))
                    showAlertAndFinish(R.string.warning,R.string.no_team_name);
                else if(count<5)
                    showAlertAndFinish(R.string.warning,R.string.player_not_enough);
                else {

                    // save the team
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                        final String str = formatter.format(curDate);
                        //final String filename = "match/" + str + ".match";

                        String folder = "team";
                        File team_dir = getDir(folder, Context.MODE_PRIVATE);
                        File team_file = new File(team_dir, str + ".team");
                        System.out.println("filepath: " + team_file.getAbsolutePath().toString());
                        FileOutputStream fileout = new FileOutputStream(team_file);
                        JSONObject jsonobj = new JSONObject();
                        jsonobj.put("TeamName", team);

                        jsonobj.put("numOfPlayer", playerList.size());
                        System.out.println("jsonobj: " + jsonobj.toString());
                        for (int j = 0; j < playerList.size(); j++) {
                            jsonobj.put( String.valueOf(j), playerList.get(j) );
                        }
                        System.out.println("jsonobj: " + jsonobj.toString());
                        fileout.write(jsonobj.toString().getBytes());
                        fileout.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlertAndFinish(R.string.error, R.string.file_open_error);
                    }



                    // Back to new match
                    i.putExtras(extras);
                    setResult(RESULT_OK,i);
                    finish();
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
    }



    
}