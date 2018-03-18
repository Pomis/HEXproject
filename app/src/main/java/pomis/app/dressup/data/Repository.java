package pomis.app.dressup.data;

import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.List;

import okhttp3.MultipartBody;
import pomis.app.dressup.models.Offer;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by romanismagilov on 17.03.18.
 */

public interface Repository {
    @Multipart
    @POST("upload")
    Call<String> upload(
            @Part MultipartBody.Part file
    );

    @GET("items")
    Call<List<Offer>> items(
            @Query("set1") String set1,
            @Query("set2") String set2,
            @Query("set3") String set3,
            @Query("type") String type
    );

    @GET("http://159.65.195.91:5000/get_style")
    Call<List<Offer>> getStyle(
            @Query("type") String type,
            @Query("img") String img
    );

}
