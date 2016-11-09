package co.quchu.quchu.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/6/2.
 */
public class SystemMessagePresentor {

  /**
   * 插入一条通知
   */
  public static long insertMessage(Context context, String title, String content, String type,
      String eventRemark, long eventTimeStamp) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("title", title);
    contentValues.put("content", content);
    contentValues.put("type", type);
    contentValues.put("eventRemark", eventRemark);
    contentValues.put("timestamp", eventTimeStamp);
    long id = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .insert(DatabaseHelper.TABLE_NAME_SYSTEM_MSG, null, contentValues);
    DatabaseHelper.closeIfOpend(context);
    return id;
  }

  /**
   * 获得所有通知
   */
  public static List<PushMessageBean> getMessages(Context context) {
    List<PushMessageBean> dataSet = new ArrayList<>();
    Cursor c = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .query(DatabaseHelper.TABLE_NAME_SYSTEM_MSG,
            new String[] { "title", "content", "type", "eventRemark", "timestamp" }, null,
            null, null, null, "timestamp desc");

    while (c.moveToNext()) {
      PushMessageBean ubm = new PushMessageBean();
      ubm.setTitle(c.getString(0));
      ubm.setContent(c.getString(1));
      ubm.setType(c.getString(2));
      ubm.setEventRemark(c.getString(3));
      ubm.setEventTimeStamp(c.getLong(4));
      dataSet.add(ubm);
    }
    c.close();
    DatabaseHelper.closeIfOpend(context);
    return dataSet;
  }

  /**
   * 删除所有通知
   */
  public static boolean delMessages(Context context) {
    boolean delSuccess = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .delete(DatabaseHelper.TABLE_NAME_SYSTEM_MSG, null, null) > 0;
    DatabaseHelper.closeIfOpend(context);
    return delSuccess;
  }

  /**
   * 统计数据量
   */
  public static int getDataSize(Context context) {
    int count = 0;
    Cursor c = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .query(DatabaseHelper.TABLE_NAME_SYSTEM_MSG, null, null, null, null, null, null);
    if (null != c) {
      count = c.getCount();
    }
    c.close();
    DatabaseHelper.closeIfOpend(context);
    return count;
  }
}
