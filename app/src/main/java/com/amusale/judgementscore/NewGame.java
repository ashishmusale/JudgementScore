package com.amusale.judgementscore;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.amusale.judgementscore.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NewGame extends AppCompatActivity {

    private DBHelper dbHelper ;
    private ListView listView;
    private int gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        dbHelper = new DBHelper(this);

        gameId = getIntent().getIntExtra(MainActivity.SCORE_KEY_CONTACT_ID, 0);

        final ScoreAdapter scoreAdapter = new ScoreAdapter(this, R.layout.game_input, getUsers());
        listView = (ListView)findViewById(R.id.listUsers);
        listView.setAdapter(scoreAdapter);


        Button button = (Button) findViewById(R.id.saveGame);
        if (null != button) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Set<String> keys = scoreAdapter.getTextValues().keySet();
                    String points = "";
                    for (String key: keys) {

                        String[] keySplit = key.split(":");
                        if (keySplit.length == 2) {
                            String uid = keySplit[1];
                            String value = scoreAdapter.getTextValues().get(key);
                            points += uid + "=" + value;
                            points += ";";
                        }
                    }
                    dbHelper.insertScore(getWildCard(), getNumOfHands(), "In Progress", points);

                    Toast.makeText(getApplicationContext(), "New game created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });
        } else {
            Log.e("BUTTON", "button SAVE GAME was not found on the page");
        }
    }

    private String getWildCard() {

        int id = (gameId % 4);

        switch (id) {
            case 0: return "spade";
            case 1: return "diamond";
            case 2: return "clubs";
            case 3: return "hearts";
        }
        return "invalid";
    }

    private int getNumOfHands() {
        int id = gameId%7;

        return 7-id;
    }

    private List<User> getUsers() {

        Cursor allUsers = dbHelper.getAllUsers();

        List<User> names = new ArrayList<>();
        allUsers.moveToFirst();
        for (int i=0; i < allUsers.getCount(); i++) {

            int userId= allUsers.getInt(allUsers.getColumnIndex(DBHelper.USER_COLUMN_ID));
            String userName = allUsers.getString(allUsers.getColumnIndex(DBHelper.USER_COLUMN_NAME));
            String userGender = allUsers.getString(allUsers.getColumnIndex(DBHelper.USER_COLUMN_GENDER));
            int userAge = allUsers.getInt(allUsers.getColumnIndex(DBHelper.USER_COLUMN_AGE));

            names.add(new User(userId, userName, userGender, userAge));
            allUsers.moveToNext();
        }
        if (!allUsers.isClosed()) {
            allUsers.close();
        }

        return names;
    }
}