package co.quchu.quchu.net;

import com.squareup.okhttp.OkHttpClient;

import co.quchu.quchu.net.tools.LoggingInterceptor;
import co.quchu.quchu.net.tools.ToStringConverterFactory;
import retrofit.Retrofit;

/**
 * NetService
 * User: Chenhs
 * Date: 2015-10-21
 */
public class NetService {
    private OkHttpClient client;
    private LoggingInterceptor logging;
    private static NetService mInstance;
    private Retrofit retrofit;
//        logging.setLevel(Level.BASIC);

    private NetService() {
        client = new OkHttpClient();
        logging = new LoggingInterceptor();
        client.interceptors().add(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(NetApi.HOST)
                .addConverterFactory(new ToStringConverterFactory())
                .client(client)
                .build();
    }

    public static NetService getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new NetService();
                }
            }
        }
        return mInstance;
    }

    public  <T> T getAysn(final Class<T> service) {
//        retrofit.create(t)
        T netService = getInstance().retrofit.create(service);
        return netService;
    }
}
