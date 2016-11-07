package co.quchu.quchu.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nico on 16/5/26.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

  private static DatabaseHelper sInstance;

  public static final String DATABASE_NAME = "db_quchu_events";

  public static final String TABLE_NAME_USER_BEHAVIOR = "tb_user_behavior";
  private static final String SQL_CREATE_TABLE_USER_BEHAVIOR = "create table "
      + TABLE_NAME_USER_BEHAVIOR
      + " (id integer primary key autoincrement,pageId integer not null,behavior text not null,arguments text,timestamp long not null);";

  public static final String TABLE_NAME_USER_SEARCH_HISTORY = "tb_user_search_history";
  public static final String SQL_CREATE_TABLE_USER_SEARCH_HISTORY = "create table "
      + TABLE_NAME_USER_SEARCH_HISTORY
      + "(id integer primary key autoincrement,keyword text not null,timestamp long not null);";


  public static final String TABLE_NAME_SYSTEM_MSG = "tb_user_system_msg";
  public static final String SQL_CREATE_TABLE_SYSTEM_MSG = "create table "
      + TABLE_NAME_SYSTEM_MSG
      + "(id integer primary key autoincrement,title text,content text,type text,eventRemark text, timestamp long);";

  public static final String TABLE_NAME_AI_CONVERSATION = "tb_ai_conversation";
  public static final String SQL_CREATE_USER_AI_CONVERSATION = "create table "
      + TABLE_NAME_AI_CONVERSATION
      + "(id integer primary key autoincrement,dataType integer not null,chatContent text,placeList text,options text,timeStamp long)";

  private static final int DATABASE_VERSION = 2;

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

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_TABLE_USER_BEHAVIOR);
    db.execSQL(SQL_CREATE_TABLE_USER_SEARCH_HISTORY);
    db.execSQL(SQL_CREATE_TABLE_SYSTEM_MSG);
    db.execSQL(SQL_CREATE_USER_AI_CONVERSATION);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if (oldVersion == 1) {
      db.execSQL(SQL_CREATE_TABLE_USER_SEARCH_HISTORY);
      db.execSQL(SQL_CREATE_TABLE_SYSTEM_MSG);
      db.execSQL(SQL_CREATE_USER_AI_CONVERSATION);
    }
  }

  public static void closeIfOpend(Context context) {
    if (getInstance(context).getReadableDatabase().isOpen()) {
      DatabaseHelper.getInstance(context).getReadableDatabase().close();
    }
  }
}


