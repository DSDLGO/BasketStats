package com.basketstats.basketstats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SavedTeam extends AppCompatActivity {

    private Button OKButton;
    private Button BackButton;
    private ListView TeamListView;
    private TextView ShowTextView;

    // team_list : team name
    // team_list_player : player for each team
    // file_list : filename for each team
    private ArrayList<String> team_list = new ArrayList<String>();
    private ArrayList<ArrayList<String>> team_list_player = new ArrayList<ArrayList<String>>();
    private ArrayList<String> file_list = new ArrayList<>();

    private Bundle teaminfo = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_team);


        getViews();
        getTeamList();
        showTeamList();
        processControllers();
    }

    private void getViews() {

        OKButton = (Button)findViewById(R.id.ok);
        BackButton = (Button)findViewById(R.id.back);
        TeamListView = (ListView)findViewById((R.id.teamlist));
        ShowTextView = (TextView)findViewById(R.id.text_show);

    }


    private void getTeamList() {

        team_list.clear();
        team_list_player.clear();
        file_list.clear();

        // access .team file

        try{
            File f = getDir("team", Context.MODE_PRIVATE);
            File[] file = f.listFiles();
            if(file != null) {
                Log.d("Files", "Size: " + file.length);
                for (int i = 0; i < file.length; i++) {
                    Log.d("Files", "FileName:" + file[i].getName());
                    file_list.add(file[i].getName());
                }


                for(String filename :file_list) {
                    File dir = getDir("team", Context.MODE_PRIVATE);
                    File team_file = new File(dir, filename);
                    FileInputStream fi = new FileInputStream(team_file);
                    InputStreamReader isr = new InputStreamReader(fi);
                    char[] inputBuffer= new char[5000];
                    int charRead;

                    String s = "";
                    while ((charRead = isr.read(inputBuffer)) > 0) {
                        // char to string conversion
                        System.out.println(charRead);
                        String readstring = String.copyValueOf(inputBuffer,0,charRead);
                        s += readstring;
                        System.out.println(readstring);

                        Log.d("VAR", s);
                        try {

                            JSONObject json = new JSONObject(s);
                            String team = (String) json.get("TeamName");
                            int numOfPlayer = (int) json.get("numOfPlayer");
                            team_list.add(team);

                            ArrayList<String> playerList = new ArrayList<String>();
                            for(int i = 0; i < numOfPlayer; i++){
                                playerList.add((String)json.get(String.valueOf(i)));
                            }
                            team_list_player.add(playerList);

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.e("error", "Json error");
                        }
                    }
                    isr.close();
                }
            }


        }catch (IOException e){
            e.printStackTrace();
            showAlertAndFinish(R.string.error, R.string.file_open_error);
        }


    }

    private void showTeamList() {
        //show list view
        int layoutId = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, layoutId, team_list);
        TeamListView.setAdapter(adapter);
    }

    private void showAlert(int titleId, int messageId){
        new AlertDialog.Builder(SavedTeam.this)
                .setTitle(titleId)
                .setMessage(messageId)
                .show();
    }

    private void showAlertAndFinish(int titleId, int messageId){
        new AlertDialog.Builder(SavedTeam.this)
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

    private void showEditDeleteCancel(String team, final int selected) {
        new AlertDialog.Builder(SavedTeam.this)
                .setTitle("Team : "+team)
                .setMessage("You can do these actions to " + team)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteFile(selected);
                        showTeamList();
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        //put information of team into intent
                        Intent i = new Intent(SavedTeam.this, EditTeam.class);
                        Bundle extras = new Bundle();

                        //team name
                        extras.putString("team", team_list.get(selected));

                        // playerlist
                        ArrayList<String> playerList = team_list_player.get(selected);
                        for(int j=0;j< playerList.size();j++){
                            extras.putString(String.valueOf(j),playerList.get(j));
                        }
                        extras.putString("numOfPlayers", String.valueOf(playerList.size()));

                        extras.putString("removeFile",file_list.get(selected));
                        // remove old file before edit
                        //deleteFile(selected);

                        // to edit team
                        i.putExtras(extras);
                        startActivityForResult(i,0);

                    }
                })
                .show();
    }

    private void deleteFile(int selected) {

        //delete file
        File dir = getDir("team", Context.MODE_PRIVATE);
        File file_delete = new File(dir, file_list.get(selected));
        file_delete.delete();

        //remove array list
        team_list.remove(selected);
        team_list_player.remove(selected);
        file_list.remove(selected);
    }



    private void processControllers() {

        //add OK button listener
        View.OnClickListener OKListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( teaminfo.isEmpty() ) {
                    showAlert(R.string.warning, R.string.no_team);
                }
                else {
                    Intent i = new Intent(SavedTeam.this, NewMatch.class);
                    i.putExtras(teaminfo);
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


        //add Team ListView long listener(edit delete)
        AdapterView.OnItemLongClickListener TeamListViewLongListener = new AdapterView.OnItemLongClickListener() {
            // 第一個參數是使用者操作的ListView物件
            // 第二個參數是使用者選擇的項目
            // 第三個參數是使用者選擇的項目編號，第一個是0
            // 第四個參數在這裡沒有用途
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                showEditDeleteCancel(team_list.get(position),position);

                return true;
            }

        };
        TeamListView.setOnItemLongClickListener(TeamListViewLongListener);



        // add Team ListView listener(view)
        AdapterView.OnItemClickListener TeamListViewListener = new AdapterView.OnItemClickListener() {
            // 第一個參數是使用者操作的ListView物件
            // 第二個參數是使用者選擇的項目
            // 第三個參數是使用者選擇的項目編號，第一個是0
            // 第四個參數在這裡沒有用途
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                Bundle extras = new Bundle();
                String text="";
                text += "Team Name " + team_list.get(position) + "\n";
                ArrayList<String> playerList = team_list_player.get(position);
                for(int j=0;j< playerList.size();j++){
                    text += playerList.get(j) + "\n";
                    extras.putString(String.valueOf(j),playerList.get(j));
                }
                text += "Number of Players " + playerList.size();
                ShowTextView.setText(text);

                extras.putString("numOfPlayers",String.valueOf(playerList.size()));
                extras.putString("team", team_list.get(position));

                teaminfo = extras ;
                

            }

        };
        TeamListView.setOnItemClickListener(TeamListViewListener);


    }



    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            // 0 : edit team result
            if(requestCode==0){
                /*
                Bundle extras = data.getExtras();
                String team = extras.getString("team");
                String s ="";
                s += team + "\n";


                int numOfPlayers = Integer.parseInt(extras.getString("numOfPlayers"));
                for (int i = 0; i < numOfPlayers; i++) {
                    String playerName = extras.getString(String.valueOf(i));
                    s += playerName += "\n";
                    //gameinfo.putString(String.valueOf(i), playerName);
                }
                s += String.valueOf(numOfPlayers) + "\n";
                ShowTextView.setText(s);
                //gameinfo.putString("numOfPlayers", String.valueOf(numOfPlayers));
                */
                getTeamList();
                showTeamList();
                ShowTextView.setText("");
                teaminfo.clear();
            }


        }
    }


}
