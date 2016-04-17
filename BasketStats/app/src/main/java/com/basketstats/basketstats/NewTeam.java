package com.basketstats.basketstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewTeam extends AppCompatActivity {

    private Button OKButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        getViews();
        processControllers();
    }

    private void getViews() {

        OKButton = (Button)findViewById(R.id.ok);

    }

    private void processControllers() {

        //add OK button listener
        View.OnClickListener OKListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get opponent from EditText
                EditText teamEditText = (EditText)findViewById(R.id.team_value);
                String team = teamEditText.getText().toString();

                Intent i = new Intent(NewTeam.this, NewMatch.class);
                Bundle extras = new Bundle();
                extras.putString("team", team);
                i.putExtras(extras);
                setResult(RESULT_OK,i);
                finish();
            }
        };
        OKButton.setOnClickListener(OKListener);





    }
}
