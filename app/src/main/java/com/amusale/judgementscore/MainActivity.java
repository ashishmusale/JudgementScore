package com.amusale.judgementscore;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.amusale.judgementscore.model.Score;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";
    public final static String SCORE_KEY_CONTACT_ID = "SCORE_KEY_CONTACT_ID";
    private static final int HEIGHT = 200;

    RelativeLayout relativeLayout;
    private DBHelper dbHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTextView = (TextView) findViewById(R.id.textview1);

        if (null != mTextView) {
            mTextView.setText("** Game Name here **");
        }

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
            return true;
        } else if (id == R.id.action_users) {
            Intent intent=new Intent(MainActivity.this, ActionUsers.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    private void createGridView() {

        String[] row = { "empty", "spade", "diamond", "clubs", "hearts", "spade", "diamond", "clubs", "hearts"};
        String[] column = getNames();

        int rl=row.length;
        int cl=column.length +1;

        ScrollView sv = new ScrollView(this);
        TableLayout tableLayout = createTableLayout(row, column,rl, cl);
        HorizontalScrollView hsv = new HorizontalScrollView(this);

        hsv.addView(tableLayout);
        sv.addView(hsv);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl);

        if (null != relativeLayout) {
            relativeLayout.addView(sv);
        }

    }

    private TableLayout createTableLayout(String [] rv, String [] cv,int rowCount, int columnCount) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;
        List<Score> scores = getScores();

        for (int i = 0; i < scores.size() +1 ; i++) {
            // 3) create tableRow
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.WHITE);
            tableRow.setMinimumHeight(HEIGHT);
            tableRow.setMinimumWidth(HEIGHT);
            tableRow.setGravity(Gravity.CENTER_VERTICAL);

            for (int j= 0; j < columnCount; j++) {
                // 4) create textView
                TextView textView = new TextView(this);
                textView.setHeight(HEIGHT);
                textView.setWidth(HEIGHT);
                //  textView.setText(String.valueOf(j));
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                String s1 = Integer.toString(i);
                String s2 = Integer.toString(j);
                String s3 = s1 + s2;
                int id = Integer.parseInt(s3);

                if (i ==0 && j==0){
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
                                    Intent intent=new Intent(MainActivity.this, NewGame.class);
                                    intent.putExtra(SCORE_KEY_CONTACT_ID, dbHelper.getAllScores().getCount());
                                    startActivity(intent);
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

                    tableRow.addView(image);

                } else if(i==0){

                    // Column Headers
//                    Log.d("TAAG", "set Column Headers");
                    textView.setText(cv[j - 1]);
                    tableRow.addView(textView, tableRowParams);

                } else if(j == 0){

                    // Row Headers
                    ImageView image = new ImageView(MainActivity.this);
                    Score score = scores.get(i-1);
                    image.setImageResource(getResource(score.getWildcard()));
                    image.setAdjustViewBounds(true);
                    image.setMaxHeight(128);
                    image.setMaxWidth(128);
                    image.setBackgroundColor(Color.WHITE);

                    tableRow.addView(image);

                } else {

                    Score score = scores.get(i-1);
                    String[] points = score.getPoints().split(";");

//                    for (int pointCount = 0; pointCount < points.length; pointCount++) {
//                        String individualScore = points[pointCount];
//                        String[] scoreSplit = individualScore.split("=");
//
//                        String point = scoreSplit[1];
//                    }

                    String point = "0";
                    if (j-1 < points.length) {

                        String individualScore = points[j - 1];
                        String[] scoreSplit = individualScore.split("=");
                        point = scoreSplit[1];
                    }
                    //Log.i("POINTS", point);
                    textView.setText("" + point);

                    tableRow.addView(textView, tableRowParams);
                }
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow, tableLayoutParams);
        }

        return tableLayout;
    }

    private int getResource(String value) {
        if (value.equals("diamond")) {
            return R.mipmap.diamond2;
        } else if (value.equals("spade")) {
            return R.mipmap.chip;
        } else if (value.equals("clubs")) {
            return R.mipmap.clubs;
        }

        return R.mipmap.hearts;
    }

    private List<Score> getScores() {

        Cursor allScores = dbHelper.getAllScores();
        allScores.moveToFirst();

        List<Score> scores = new ArrayList<>();
        for (int i=0; i < allScores.getCount(); i++) {

            String scoreWildcard = allScores.getString(allScores.getColumnIndex(DBHelper.SCORE_COLUMN_WILD_CARD));
            String scorePoints = allScores.getString(allScores.getColumnIndex(DBHelper.SCORE_COLUMN_POINTS));
            Score score = new Score();
            score.setWildcard(scoreWildcard);
            score.setPoints(scorePoints);

            scores.add(score);
            allScores.moveToNext();
        }

        if (!allScores.isClosed()) {
            allScores.close();
        }
        return scores;
    }


    private String[] getNames() {

        Cursor allUsers = dbHelper.getAllUsers();

        String[] names = new String[allUsers.getCount()];
        allUsers.moveToFirst();
        for (int i=0; i < allUsers.getCount(); i++) {
            String personName = allUsers.getString(allUsers.getColumnIndex(DBHelper.USER_COLUMN_NAME));
            String personGender = allUsers.getString(allUsers.getColumnIndex(DBHelper.USER_COLUMN_GENDER));
            int personAge = allUsers.getInt(allUsers.getColumnIndex(DBHelper.USER_COLUMN_AGE));

            names[i] = personName;
            allUsers.moveToNext();
        }
        if (!allUsers.isClosed()) {
            allUsers.close();
        }

        return names;
    }
}
