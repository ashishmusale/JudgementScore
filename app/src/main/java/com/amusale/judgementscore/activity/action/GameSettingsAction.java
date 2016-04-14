package com.amusale.judgementscore.activity.action;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amusale.judgementscore.DBHelper;
import com.amusale.judgementscore.R;

public class GameSettingsAction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings_action);

        setTitle("Game Settings");


        Button resetGames = (Button)findViewById(R.id.resetGames);
        Button resetUsers = (Button)findViewById(R.id.resetUsers);

        final DBHelper dbHelper = new DBHelper(this);
        resetGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.resetGames();
                Toast.makeText(getApplicationContext(), "All games reset", Toast.LENGTH_SHORT).show();
            }
        });

        resetUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.resetUsers();
                Toast.makeText(getApplicationContext(), "All users removed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
