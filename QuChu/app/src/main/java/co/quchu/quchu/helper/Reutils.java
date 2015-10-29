package co.quchu.quchu.helper;

import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;

/**
 * Reutils
 * User: Chenhs
 * Date: 2015-10-20
 */
public class Reutils {

    public static void getUn() {
        final long tt = System.currentTimeMillis();
        getuser gg = NetService.getInstance().getAysn(getuser.class);
        Call<String> cc = gg.listRepos();
        cc.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                try {
//if (response.body().has("result"))
                    LogUtils.e("Code=" + response.code() + "   message= " + response.message() + "///" + response.body());

//                    ArticleListModel alm = new Gson().fromJson(response.body(), ArticleListModel.class);
//                    LogUtils.d((alm == null ? "true" : "false") + "/////" + (System.currentTimeMillis() - tt));
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e(e.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    interface getuser {
        @GET(NetApi.GetCityList)
        Call<String> listRepos();
    }
}
