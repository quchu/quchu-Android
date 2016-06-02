package co.quchu.quchu.presenter;

import android.content.ContentValues;
import android.content.Context;

import co.quchu.quchu.utils.DatabaseHelper;

/**
 * Created by Nico on 16/6/2.
 */
public class UserBehaviorPresentor {





    public static void insertBehavior(Context context,String pageName,String pageArguments,String pageValues,long timestamp){
        ContentValues contentValues = new ContentValues();
        contentValues.put("pageName",pageName);
        contentValues.put("pageArguments",pageArguments);
        contentValues.put("pageValues",pageValues);
        contentValues.put("timestamp",timestamp);
        contentValues.put("status",false);
        DatabaseHelper.getInstance(context).getReadableDatabase().insert(DatabaseHelper.TABLE_NAME_USER_BEHAVIOR,null,contentValues);
        DatabaseHelper.getInstance(context).getReadableDatabase().close();
    }
}
