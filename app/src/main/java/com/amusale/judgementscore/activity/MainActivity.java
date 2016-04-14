package com.amusale.judgementscore.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.amusale.judgementscore.activity.action.GameSettingsAction;
import com.amusale.judgementscore.activity.action.UsersAction;
import com.amusale.judgementscore.DBHelper;
import com.amusale.judgementscore.R;
import com.amusale.judgementscore.model.Score;
import com.amusale.judgementscore.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";
    public final static String SCORE_KEY_CONTACT_ID = "SCORE_KEY_CONTACT_ID";
    public final static String SCORE_ACTION = "SCORE_ACTION";
    public final static String SCORE_ACTION_NEW = "SCORE_ACTION_NEW";
    public final static String SCORE_ACTION_EDIT = "SCORE_ACTION_EDIT";
    private static final int HEIGHT = 100;

    RelativeLayout relativeLayout;
    private DBHelper dbHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        createGridView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (null != fab) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                    Intent intent=new Intent(MainActivity.this, CreateOrEditUserActivity.class);
                    intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(MainActivity.this, GameSettingsAction.class);
            startActivity(intent);
        } else if (id == R.id.action_users) {
            Intent intent=new Intent(MainActivity.this, UsersAction.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    private void createGridView() {

        List<User> users = dbHelper.getAllUsers();

        ScrollView sv = new ScrollView(this);
        TableLayout tableLayout = createTableLayout(users);
        HorizontalScrollView hsv = new HorizontalScrollView(this);

        hsv.addView(tableLayout);
        sv.addView(hsv);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl);

        if (null != relativeLayout) {
            relativeLayout.addView(sv);
        }

    }

    private TableLayout createTableLayout(List<User> users) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;
        List<Score> scores = dbHelper.getAllScores();

        for (int i = 0; i < (scores.size() +1) ; i++) {
            // 3) create tableRow
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.WHITE);
            tableRow.setMinimumHeight(HEIGHT);
            tableRow.setMinimumWidth(HEIGHT);
            tableRow.setGravity(Gravity.CENTER_VERTICAL);

            Map<String, String> scoreMap = new HashMap<>();
            Map<String, String> wonStatusMap = new HashMap<>();
            Score score = null;
            if (i > 0) {
                score = scores.get(i-1);
                String[] points = score.getPoints().split(";");

                for (String s : points) {

                    String[] userPoints = s.split(":");
                    if (userPoints.length > 1) {
                        scoreMap.put(userPoints[0], userPoints[1]);
                        wonStatusMap.put(userPoints[0], userPoints[2]);
                    }
                }

            }


            for (int j= 0; j < (users.size() + 1); j++) {

                TextView textView = generateTextView(this);
                User user = null;
                if (j > 0)
                 user = users.get(j-1);

                if (i == 0 && j == 0) {
                    tableRow.addView(fillZeroZeroCell());
                } else if(i == 0) {
                    // Column Headers
                    textView.setText(user.getUserName());
                    tableRow.addView(textView, tableRowParams);
                } else if(j == 0) {
                    // Row Headers
                    tableRow.addView(fillRowHeaders(score));
                } else {
                    // Score cells
                    tableRow.addView(fillScoreCells(scoreMap.get(String.format("%d", user.getUserId())),
                            wonStatusMap.get(String.format("%d", user.getUserId())),
                            score.getStatus(),
                            textView), tableRowParams);
                }
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow, tableLayoutParams);
        }

        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(Color.BLACK);
        tableRow.setMinimumHeight(HEIGHT);
        tableRow.setMinimumWidth(HEIGHT);
        tableRow.setGravity(Gravity.CENTER_VERTICAL);
        TextView titleView = generateTextView(this);
        titleView.setHeight(HEIGHT);
        titleView.setWidth(HEIGHT);
        titleView.setBackgroundColor(Color.WHITE);
        titleView.setGravity(Gravity.CENTER);
        titleView.setText("Total");
        tableRow.addView(titleView);

        for (int j= 0; j < users.size(); j++) {
            User user = users.get(j);
            int sum = 0;
            for (Score score: scores) {
                String[] points = score.getPoints().split(";");

                for (String s : points) {

                    String[] userPoints = s.split(":");
                    if (userPoints.length > 2 &&
                            Integer.parseInt(userPoints[0]) == user.getUserId()) {
                        int numOfHands = Integer.parseInt(userPoints[1]);
                        int wonStatus = Integer.parseInt(userPoints[2]);

                        if (wonStatus == GameActivity.STATUS_WON) {
                            if (numOfHands != 0) {
                                sum += (numOfHands * 10);
                            } else {
                                sum += 10;
                            }
                        }
                    }
                }
            }

            TextView textView = new TextView(this);
            textView.setHeight(HEIGHT);
            textView.setWidth(HEIGHT);
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setText(String.format("%d", sum));
            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);


        return tableLayout;
    }

    private int getResource(String value) {
        switch (value) {
            case "diamond": return R.mipmap.diamond2;
            case "spade": return R.mipmap.chip;
            case "clubs": return R.mipmap.clubs;
            case "hearts": return R.mipmap.hearts;
        }
        return R.mipmap.hearts;
    }

    private boolean gameInProgress(Score score) {
        return score.getStatus().equals("In Progress");
    }
    private boolean gameInProgress() {
        List<Score> scores = dbHelper.getAllScores();

        for (Score score: scores) {
            if (score.getStatus().equals("In Progress")) {
                return true;
            }
        }
        return false;
    }


    private TextView fillScoreCells(String point, String wonStatus, String scoreStatus, TextView textView) {

        if (null == point && null == wonStatus) {
            point = "0";
        } else {
            if (Integer.parseInt(wonStatus) == GameActivity.STATUS_WON) {
                int pointInt = Integer.parseInt(point);
                if (pointInt == 0 ) {
                    pointInt = 1; // if user won with 0 hands, they get 10 points
                }
                point = String.format("%d", pointInt * 10);
            } else {
                if (!scoreStatus.equals("In Progress")) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        }
        textView.setText(point);
        return textView;
    }

    private ImageView fillRowHeaders(Score score) {
        ImageView image = new ImageView(MainActivity.this);
        image.setImageResource(getResource(score.getWildcard()));
        image.setAdjustViewBounds(true);
        image.setMaxHeight(128);
        image.setMaxWidth(128);
        image.setBackgroundColor(Color.WHITE);
        image.setTag(R.id.gameIdResource, score.getId());

        final boolean inProgress = gameInProgress(score);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inProgress) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra(SCORE_KEY_CONTACT_ID, (int) v.getTag(R.id.gameIdResource));
                    intent.putExtra(SCORE_ACTION, SCORE_ACTION_EDIT);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Game already completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return image;

    }

    private ImageView fillZeroZeroCell() {
        final ImageView image = new ImageView(MainActivity.this);

        image.setImageResource(R.mipmap.add_game);
        image.setAdjustViewBounds(true);
        image.setMaxHeight(64);
        image.setMaxWidth(64);
        image.setBackgroundColor(Color.WHITE);
        image.setClickable(true);

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        if (gameInProgress()) {
                            Toast.makeText(getApplicationContext(),
                                    "Previous game already in progress", Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(MainActivity.this, GameActivity.class);
                            intent.putExtra(SCORE_KEY_CONTACT_ID, dbHelper.getAllScores().size());
                            intent.putExtra(SCORE_ACTION, SCORE_ACTION_NEW);
                            startActivity(intent);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: {
//                                    image.setImageBitmap(res.getDrawable(R.drawable.img_up));
                        break;
                    }
                }
                return true;
            }
        });
        return image;
    }

    private TextView generateTextView(MainActivity mainActivity) {
        TextView textView = new TextView(mainActivity);
        textView.setHeight(HEIGHT);
        textView.setWidth(HEIGHT);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
