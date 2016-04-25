package com.basketstats.basketstats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class LoadActivity extends AppCompatActivity {

    private TextView load_show;
    private TextView load_show_name;
    private TextView load_show_1pt;
    private TextView load_show_2pt;
    private TextView load_show_3pt;
    private TextView load_show_rebound;
    private TextView load_show_ast;
    private TextView load_show_stl;
    private TextView load_show_to;
    private TextView load_show_pf;
    private TextView load_show_blk;
    private Button chooseFile;
    private String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        chooseFile = (Button) findViewById(R.id.choose_file);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(LoadActivity.this);
                final View view = inflater.inflate(R.layout.dialog_choosefile, null);
                final Spinner filespinner = (Spinner) view.findViewById(R.id.spinner_choose);
                File f = getDir("match", Context.MODE_PRIVATE);
                File[] file = f.listFiles();
                ArrayList<String> file_list = new ArrayList<>();
                if(file != null) {
                    Log.d("Files", "Size: " + file.length);
                    for (int i = 0; i < file.length; i++) {
                        Log.d("Files", "FileName:" + file[i].getName());
                        file_list.add(file[i].getName());
                    }
                }

                ArrayAdapter<String> fileList = new ArrayAdapter<String>(LoadActivity.this, android.R.layout.simple_spinner_item, file_list);
                filespinner.setAdapter(fileList);
                filespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        String selected = arg0.getItemAtPosition(position).toString();
                        filespinner.setTag(selected);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        filespinner.setTag("");
                    }
                });

                new AlertDialog.Builder(LoadActivity.this)
                        .setTitle("Choose file")
                        .setView(view)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Spinner spinner = (Spinner) (view.findViewById(R.id.spinner_choose));
                                filename = spinner.getTag().toString();
                                showFile();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .show();

            }
        });
        load_show = (TextView) findViewById(R.id.load_info);
        load_show_name = (TextView) findViewById(R.id.load_show_name);
        load_show_1pt = (TextView) findViewById(R.id.load_show_1pt);
        load_show_2pt = (TextView) findViewById(R.id.load_show_2pt);
        load_show_3pt = (TextView) findViewById(R.id.load_show_3pt);
        load_show_rebound = (TextView) findViewById(R.id.load_show_rebound);
        load_show_ast = (TextView) findViewById(R.id.load_show_ast);
        load_show_stl = (TextView) findViewById(R.id.load_show_stl);
        load_show_to = (TextView) findViewById(R.id.load_show_to);
        load_show_pf = (TextView) findViewById(R.id.load_show_pf);
        load_show_blk = (TextView) findViewById(R.id.load_show_blk);

    }

    private void showAlertAndFinish(int titleId, int messageId){
        new AlertDialog.Builder(LoadActivity.this)
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

    private void showFile(){
        String s = "";
        try{
            File dir = getDir("match", Context.MODE_PRIVATE);
            File file = new File(dir, filename);
            FileInputStream fi = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fi);
            char[] inputBuffer= new char[5000];
            int charRead;

            while ((charRead = isr.read(inputBuffer)) > 0) {
                // char to string conversion
                System.out.println(charRead);
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s += readstring;
                System.out.println(readstring);
            }
            isr.close();

        }catch (IOException e){
            e.printStackTrace();
            showAlertAndFinish(R.string.error, R.string.file_open_error);
        }

        Log.d("VAR", s);
        try {

            JSONObject json = new JSONObject(s);
            String home_name = (String) json.get("home");
            String away_name = (String) json.get("away");
            int away_score = (Integer) json.get("away_score") ;
            int home_score = (Integer) json.get("home_score") ;
            int numOfPlayer = (int) json.get("numOfPlayer");
            JSONArray playerList = json.getJSONArray("playerList");
            ArrayList<String> playerNames = new ArrayList<>();
            for(int i = 0; i < numOfPlayer; i++){
                playerNames.add((String)playerList.get(i));
            }
            String text3p ="3pt\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                text3p += jsonObject.getString("_3pm") + "/" + jsonObject.getString("_3pa") + "\n";
            }
            String text2p="2pt\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                text2p += jsonObject.getString("_2pm") + "/" + jsonObject.getString("_2pa") + "\n";
            }
            String text1p="ft\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                text1p += jsonObject.getString("ftm") + "/" + jsonObject.getString("fta") + "\n";
            }
            String textrb="reb\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                textrb +=  jsonObject.getString("reb") + "\n";
            }
            String textast="ast\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                textast +=  jsonObject.getString("ast") + "\n";
            }
            String textstl="stl\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                textstl +=  jsonObject.getString("stl") + "\n";
            }
            String textto="to\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                textto +=  jsonObject.getString("to") + "\n";
            }
            String textpf="pf\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                textpf +=  jsonObject.getString("pf") + "\n";
            }
            String textbs="bs\n";
            for(int j = 0 ; j < numOfPlayer; j++){
                JSONObject jsonObject = json.getJSONObject(playerList.get(j) + "stat");
                textbs +=  jsonObject.getString("bs") + "\n";
            }

            String text = "";
            String textplayers ="Players\n";
            text += "home: " + home_name + "\n";
            text += "opponent: " + away_name + "\n";
            text += "score : (opponent)" + away_score +" : (home)" + home_score + "\n";
            for(int i = 0; i < numOfPlayer; i++){
                textplayers += playerNames.get(i) + "\n";
            }
            load_show.setText(text);
            load_show_name.setText(textplayers);
            load_show_1pt.setText(text1p);
            load_show_2pt.setText(text2p);
            load_show_3pt.setText(text3p);
            load_show_rebound.setText(textrb);
            load_show_ast.setText(textast);
            load_show_stl.setText(textstl);
            load_show_to.setText(textto);
            load_show_pf.setText(textpf);
            load_show_blk.setText(textbs);
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("error", "Json error");
        }
    }
}
/*
public class LoadActivity extends AppCompatActivity {

    private TextView load_show;
    private Button chooseFile;
    private String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        chooseFile = (Button) findViewById(R.id.choose_file);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(LoadActivity.this);
                final View view = inflater.inflate(R.layout.dialog_choosefile, null);
                final Spinner filespinner = (Spinner) view.findViewById(R.id.spinner_choose);
                File f = getDir("match", Context.MODE_PRIVATE);
                File[] file = f.listFiles();
                ArrayList<String> file_list = new ArrayList<>();
                if(file != null) {
                    Log.d("Files", "Size: " + file.length);
                    for (int i = 0; i < file.length; i++) {
                        Log.d("Files", "FileName:" + file[i].getName());
                        file_list.add(file[i].getName());
                    }
                }

                ArrayAdapter<String> fileList = new ArrayAdapter<String>(LoadActivity.this, android.R.layout.simple_spinner_item, file_list);
                filespinner.setAdapter(fileList);
                filespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        String selected = arg0.getItemAtPosition(position).toString();
                        filespinner.setTag(selected);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        filespinner.setTag("");
                    }
                });

                new AlertDialog.Builder(LoadActivity.this)
                        .setTitle("Choose file")
                        .setView(view)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Spinner spinner = (Spinner) (view.findViewById(R.id.spinner_choose));
                                filename = spinner.getTag().toString();
                                showFile();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .show();

            }
        });
        load_show = (TextView) findViewById(R.id.load_show);

    }

    private void showAlertAndFinish(int titleId, int messageId){
        new AlertDialog.Builder(LoadActivity.this)
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

    private void showFile(){
        try{
            File dir = getDir("match", Context.MODE_PRIVATE);
            File file = new File(dir, filename);
            FileInputStream fi = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fi);
            char[] inputBuffer= new char[5000];
            String s="";
            int charRead;

            while ((charRead = isr.read(inputBuffer)) > 0) {
                // char to string conversion
                System.out.println(charRead);
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s += readstring;
                System.out.println(readstring);
            }
            isr.close();

            System.out.println("string: " + s);
            load_show.setText(s);
        }catch (IOException e){
            e.printStackTrace();
            showAlertAndFinish(R.string.error, R.string.file_open_error);
        }
    }
}
*/