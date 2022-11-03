package com.example.passwordgen.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.passwordgen.Model.PasswordGenModel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    String currentDate = sdf.format(new Date());
    private static final int VERSION = 1;
    private static final String NAME = "passwordGenListDatabase";
    private static final String PASSWORD_TABLE = "password";
    private static final String WEBSITE = "website";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String TIMESTAMP = "timestamp";
    private static final String CREATE_PASSWORD_TABLE = "CREATE TABLE " + PASSWORD_TABLE + "(" + WEBSITE + " TEXT PRIMARY KEY, "
            + USER + " TEXT, " + PASSWORD + " TEXT, " + TIMESTAMP + " DATETIME DEFAULT CURRENT_DATE)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PASSWORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + PASSWORD_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertPassword(PasswordGenModel password){
        ContentValues cv = new ContentValues();
        cv.put(WEBSITE, password.getWebsite());
        cv.put(USER, password.getUser());
        cv.put(PASSWORD, password.getPassword());
        cv.put(TIMESTAMP, currentDate);
        db.insert(PASSWORD_TABLE, null, cv);
    }

    public List<PasswordGenModel> getAllPasswords(){
        List<PasswordGenModel> passwordList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(PASSWORD_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        PasswordGenModel password = new PasswordGenModel();
                        password.setWebsite(cur.getString(cur.getColumnIndex(WEBSITE)));
                        password.setUser(cur.getString(cur.getColumnIndex(USER)));
                        password.setPassword(cur.getString(cur.getColumnIndex(PASSWORD)));
                        password.setTimestamp(cur.getString(cur.getColumnIndex(TIMESTAMP)));
                        passwordList.add(password);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return passwordList;
    }

    public void updatePassword(String website, String password) {
        ContentValues cv = new ContentValues();
        cv.put(PASSWORD, password);
        cv.put(TIMESTAMP, currentDate);
        db.update(PASSWORD_TABLE, cv, WEBSITE + "= ?", new String[] {website});
    }

    public void deletePassword(String website){
        db.delete(PASSWORD_TABLE, WEBSITE + "= ?", new String[] {website});
    }
}
