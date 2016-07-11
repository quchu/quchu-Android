package co.quchu.quchu.presenter;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.model.FootprintModel;
import co.quchu.quchu.model.UserBehaviorModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.DatabaseHelper;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by Nico on 16/6/2.
 */
public class UserBehaviorPresentor {


    /**
     * 插入一条用户行为
     * @param context
     * @param pageName 页面名称
     * @param behavior 行为名称
     * @param arguments 页面参数
     * @param timestamp 时间戳
     * @return
     */
    public static long insertBehavior(Context context, int pageName, String behavior, String arguments, long timestamp){
        ContentValues contentValues = new ContentValues();
        contentValues.put("pageId",pageName);
        contentValues.put("behavior",behavior);
        contentValues.put("arguments",arguments);
        contentValues.put("timestamp",timestamp);
        long id = DatabaseHelper.getInstance(context).getReadableDatabase().insert(DatabaseHelper.TABLE_NAME_USER_BEHAVIOR,null,contentValues);
        closeDBIfOpen(context);
        return id;
    }


    /**
     * 获得所有用户行为
     * @param context
     * @return
     */
    public static List<UserBehaviorModel> getBehaviors(Context context){
        List<UserBehaviorModel> dataSet = new ArrayList<>();
        Cursor c = DatabaseHelper
                .getInstance(context)
                .getReadableDatabase()
                .query(DatabaseHelper.TABLE_NAME_USER_BEHAVIOR,new String[]{"id","pageId","behavior","arguments","timestamp"},null,null,null,null,"timestamp desc");


        while (c.moveToNext()){
            UserBehaviorModel ubm = new UserBehaviorModel();
//
//            ubm.id = c.getInt(0);
            ubm.pageId = c.getInt(1);
            ubm.userBehavior = c.getString(2);
            ubm.arguments = c.getString(3);
            ubm.timestamp = c.getLong(4);

            dataSet.add(ubm);
        }
        c.close();
        closeDBIfOpen(context);
        return dataSet;
    }

    /**
     * 删除所有用户行为
     * @param context
     * @return
     */
    public static boolean delBehaviors(Context context){
        boolean delSuccess = DatabaseHelper.getInstance(context).getReadableDatabase().delete(DatabaseHelper.TABLE_NAME_USER_BEHAVIOR,null,null)>0;
        closeDBIfOpen(context);
        return delSuccess;
    }


    /**
     * 统计数据量
     * @param context
     * @return
     */
    public static int getDataSize(Context context){
        int count = 0;
        Cursor c = DatabaseHelper.getInstance(context).getReadableDatabase().query(DatabaseHelper.TABLE_NAME_USER_BEHAVIOR,null,null,null,null,null,null);
        if (null!=c){
            count = c.getCount();
        }
        c.close();
        closeDBIfOpen(context);
        return count;
    }

    /**
     * 关闭数据库
     * @param context
     */
    private static void closeDBIfOpen(Context context){
        if (DatabaseHelper.getInstance(context).getReadableDatabase().isOpen()){
            DatabaseHelper.getInstance(context).getReadableDatabase().close();
        }
    }


    public static void postBehaviors(final Context context, List<UserBehaviorModel> data, final CommonListener pListener) {


        JSONObject jsonObject;
        JSONObject jsonObjectAll = null;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject();
            jsonArray = new JSONArray();
            for (int i = 0; i < data.size(); i++) {
                JSONObject jsonObjectChild = new JSONObject();
                jsonObjectChild.put("pageId",data.get(i).pageId);
                jsonObjectChild.put("timestamp",data.get(i).timestamp);
                if (!StringUtils.isEmpty(data.get(i).userBehavior)){
                    jsonObjectChild.put("userBehavior",data.get(i).userBehavior);
                }
                if (!StringUtils.isEmpty(data.get(i).arguments)){
                    jsonObjectChild.put("arguments",new JSONObject(data.get(i).arguments));
                }
                jsonArray.put(jsonObjectChild);
            }
            jsonObjectAll = new JSONObject();
            jsonObject.put("UserBehaviors",jsonArray);
            jsonObject.put("Device","android");
            jsonObjectAll.put("UserBehaviors",jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetService.post(context, NetApi.postUserBehavior,jsonObjectAll, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                delBehaviors(context);
                pListener.successListener(null);
            }

            @Override
            public boolean onError(String error) {
                pListener.errorListener(null,null,null);
                return false;
            }
        });
    }



}
