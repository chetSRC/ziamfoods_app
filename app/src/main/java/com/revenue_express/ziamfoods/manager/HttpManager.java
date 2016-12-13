package com.revenue_express.ziamfoods.manager;
import android.content.Context;
import com.revenue_express.ziamfoods.manager.http.ApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class HttpManager {

    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null)
            instance = new HttpManager();
        return instance;
    }

    private Context mContext;
    private ApiService service;

    private HttpManager() {

        mContext = Contextor.getInstance().getContext();
//        .baseUrl("http://demo.ziamthai.com/")
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.ziamthai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);
    }

    public ApiService getService(){
        return service;
    }

}
