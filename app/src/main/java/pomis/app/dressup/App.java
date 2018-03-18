package pomis.app.dressup;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pomis.app.dressup.data.Repository;
import pomis.app.dressup.models.Offer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by romanismagilov on 17.03.18.
 */

public class App extends Application {
    static private Repository api;
    static final private String BASE_URL = "http://167.99.36.68:5000/";
    static public List<Offer> offers;

    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofint();
    }

    public static Repository getApi() {
        if (api == null) {
            initRetrofint();
        } return api;
    }

    private static void initRetrofint() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.readTimeout(260, TimeUnit.SECONDS);
        httpClient.connectTimeout(260, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .build();

        api = retrofit.create(Repository.class);
    }
}
