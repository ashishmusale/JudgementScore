package com.amusale.judgementscore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amusale.judgementscore.model.Score;
import com.amusale.judgementscore.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amusale on 4/4/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteExample.db";
    private static final int DATABASE_VERSION = 2;

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_ID = "_id";
    public static final String USER_COLUMN_NAME = "name";
    public static final String USER_COLUMN_GENDER = "gender";
    public static final String USER_COLUMN_AGE = "age";

    public static final String SCORE_TABLE_NAME = "score";
    public static final String SCORE_COLUMN_ID = "_id";
    public static final String SCORE_COLUMN_WILD_CARD = "wildcard";
    public static final String SCORE_COLUMN_STATUS = "status";
    public static final String SCORE_COLUMN_NUM_OF_CARDS = "num_cards";
    public static final String SCORE_COLUMN_POINTS = "points";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + USER_TABLE_NAME +
                        "(" + USER_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        USER_COLUMN_NAME + " TEXT, " +
                        USER_COLUMN_GENDER + " TEXT, " +
                        USER_COLUMN_AGE + " INTEGER)"
        );
        db.execSQL(
                "CREATE TABLE " + SCORE_TABLE_NAME +
                        "(" + SCORE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        SCORE_COLUMN_WILD_CARD + " TEXT, " +
                        SCORE_COLUMN_STATUS + " TEXT, " +
                        SCORE_COLUMN_NUM_OF_CARDS + " INTEGER, " +
                        SCORE_COLUMN_POINTS + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //
    }

    public boolean insertUser(String name, String gender, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_GENDER, gender);
        contentValues.put(USER_COLUMN_AGE, age);

        db.insert(USER_TABLE_NAME, null, contentValues);
        return true;
    }


    public boolean updateUser(Integer id, String name, String gender, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_GENDER, gender);
        contentValues.put(USER_COLUMN_AGE, age);
        db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteUser(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_TABLE_NAME,
                USER_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " +
                USER_COLUMN_ID + "=?", new String[]{Integer.toString(id)});

        return getUserFromCursor(cursor);
    }

    public Cursor getAllUsersCursor() {
        return getAll(USER_TABLE_NAME);
    }
    public List<User> getAllUsers() {
        Cursor allUsers = getAll(USER_TABLE_NAME);

        List<User> users= new ArrayList<>();
        allUsers.moveToFirst();
        for (int i=0; i < allUsers.getCount(); i++) {
            users.add(getUserFromCursor(allUsers));
            allUsers.moveToNext();
        }
        if (!allUsers.isClosed()) {
            allUsers.close();
        }

        return users;
    }

    private User getUserFromCursor(Cursor cursor) {
        int userId = cursor.getInt(cursor.getColumnIndex(DBHelper.USER_COLUMN_ID));
        String userName = cursor.getString(cursor.getColumnIndex(DBHelper.USER_COLUMN_NAME));
        String userGender = cursor.getString(cursor.getColumnIndex(DBHelper.USER_COLUMN_GENDER));
        int userAge = cursor.getInt(cursor.getColumnIndex(DBHelper.USER_COLUMN_AGE));

        return new User(userId, userName, userGender, userAge);
    }

    // SCORE
    public boolean insertScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCORE_COLUMN_WILD_CARD, score.getWildcard());
        contentValues.put(SCORE_COLUMN_NUM_OF_CARDS, score.getMaxNumOfCards());
        contentValues.put(SCORE_COLUMN_STATUS, score.getStatus());
        contentValues.put(SCORE_COLUMN_POINTS, score.getPoints());

        db.insert(SCORE_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertScore(String wildcard, int numOfCards, String status, String points) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCORE_COLUMN_WILD_CARD, wildcard);
        contentValues.put(SCORE_COLUMN_NUM_OF_CARDS, numOfCards);
        contentValues.put(SCORE_COLUMN_STATUS, status);
        contentValues.put(SCORE_COLUMN_POINTS, points);

        db.insert(SCORE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Score getScore(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor scoreCursor =  db.rawQuery("SELECT * FROM " + SCORE_TABLE_NAME + " WHERE " +
                SCORE_COLUMN_ID + "=?", new String[]{Integer.toString(id)});

        scoreCursor.moveToFirst();

        String scoreWildcard = scoreCursor.getString(scoreCursor.getColumnIndex(DBHelper.SCORE_COLUMN_WILD_CARD));
        String scorePoints = scoreCursor.getString(scoreCursor.getColumnIndex(DBHelper.SCORE_COLUMN_POINTS));
        Score score = new Score();
        score.setId(scoreCursor.getInt(scoreCursor.getColumnIndex(DBHelper.SCORE_COLUMN_ID)));
        score.setWildcard(scoreWildcard);
        score.setPoints(scorePoints);
        score.setStatus(scoreCursor.getString(scoreCursor.getColumnIndex(DBHelper.SCORE_COLUMN_STATUS)));

        if (!scoreCursor.isClosed()) {
            scoreCursor.close();
        }
        return score;
    }

    public boolean updateScore(Integer id, String status, String points) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCORE_COLUMN_STATUS, status);
        contentValues.put(SCORE_COLUMN_POINTS, points);

        db.update(SCORE_TABLE_NAME, contentValues, SCORE_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public List<Score> getAllScores() {
        Cursor allScores = getAll(SCORE_TABLE_NAME);
        allScores.moveToFirst();

        List<Score> scores = new ArrayList<>();
        for (int i=0; i < allScores.getCount(); i++) {

            String scoreWildcard = allScores.getString(allScores.getColumnIndex(DBHelper.SCORE_COLUMN_WILD_CARD));
            String scorePoints = allScores.getString(allScores.getColumnIndex(DBHelper.SCORE_COLUMN_POINTS));
            Score score = new Score();
            score.setId(allScores.getInt(allScores.getColumnIndex(DBHelper.SCORE_COLUMN_ID)));
            score.setWildcard(scoreWildcard);
            score.setPoints(scorePoints);
            score.setStatus(allScores.getString(allScores.getColumnIndex(DBHelper.SCORE_COLUMN_STATUS)));

            scores.add(score);
            allScores.moveToNext();
        }

        if (!allScores.isClosed()) {
            allScores.close();
        }
        return scores;
    }


    private Cursor getAll(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + tableName, null );
        return res;
    }
}
