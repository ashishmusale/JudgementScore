package com.amusale.judgementscore.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amusale.judgementscore.DBHelper;
import com.amusale.judgementscore.R;
import com.amusale.judgementscore.model.User;

/**
 * Created by amusale on 4/4/16.
 */
public class CreateOrEditUserActivity extends AppCompatActivity implements View.OnClickListener {


    private DBHelper dbHelper ;
    EditText nameEditText;
    EditText genderEditText;
    EditText ageEditText;

    LinearLayout buttonLayout;
    Button editButton, deleteButton;

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        setContentView(R.layout.activity_create_or_edit_user_activity);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        genderEditText = (EditText) findViewById(R.id.editTextGender);
        ageEditText = (EditText) findViewById(R.id.editTextAge);

        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        if(userID > 0) {
            setTitle(getResources().getString(R.string.edit));
            buttonLayout.setVisibility(View.VISIBLE);

            User user = dbHelper.getUser(userID);

            nameEditText.setText(user.getUserName());
            nameEditText.setFocusable(false);
            nameEditText.setClickable(false);

            genderEditText.setText(user.getUserGender());
            genderEditText.setFocusable(false);
            genderEditText.setClickable(false);

            ageEditText.setText((user.getUserAge() + ""));
            ageEditText.setFocusable(false);
            ageEditText.setClickable(false);
        } else {
            setTitle(getResources().getString(R.string.add));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editButton:
                buttonLayout.setVisibility(View.GONE);
                nameEditText.setEnabled(true);
                nameEditText.setFocusableInTouchMode(true);
                nameEditText.setClickable(true);

                genderEditText.setEnabled(true);
                genderEditText.setFocusableInTouchMode(true);
                genderEditText.setClickable(true);

                ageEditText.setEnabled(true);
                ageEditText.setFocusableInTouchMode(true);
                ageEditText.setClickable(true);
                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deletePerson)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteUser(userID);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Person?");
                d.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (userID <= 0) {
            final MenuItem menuItem = menu.add(Menu.NONE, R.id.menuSaveUser, Menu.NONE, R.string.save);
            menuItem.setIcon(R.mipmap.check);
            MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menuSaveUser) {
            persistPerson();
        }

        return super.onOptionsItemSelected(item);
    }

    public void persistPerson() {
        if(userID > 0) {
            if(dbHelper.updateUser(userID, nameEditText.getText().toString(),
                    genderEditText.getText().toString(),
                    Integer.parseInt(ageEditText.getText().toString()))) {
                Toast.makeText(getApplicationContext(), "Person Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Person Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertUser(nameEditText.getText().toString(),
                    genderEditText.getText().toString(),
                    Integer.parseInt(ageEditText.getText().toString()),
                    false)) {
                Toast.makeText(getApplicationContext(), "Person Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert person", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
