package com.basketstats.basketstats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {


    private List<EditText> playerList = new ArrayList<EditText>();      // store the name of input players
    private int startID = 3000;
    private int benchID = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* set up layout for starting players */
        LinearLayout startingLayout = new LinearLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(FILL_PARENT, WRAP_CONTENT);
        startingLayout.setLayoutParams(params);
        startingLayout.setOrientation(VERTICAL);

        /* Generate Register View */
        startingLayout.addView(typeLabel("Starting Players", startID));
        for(int i = 1; i <= 5; i++)
            startingLayout.addView(playerName(String.valueOf(i)));
        startingLayout.addView(typeLabel("Bench Players", benchID));
        for(int i = 6; i <= 12; i++)
            startingLayout.addView(playerName(String.valueOf(i)));
        setContentView(startingLayout);
    }


    /* Generate Text View (Starting , bench )*/
    private TextView typeLabel (String label, int id) {
        TextView view = new TextView(this);
        view.setId(id);
        view.setTextSize(20);
        view.setText(label);
        return view;
    }

    /* Generate Text field  */
    private EditText playerName (String num) {
        EditText editText = new EditText(this);
        editText.setId(Integer.valueOf(num));
        editText.setHint("Player " + num);
        playerList.add(editText);
        return editText;
    }
}

