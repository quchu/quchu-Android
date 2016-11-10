package co.quchu.quchu.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import co.quchu.quchu.model.AIConversationModel;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.DatabaseHelper;
import co.quchu.quchu.utils.SPUtils;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecommendPresenter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 逻辑类
 */
public class AIConversationPresenter {

  public static void startConversation(Context context, boolean startor,
      final CommonListener<AIConversationModel> listener) {

    Map<String, String> map = new HashMap<>();
    map.put("type", startor ? String.valueOf("01") : "03");

    map.put("longitude",String.valueOf(SPUtils.getLongitude()));
    map.put("latitude",String.valueOf(SPUtils.getLatitude()));
    map.put("cityId",String.valueOf(SPUtils.getCityId()));
    GsonRequest<AIConversationModel> request =
        new GsonRequest<>(NetApi.getAIQuestion, AIConversationModel.class, map,
            new ResponseListener<AIConversationModel>() {
              @Override public void onErrorResponse(@Nullable VolleyError error) {
                if (listener != null) {
                  listener.errorListener(error, "", "");
                }
              }

              @Override
              public void onResponse(AIConversationModel response, boolean result, String errorCode,
                  @Nullable String msg) {
                if (listener != null) {
                  listener.successListener(response);
                }
              }
            });
    request.start(context);
  }

  public static void getNext(Context context, String question, String flash,
      final CommonListener<AIConversationModel> listener) {

    Map<String, String> map = new HashMap<>();
    map.put("question", question);
    map.put("flash", flash);

    map.put("longitude",String.valueOf(SPUtils.getLongitude()));
    map.put("latitude",String.valueOf(SPUtils.getLatitude()));
    map.put("cityId",String.valueOf(SPUtils.getCityId()));

    GsonRequest<AIConversationModel> request =
        new GsonRequest<>(NetApi.getAIAnswer, AIConversationModel.class, map,
            new ResponseListener<AIConversationModel>() {
              @Override public void onErrorResponse(@Nullable VolleyError error) {
                if (listener != null) {
                  listener.errorListener(error, "", "");
                }
              }

              @Override
              public void onResponse(AIConversationModel response, boolean result, String errorCode,
                  @Nullable String msg) {
                if (listener != null) {
                  listener.successListener(response);
                }
              }
            });
    request.start(context);
  }

  /**
   * 插入一条对话
   */
  public static long insertMessage(Context context, AIConversationModel model) {

    ContentValues contentValues = new ContentValues();
    switch (model.getDataType()) {
      case QUESTION:
        contentValues.put("dataType", 1);
        contentValues.put("chatContent", model.getAnswer());
        break;
      case ANSWER:
        contentValues.put("dataType", 2);
        contentValues.put("chatContent", model.getAnswer());
        break;
      case OPTION:
        contentValues.put("dataType", 3);
        contentValues.put("options", new Gson().toJson(model.getAnswerPramms()));
        break;
      case GALLERY:
        contentValues.put("dataType", 4);
        contentValues.put("placeList", new Gson().toJson(model.getPlaceList()));
        break;
      case NO_NETWORK:
        return -1;
    }

    contentValues.put("timestamp", System.currentTimeMillis());
    long id = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .insert(DatabaseHelper.TABLE_NAME_AI_CONVERSATION, null, contentValues);
    DatabaseHelper.closeIfOpend(context);
    return id;
  }

  /**
   * 获得所有对话
   */
  public static List<AIConversationModel> getMessages(Context context) {
    //dataType,chatContent,placeList,options,timeStamp
    List<AIConversationModel> dataSet = new ArrayList<>();
    Cursor c = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .query(DatabaseHelper.TABLE_NAME_AI_CONVERSATION,
            new String[] { "dataType", "chatContent", "placeList", "timeStamp" }, null,
            null, null, null, "timestamp asc");

    while (c.moveToNext()) {
      AIConversationModel acm = new AIConversationModel();

      switch (c.getInt(0)) {
        case 1:
          acm.setDataType(AIConversationModel.EnumDataType.QUESTION);
          acm.setAnswer(c.getString(1));
          break;
        case 2:
          acm.setDataType(AIConversationModel.EnumDataType.ANSWER);
          acm.setAnswer(c.getString(1));
          break;
        case 3:
          //acm.setDataType(AIConversationModel.EnumDataType.OPTION);

          //List<String> answers =
          //    new Gson().fromJson(c.getString(3).toString(), new TypeToken<List<String>>() {
          //    }.getType());
          //acm.setAnswerPramms(answers);
          break;
        case 4:
          acm.setDataType(AIConversationModel.EnumDataType.GALLERY);
          List<DetailModel> places =
              new Gson().fromJson(c.getString(2).toString(), new TypeToken<List<DetailModel>>() {
              }.getType());
          acm.setPlaceList(places);
          break;
      }
      dataSet.add(acm);
    }
    c.close();
    DatabaseHelper.closeIfOpend(context);
    return dataSet;
  }

  /**
   * 删除最后一个Option如果存在的话
   */
  public static boolean delOptionMessages(Context context) {

    Cursor c = DatabaseHelper.getInstance(context).getReadableDatabase().rawQuery("select id,dataType from "+DatabaseHelper.TABLE_NAME_AI_CONVERSATION+" order by timestamp desc limit 1",null);

    if (c.moveToNext()) {
      int id = c.getInt(0);
      int type = c.getInt(1);
      c.close();

      if (type  == 3){
        return DatabaseHelper.getInstance(context)
            .getReadableDatabase()
            .delete(DatabaseHelper.TABLE_NAME_AI_CONVERSATION, "id=?",
                new String[] { String.valueOf(id) }) > 0;
      }else{
        return false;
      }

    }
    return false;
  }

  /**
   * 删除所有对话
   */
  public static boolean delMessages(Context context) {
    boolean delSuccess = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .delete(DatabaseHelper.TABLE_NAME_AI_CONVERSATION, null, null) > 0;
    DatabaseHelper.closeIfOpend(context);
    return delSuccess;
  }

  /**
   * 删除小于日期前的
   */
  public static boolean delMessagesBefore(Context context,long date) {
    boolean delSuccess = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .delete(DatabaseHelper.TABLE_NAME_AI_CONVERSATION, null, null) > 0;
    DatabaseHelper.getInstance(context).getReadableDatabase().delete(DatabaseHelper.TABLE_NAME_AI_CONVERSATION,"timeStamp <"+date,null);
    DatabaseHelper.closeIfOpend(context);
    return delSuccess;
  }

  /**
   * 统计对话数据量
   */
  public static int getMessageSize(Context context) {
    int count = 0;
    Cursor c = DatabaseHelper.getInstance(context)
        .getReadableDatabase()
        .query(DatabaseHelper.TABLE_NAME_AI_CONVERSATION, null, null, null, null, null, null);
    if (null != c) {
      count = c.getCount();
    }
    c.close();
    DatabaseHelper.closeIfOpend(context);
    return count;
  }
}
