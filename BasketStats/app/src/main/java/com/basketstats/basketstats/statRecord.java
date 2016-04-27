package com.basketstats.basketstats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class statRecord extends AppCompatActivity {

    private ArrayList<String> playerList = new ArrayList<String>();
    private String home_team, away_team;
    private int home_score = 0, away_score = 0;
    private Button homeTeamButton, awayTeamButton;
    private Button[] playerButtons;
    private TextView homeTeamScore, awayTeamScore;
    private TextView playerStatusName, playerStatusString;
    private TextView playerSelected, actionSelected;
    private TextView leftDrawer;
    private String[] drawerList = {"Play by Play"};
    private Hashtable playerRecords;
    private Typeface typeface;
    private Hashtable actionNameToMethod;
    private RecordLog log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_record);

        // create log instance
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
            String str = formatter.format(curDate);
            FileOutputStream fileout = openFileOutput(str + "record.log", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(str + "\n");
            FileInputStream filein = openFileInput(str + "record.log");
            log = new RecordLog(outputWriter, filein);
        }catch(Exception e){
            e.printStackTrace();
            showAlertAndFinish(R.string.error, R.string.file_open_error);
        }

        /* get players' name from register activity */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int numOfPlayers = Integer.parseInt(extras.getString("numOfPlayers"));
        home_team = extras.getString("team");
        away_team = extras.getString("opp");

        /* set up led font */
        typeface = Typeface.createFromAsset(getAssets(), "fonts/LED.ttf");

        /* log some info */
        log.log(log.info, "team=" + home_team);
        log.log(log.info, "opp=" + away_team);
        log.log(log.info, "numOfPlayers=" + String.valueOf(numOfPlayers));

        for (int i = 0; i < numOfPlayers; i++) {
            String playerName = extras.getString(String.valueOf(i));
            playerList.add(playerName);
            log.log(log.info, "player" + String.valueOf(i) + "=" + playerName);
        }

        preprocessPlayerRecord();
        preprocessAction();

        getViews();
        processViews();
        precessControllers();
    }

    private void preprocessPlayerRecord(){
        playerRecords = new Hashtable();
        actionNameToMethod = new Hashtable();

        /* put records into hashtable */
        for(int i = 0; i < playerList.size(); i++){
            playerRecords.put(playerList.get(i), new PlayerRecord());
        }
    }

    private void preprocessAction(){

        /* put text-method relation */
        actionNameToMethod.put("罰球中", "ftm");
        actionNameToMethod.put("罰球不中", "fta");
        actionNameToMethod.put("兩分中", "_2pm");
        actionNameToMethod.put("兩分不中", "_2pa");
        actionNameToMethod.put("三分中", "_3pm");
        actionNameToMethod.put("三分不中", "_3pa");
        actionNameToMethod.put("籃板", "reb");
        actionNameToMethod.put("助攻", "ast");
        actionNameToMethod.put("抄截", "stl");
        actionNameToMethod.put("失誤", "to");
        actionNameToMethod.put("犯規", "pf");
        actionNameToMethod.put("火鍋", "bs");
    }

    private void getViews(){
        homeTeamButton = (Button) findViewById(R.id.button_home_team);
        awayTeamButton = (Button) findViewById(R.id.button_away_team);

        homeTeamScore = (TextView) findViewById(R.id.score_home_team);
        awayTeamScore = (TextView) findViewById(R.id.score_away_team);

        homeTeamScore.setTypeface(typeface);
        awayTeamScore.setTypeface(typeface);

        playerStatusName = (TextView) findViewById(R.id.name_player_status);
        playerStatusString = (TextView) findViewById(R.id.string_player_status);

        playerSelected = (TextView) findViewById(R.id.player_selected);
        actionSelected = (TextView) findViewById(R.id.action_selected);

        playerButtons = new Button[5];
        playerButtons[0] = (Button) findViewById(R.id.button_player_1);
        playerButtons[1] = (Button) findViewById(R.id.button_player_2);
        playerButtons[2] = (Button) findViewById(R.id.button_player_3);
        playerButtons[3] = (Button) findViewById(R.id.button_player_4);
        playerButtons[4] = (Button) findViewById(R.id.button_player_5);
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
            for(int i = 0; i < 5; i++){
                playerButtons[i].setText(playerList.get(i));
                playerButtons[i].setTag(playerList.get(i));
            }
        }
        else{
            // show a warning dialog
            showAlertAndFinish(R.string.warning, R.string.player_not_enough);
        }

        leftDrawer = (TextView) findViewById(R.id.left_drawer);

        leftDrawer.setText("");
    }

    private void precessControllers(){

        // add score button listener
        View.OnClickListener scoreButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == homeTeamButton.getId()){
                    home_score += 1;
                    homeTeamScore.setText(String.valueOf(home_score));
                    log.log(log.commandLog, "home_score=+1");
                }
                else if(v.getId() == awayTeamButton.getId()){
                    away_score += 1;
                    awayTeamScore.setText(String.valueOf(away_score));
                    log.log(log.commandLog, "away_score=+1");
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

                setPlayerStatusString(tag);
            }
        };

        for(int i = 0; i < 5; i++)
            playerButtons[i].setOnClickListener(playerButtonListener);
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
                undo();
                return true;
            case R.id.action_save:
                save();
                return true;
            case R.id.action_switch:
                switchPlayer();
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
        // check: the view should be a button
        if(view instanceof Button) {
            Button button = (Button) view;
            actionSelected.setText(button.getText().toString());
            actionSelected.setTag(button.getTag().toString());
        }
    }

    public void ActionSubmit(View view){
        if(view.getId() == R.id.button_player_selected) {
            // if both player and action are selected
            if (playerSelected.getText().toString() != "" && actionSelected.getText().toString() != "") {
                String playerName = playerSelected.getText().toString();
                String actionName = actionSelected.getText().toString();
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

                // empty all fields
                playerSelected.setText("");
                actionSelected.setText("");
                actionSelected.setTag("");

                setPlayerStatusString(playerName);

                // update log and play-by-play list
                log.log(log.playLog, playerName, actionName);
                leftDrawer.setText(log.getPlayByPlayList());
            }
        }
    }

    private void setPlayerStatusString(String tag){
        PlayerRecord record = (PlayerRecord) playerRecords.get(tag);
        int pts = record.pts, reb = record.reb, ast = record.ast;
        playerStatusString.setText(String.valueOf(pts) + "pts " + String.valueOf(reb) + "reb " + String.valueOf(ast) + "ast");
    }

    private void switchPlayer(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View v = inflater.inflate(R.layout.dialog_switchplayer, null);
        final Spinner on_field_player = (Spinner) v.findViewById(R.id.spinner_switch1);
        final Spinner bench_player = (Spinner) v.findViewById(R.id.spinner_switch2);

        ArrayList<String> on_field_player_list = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            on_field_player_list.add(playerButtons[i].getText().toString());
        ArrayAdapter<String> onFieldList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, on_field_player_list);
        on_field_player.setAdapter(onFieldList);
        on_field_player.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
                String selected = arg0.getItemAtPosition(position).toString();
                on_field_player.setTag(selected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                on_field_player.setTag("");
            }
        });

        ArrayList<String> bench_player_list = new ArrayList<>(playerList);
        for(int i = 0; i < 5; i++)
            bench_player_list.remove(playerButtons[i].getText().toString());
        ArrayAdapter<String> benchList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bench_player_list);
        bench_player.setAdapter(benchList);
        bench_player.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String selected = arg0.getItemAtPosition(position).toString();
                bench_player.setTag(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                bench_player.setTag("");
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Switch player")
                .setView(v)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Spinner spinner1 = (Spinner) (v.findViewById(R.id.spinner_switch1));
                        Spinner spinner2 = (Spinner) (v.findViewById(R.id.spinner_switch2));

                        String on_field = spinner1.getTag().toString();
                        String bench = spinner2.getTag().toString();
                        if(!on_field.matches("") && !bench.matches("")){
                            int buttonIndex = 0;
                            for(int i = 0; i < 5; i++){
                                if(playerButtons[i].getText().toString() == on_field) buttonIndex = i;
                            }
                            playerButtons[buttonIndex].setText(bench);
                            playerButtons[buttonIndex].setTag(bench);
                            playerStatusName.setText("");
                            playerStatusString.setText("");
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
    }

    private void undo(){
        ArrayList<String> retLog = log.undo();
        if(retLog.isEmpty()) return;

        int type = log.getType(retLog.get(0));
        if(type == -1) return;
        else if(type == log.info){
            // do nothing
        }
        else if(type == log.commandLog){
            if(retLog.size() == 2){
                String str2 = retLog.get(1);
                if(str2 == "home_score=+1"){
                    home_score -= 1;
                    homeTeamScore.setText(String.valueOf(home_score));
                }
                else if(str2 == "away_score=+1"){
                    away_score -= 1;
                    awayTeamScore.setText(String.valueOf(away_score));
                }
            }
        }
        else if(type == log.playLog){
            if(retLog.size() == 3){
                String playerName = retLog.get(1);
                String action = retLog.get(2);
                String actionMethod = actionNameToMethod.get(action).toString();
                PlayerRecord record = (PlayerRecord) playerRecords.get(playerName);
                int minus = -1;
                try {
                    Method method = record.getClass().getMethod(actionMethod, new Class[] { int.class });
                    method.invoke(record, new Object[] { minus });
                }catch (Exception e){
                    e.printStackTrace();
                    showAlertAndFinish(R.string.warning, R.string.player_name_wrong);
                }

                playerSelected.setText("");
                actionSelected.setText("");
                actionSelected.setTag("");
                int pts = record.pts, reb = record.reb, ast = record.ast;
                playerStatusString.setText(String.valueOf(pts) + "pts " + String.valueOf(reb) + "reb " + String.valueOf(ast) + "ast");

                leftDrawer.setText(log.getPlayByPlayList());
            }
        }
    }

    private void save(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        final String str = formatter.format(curDate);
        final String filename = "match/" + str + ".match";
        new AlertDialog.Builder(this)
                .setTitle("Save")
                .setMessage("save file: " + "\n" + filename)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("saving file");
                        try {
                            String folder = "match";
                            File match_dir = getDir(folder, Context.MODE_PRIVATE);
                            File match_file = new File(match_dir, str + ".match");
                            System.out.println("filepath: " + match_file.getAbsolutePath().toString());
                            FileOutputStream fileout = new FileOutputStream(match_file);
                            JSONObject jsonobj = new JSONObject();
                            jsonobj.put("home", home_team);
                            jsonobj.put("away", away_team);
                            jsonobj.put("home_score", home_score);
                            jsonobj.put("away_score", away_score);
                            jsonobj.put("numOfPlayer", playerList.size());
                            System.out.println("jsonobj: " + jsonobj.toString());
                            JSONArray plist = new JSONArray();
                            for (int i = 0; i < playerList.size(); i++) {
                                plist.put(playerList.get(i));
                                JSONObject obj = ((PlayerRecord) playerRecords.get(playerList.get(i))).toJson();
                                jsonobj.put(playerList.get(i) + "stat", obj);
                            }
                            System.out.println("jsonobj: " + jsonobj.toString());
                            jsonobj.put("playerList", plist);

                            System.out.println("jsonobj: " + jsonobj.toString());
                            fileout.write(jsonobj.toString().getBytes());
                            fileout.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlertAndFinish(R.string.error, R.string.file_open_error);
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
    }
}