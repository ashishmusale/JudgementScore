package com.amusale.judgementscore;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amusale.judgementscore.model.User;

public class NewGame extends AppCompatActivity {

    private DBHelper dbHelper ;
    private ListView listView;
    private int gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        dbHelper = new DBHelper(this);



        gameId = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);
        final Cursor cursor = dbHelper.getAllUsers();
        String [] columns = new String[] {
                DBHelper.USER_COLUMN_ID,
                DBHelper.USER_COLUMN_NAME
        };

        int [] widgets = new int[] {
                R.id.userId,
                R.id.userName
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.game_input,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.listUsers);
        if (null != listView) {
            listView.setAdapter(cursorAdapter);
        }



        Button button = (Button) findViewById(R.id.saveGame);
        if (null != button) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String points = "";
                    for(int i=0;i<listView.getCount();i++){
                        View listViewItem = listView.getChildAt(i);
                        if (null == listViewItem) {
                            Log.i("ListViewItem", "Empty List view item");
                        }
                        EditText numOfHandsView = (EditText) listViewItem.findViewById(R.id.numOfHands);
                        TextView userIdView = (TextView) listViewItem.findViewById(R.id.userId);
                        String numOfHands = numOfHandsView.getText().toString();
                        String userId = userIdView.getText().toString();
                        points += userId + "=" + numOfHands;

                        points += ";";
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

    private User[] getUsers() {

        Cursor allUsers = dbHelper.getAllUsers();

        User[] names = new User[allUsers.getCount()];
        allUsers.moveToFirst();
        for (int i=0; i < allUsers.getCount(); i++) {
            String userName = allUsers.getString(allUsers.getColumnIndex(DBHelper.USER_COLUMN_NAME));
            String userGender = allUsers.getString(allUsers.getColumnIndex(DBHelper.USER_COLUMN_GENDER));
            int userAge = allUsers.getInt(allUsers.getColumnIndex(DBHelper.USER_COLUMN_AGE));

            Log.d("PersonName", userName);
            Log.d("PersonGender", userGender);
            Log.d("PersonAge", "" + userAge);
            names[i] = new User(userName, userGender, userAge);
            allUsers.moveToNext();
        }
        if (!allUsers.isClosed()) {
            allUsers.close();
        }

        return names;
    }
}
