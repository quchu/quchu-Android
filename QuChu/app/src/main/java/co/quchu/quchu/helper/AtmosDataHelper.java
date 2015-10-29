package co.quchu.quchu.helper;

import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * AtmosDataHelper
 * User: Chenhs
 * Date: 2015-10-29
 * 氛围--网络数据获取工具
 */
public class AtmosDataHelper {

    public static void GetAtmosData(){
        AtmosDataInterface gg = NetService.getInstance().getAysn(AtmosDataInterface.class);
        Call<String> ResponseCall = gg.getData();
        ResponseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }



    interface AtmosDataInterface {
        @Headers("Content-Type: charset=UTF-8")
        @GET(NetApi.GetCityList)
        Call<String> getData();
    }
}
