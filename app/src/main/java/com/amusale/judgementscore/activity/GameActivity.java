package com.amusale.judgementscore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amusale.judgementscore.DBHelper;
import com.amusale.judgementscore.R;
import com.amusale.judgementscore.ScoreAdapter;
import com.amusale.judgementscore.activity.MainActivity;
import com.amusale.judgementscore.model.Game;
import com.amusale.judgementscore.model.Score;

import java.util.Set;

public class GameActivity extends AppCompatActivity {


    public static final int STATUS_WON = 0;
    public static final int STATUS_LOST = 1;

    private DBHelper dbHelper ;
    private ListView listView;
    private int gameId;
    private String gameAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        dbHelper = new DBHelper(this);

        gameId = getIntent().getIntExtra(MainActivity.SCORE_KEY_CONTACT_ID, 0);
        gameAction = getIntent().getStringExtra(MainActivity.SCORE_ACTION);


        Game gameInfo = new Game();
        gameInfo.setUser(dbHelper.getAllUsers());
        gameInfo.setGameAction(gameAction);
        gameInfo.setGameId(gameId);

        Score score = new Score();
        score.setMaxNumOfCards(getNumOfHands());
        score.setWildcard(getWildCard());
        gameInfo.setScore(score);

        final ScoreAdapter scoreAdapter = new ScoreAdapter(this, R.layout.game_input, dbHelper.getAllUsers());
        scoreAdapter.setGameInfo(gameInfo);
        listView = (ListView)findViewById(R.id.listUsers);
        listView.setAdapter(scoreAdapter);


        Button createGameButton = (Button) findViewById(R.id.create_game);
        Button finishGameButton = (Button) findViewById(R.id.finish_game);

        if (gameAction.equals(MainActivity.SCORE_ACTION_EDIT)) {
            finishGameButton.setVisibility(View.VISIBLE);
            createGameButton.setVisibility(View.GONE);

            score = dbHelper.getScore(gameId);
        } else {
            finishGameButton.setVisibility(View.GONE);
            createGameButton.setVisibility(View.VISIBLE);
        }

        TextView wildcardHeader = (TextView) findViewById(R.id.currentWildcard);
        wildcardHeader.setText(score.getWildcard());


        if (null != createGameButton) {
            createGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Set<String> keys = scoreAdapter.getTextValues().keySet();
                    String points = "";
                    for (String key : keys) {

                        Log.d("KEY: ", key);
                        String[] keySplit = key.split(":");
                        if (keySplit.length == 2) {
                            String uid = keySplit[1];
                            String value = scoreAdapter.getTextValues().get(key);
                            int status = STATUS_LOST;
                            points += uid + ":" + value + ":" + status;
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
            Log.e("BUTTON", "createGameButton SAVE GAME was not found on the page");
        }



        finishGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> keys = scoreAdapter.getTextValues().keySet();
                String points = "";
                for (String key : keys) {

                    String[] keySplit = key.split(":");
                    if (keySplit.length == 2) {
                        String uid = keySplit[1];
                        String value = scoreAdapter.getTextValues().get(key);
                        int status = STATUS_LOST;
                        points += uid + ":" + value + ":" + status;
                        points += ";";
                    }
                }
                dbHelper.updateScore(gameId, "Completed", points);

                Toast.makeText(getApplicationContext(), "Game finished, scores will be updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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
}
