package com.basketstats.basketstats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class statRecord extends AppCompatActivity {

    private ArrayList<String> playerList = new ArrayList<String>();
    private String home_team, away_team, date;
    private int home_score = 0, away_score = 0;
    private Button homeTeamButton, awayTeamButton;
    private Button playerButton1, playerButton2, playerButton3, playerButton4, playerButton5;
    private TextView homeTeamScore, awayTeamScore;
    private TextView playerStatusName, playerStatusString;
    private TextView playerSelected, actionSelected;
    private ListView leftDrawer;
    private String[] drawerList = {"Play by Play"};
    private Hashtable playerRecords;
    private RecordLog Log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_record);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
            String str = formatter.format(curDate);
            FileOutputStream fileout = openFileOutput(str + "record.log", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            Log = new RecordLog(outputWriter);
        }catch(Exception e){
            e.printStackTrace();
            showAlertAndFinish(R.string.error, R.string.file_open_error);
        }

        /* get players name from register activity */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int numOfPlayers = Integer.parseInt(extras.getString("numOfPlayers"));
        home_team = extras.getString("team");
        away_team = extras.getString("opp");
        date = extras.getString("date");

        Log.log(Log.info, "team=" + home_team);
        Log.log(Log.info, "opp=" + away_team);
        Log.log(Log.info, "date=" + date);
        Log.log(Log.info, "numOfPlayers=" + String.valueOf(numOfPlayers));

        for (int i = 0; i < numOfPlayers; i++) {
            String playerName = extras.getString(String.valueOf(i));
            playerList.add(playerName);
            Log.log(Log.info, "player" + String.valueOf(i) + "=" + playerName);
        }

        preprocessPlayerRecord();

        getViews();
        processViews();
        precessControllers();
    }

    private void preprocessPlayerRecord(){
        playerRecords = new Hashtable();
        for(int i = 0; i < playerList.size(); i++){
            playerRecords.put(playerList.get(i), new PlayerRecord());
        }
    }

    private void getViews(){
        homeTeamButton = (Button) findViewById(R.id.button_home_team);
        awayTeamButton = (Button) findViewById(R.id.button_away_team);

        homeTeamScore = (TextView) findViewById(R.id.score_home_team);
        awayTeamScore = (TextView) findViewById(R.id.score_away_team);

        playerStatusName = (TextView) findViewById(R.id.name_player_status);
        playerStatusString = (TextView) findViewById(R.id.string_player_status);

        playerSelected = (TextView) findViewById(R.id.player_selected);
        actionSelected = (TextView) findViewById(R.id.action_selected);

        playerButton1 = (Button) findViewById(R.id.button_player_1);
        playerButton2 = (Button) findViewById(R.id.button_player_2);
        playerButton3 = (Button) findViewById(R.id.button_player_3);
        playerButton4 = (Button) findViewById(R.id.button_player_4);
        playerButton5 = (Button) findViewById(R.id.button_player_5);
    }

    private void processViews(){
        // set team name buttons
        homeTeamButton.setText(home_team);
        homeTeamButton.setTag("home_team");
        awayTeamButton.setText(away_team);
        awayTeamButton.setTag("away_team");

        // set team scores to 0
        homeTeamScore.setText(String.valueOf(home_score));
        awayTeamScore.setText(String.valueOf(away_score));

        // set player status bar to be empty
        playerStatusName.setText("");
        playerStatusString.setText("");

        // set selected player to be empty
        playerSelected.setText("");
        actionSelected.setText("");

        // numOfPlayers should be at least 5
        if(playerList.size() >= 5) {
            playerButton1.setText(playerList.get(0));
            playerButton1.setTag(playerList.get(0));
            playerButton2.setText(playerList.get(1));
            playerButton2.setTag(playerList.get(1));
            playerButton3.setText(playerList.get(2));
            playerButton3.setTag(playerList.get(2));
            playerButton4.setText(playerList.get(3));
            playerButton4.setTag(playerList.get(3));
            playerButton5.setText(playerList.get(4));
            playerButton5.setTag(playerList.get(4));
        }
        else{ // show a warning dialog
            showAlertAndFinish(R.string.warning, R.string.player_not_enough);
        }

        leftDrawer = (ListView) findViewById(R.id.left_drawer);
        ListAdapter mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                drawerList);
        leftDrawer.setAdapter(mAdapter);
    }

    private void precessControllers(){

        // add side menu item listener
        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "你選擇的是" + drawerList[position], Toast.LENGTH_SHORT).show();
            }
        });

        // add score button listener
        View.OnClickListener scoreButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == homeTeamButton.getId()){
                    home_score += 1;
                    homeTeamScore.setText(String.valueOf(home_score));
                    Log.log(Log.command, "viewId=" + String.valueOf(v.getId()));
                }
                else if(v.getId() == awayTeamButton.getId()){
                    away_score += 1;
                    awayTeamScore.setText(String.valueOf(away_score));
                    Log.log(Log.command, "viewId=" + String.valueOf(v.getId()));
                }
            }
        };
        homeTeamButton.setOnClickListener(scoreButtonListener);
        awayTeamButton.setOnClickListener(scoreButtonListener);

        // add choose player button listener
        View.OnClickListener playerButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                playerStatusName.setText(tag);
                playerSelected.setText(tag);

                PlayerRecord record = (PlayerRecord) playerRecords.get(tag);
                int pts = record.pts, reb = record.reb, ast = record.ast;
                playerStatusString.setText(String.valueOf(pts) + "pts " + String.valueOf(reb) + "reb " + String.valueOf(ast) + "ast");
            }
        };
        playerButton1.setOnClickListener(playerButtonListener);
        playerButton2.setOnClickListener(playerButtonListener);
        playerButton3.setOnClickListener(playerButtonListener);
        playerButton4.setOnClickListener(playerButtonListener);
        playerButton5.setOnClickListener(playerButtonListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_statrecord, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_undo:
                Toast.makeText(this, "Undo", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_save:
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertAndFinish(int titleId, int messageId){
        new AlertDialog.Builder(statRecord.this)
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

    public void ActionClicked(View view){
        if(view instanceof Button) {
            Button button = (Button) view;
            actionSelected.setText(button.getText().toString());
            actionSelected.setTag(button.getTag().toString());
        }
    }

    public void ActionSubmit(View view){
        if(view.getId() == R.id.button_player_selected) {
            if (playerSelected.getText().toString() != "" && actionSelected.getText().toString() != "") {
                String playerName = playerSelected.getText().toString();
                String action = actionSelected.getTag().toString();
                PlayerRecord record = (PlayerRecord) playerRecords.get(playerName);
                int plus = 1;
                try {
                    Method method = record.getClass().getMethod(action, new Class[] { int.class });
                    method.invoke(record, new Object[] { plus });
                }catch (Exception e){
                    e.printStackTrace();
                    showAlertAndFinish(R.string.warning, R.string.player_name_wrong);
                }

                playerSelected.setText("");
                actionSelected.setText("");
                actionSelected.setTag("");
                int pts = record.pts, reb = record.reb, ast = record.ast;
                playerStatusString.setText(String.valueOf(pts) + "pts " + String.valueOf(reb) + "reb " + String.valueOf(ast) + "ast");
            }
        }
    }


}