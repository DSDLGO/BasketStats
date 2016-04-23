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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
