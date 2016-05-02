package com.basketstats.basketstats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditTeam extends AppCompatActivity {

    private Button OKButton;
    private Button BackButton;
    private EditText teamEditText;
    private LinearLayout PlayerListLayout;

    private String removeFile;

    private List<EditText> playerListEditText = new ArrayList<EditText>();      // store the name of input players
    private List<String> playerList = new ArrayList<String>();

    private List<String> newPlayerList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        getViews();
        getTeam();
        processControllers();

    }

    private void getViews() {

        OKButton = (Button)findViewById(R.id.ok);
        BackButton = (Button)findViewById(R.id.back);
        PlayerListLayout = (LinearLayout)findViewById(R.id.player_list);
        teamEditText = (EditText)findViewById(R.id.team_value);

    }

    private void getTeam() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String team = extras.getString("team");
        int numOfPlayers = Integer.parseInt(extras.getString("numOfPlayers"));
        for (int i = 0; i < numOfPlayers; i++) {
            String playerName = extras.getString(String.valueOf(i));
            playerList.add(playerName);
        }

        //set team edit text for name
        teamEditText.setText(team);

        //create playerlist
        createPlayList(numOfPlayers);

        removeFile = extras.getString("removeFile");

    }


    private void createPlayList(int numOfPlayers) {

        for(int i=0;i<numOfPlayers;i++)
            PlayerListLayout.addView(createEditText(i,playerList.get(i)));

    }

    private EditText createEditText(int num,String playerName) {
        EditText playerEditText = new EditText(this);
        playerEditText.setId(Integer.valueOf(num));
        playerEditText.setText(playerName);
        playerListEditText.add(playerEditText);
        return playerEditText;
    }



    private void showAlert(int titleId, int messageId){
        new AlertDialog.Builder(EditTeam.this)
                .setTitle(titleId)
                .setMessage(messageId)
                .show();
    }

    private void showAlertAndFinish(int titleId, int messageId){
        new AlertDialog.Builder(EditTeam.this)
                .setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void processControllers() {

        //add OK button listener
        View.OnClickListener OKListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EditTeam.this, SavedTeam.class);
                Bundle extras = new Bundle();

                // get new team name from EditText
                String team = teamEditText.getText().toString();
                extras.putString("team", team);


                // get playerlist
                int count=0;
                for(EditText player : playerListEditText) {
                    String playerName = player.getText().toString();
                    if (playerName.matches(""))
                        continue;
                    extras.putString(String.valueOf(count), playerName);
                    newPlayerList.add(playerName);
                    count++;
                }
                extras.putString("numOfPlayers", String.valueOf(count));

                // showAlert
                if (team.matches(""))
                    showAlert(R.string.warning, R.string.no_team_name);
                else if(count<5)
                    showAlert(R.string.warning, R.string.player_not_enough);
                else {

                    // save the team
                    saveTeamtoFile(team);

                    deleteFile();

                    // Back to new match
                    //i.putExtras(extras);
                    setResult(RESULT_OK, i);
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

    private void deleteFile() {

        //delete file
        File dir = getDir("team", Context.MODE_PRIVATE);
        File file_delete = new File(dir, removeFile);
        file_delete.delete();

    }

    private void saveTeamtoFile(String team) {

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
            jsonobj.put("numOfPlayer", newPlayerList.size());
            System.out.println("jsonobj: " + jsonobj.toString());
            for (int j = 0; j < newPlayerList.size(); j++) {
                jsonobj.put(String.valueOf(j), newPlayerList.get(j));
            }
            System.out.println("jsonobj: " + jsonobj.toString());
            fileout.write(jsonobj.toString().getBytes());
            fileout.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlertAndFinish(R.string.error, R.string.file_open_error);
        }

    }


}
