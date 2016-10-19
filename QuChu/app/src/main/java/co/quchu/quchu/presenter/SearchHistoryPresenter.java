package co.quchu.quchu.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import co.quchu.quchu.model.SearchKeywordModel;
import co.quchu.quchu.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/10/19.
 */

public class SearchHistoryPresenter {

  /**
   * 插入一条搜索键值
   * @param context
   * @param keyword
   * @return
   */
  public static long insertKeyword(Context context, String keyword) {
    deleteIfExisted(context,keyword);
    ContentValues contentValues = new ContentValues();
    contentValues.put("keyword", keyword);
    contentValues.put("timestamp", System.currentTimeMillis());
    long id = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .insert(DatabaseHelper.TABLE_NAME_USER_SEARCH_HISTORY, null, contentValues);
    DatabaseHelper.closeIfOpend(context);
    return id;
  }

  /**
   * 获得所有用户行为
   */
  public static List<SearchKeywordModel> getHistoryKeywords(Context context) {
    List<SearchKeywordModel> dataSet = new ArrayList<>();
    Cursor c = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .query(DatabaseHelper.TABLE_NAME_USER_SEARCH_HISTORY,
            new String[] { "id", "keyword", "timestamp"}, null, null, null,
            null, "timestamp desc");

    while (c.moveToNext()) {
      SearchKeywordModel ubm = new SearchKeywordModel();
      ubm.setId(c.getInt(0));
      ubm.setKeyword(c.getString(1));
      ubm.setTimestamp(c.getLong(2));
      dataSet.add(ubm);
    }
    c.close();
    DatabaseHelper.closeIfOpend(context);
    return dataSet;
  }


  /**
   * 删除如果存在
   * @param context
   * @param keyword
   * @return
   */
  public static boolean deleteIfExisted(Context context,String keyword){
    boolean delSuccess = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .delete(DatabaseHelper.TABLE_NAME_USER_SEARCH_HISTORY,"keyword = '"+keyword+"'",null)>0;
    DatabaseHelper.closeIfOpend(context);
    return delSuccess;
  }

}
