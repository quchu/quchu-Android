package co.quchu.quchu.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Nico on 16/5/26.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "db_quchu_events";
    private static final String DATABASE_TABLE = "tb_user_behavior";
    private static final String SQL_CREATE_TABLE_USER_BEHAVIOR =
            "create table tb_user_behavior (id integer primary key autoincrement,pageName text not null,pageArguments text,pageValues text,timestamp long not null,status integer not null);";

    private static final String SQL_CREATE_TABLE_CITY_LIST =
            "create table tb_city_list (_id integer primary key autoincrement,cityId integer not null, cityName text not null);";
    private static final int DATABASE_VERSION = 1;

    public static synchronized DatabaseHelper getInstance(Context context) {


        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER_BEHAVIOR);
        db.execSQL(SQL_CREATE_TABLE_CITY_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


