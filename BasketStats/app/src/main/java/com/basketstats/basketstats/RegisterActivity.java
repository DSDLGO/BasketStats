package com.basketstats.basketstats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;


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
        startingLayout.addView(startGameButton());
        setContentView(startingLayout);
    }

    /* submit button */
    private Button startGameButton() {
        Button button = new Button(this);
        button.setHeight(WRAP_CONTENT);
        button.setText("Start Game!");
        button.setOnClickListener(startGameListener);
        return button;
    }

    private View.OnClickListener startGameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int count = 0;
            String team = "台大資工";
            String date = "2016/04/25";
            String opp = "GSW";
            Intent i = new Intent(RegisterActivity.this, statRecord.class);
            Bundle extras = new Bundle();
            extras.putString("team", team);
            extras.putString("date", date);
            extras.putString("opp", opp);
            for(EditText editText : playerList) {
                String playerName = editText.getText().toString();
                if (playerName.matches(""))
                    continue;
                extras.putString(String.valueOf(count), playerName);
                count++;
            }
            extras.putString("numOfPlayers", String.valueOf(count));
            i.putExtras(extras);
            startActivity(i);
        }
    };

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

