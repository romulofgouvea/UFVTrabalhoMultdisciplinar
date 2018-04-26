package br.com.stevanini.espplay.utils.RetrofitAPI.io;

import android.support.annotation.Nullable;

import java.util.List;

import br.com.stevanini.espplay.domain.PlaceAPI;
import br.com.stevanini.espplay.domain.User;
import br.com.stevanini.espplay.domain.UserAddressAPI;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitAPI {

//-------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------CATEGORIAS--------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------

    //BUSCA AS CATEGORIAS
    @FormUrlEncoded
    @POST("_ajax/Users.ajax.php")
    Call<Boolean> getEmailAPI(
            @Field("callback") String callback,
            @Field("callback_action") String callback_action,
            @Field("user_email") String email
    );

    @FormUrlEncoded
    @POST("_ajax/Users.ajax.php")
    Call<Boolean> getVerifyUserAPI(
            @Field("callback") String callback,
            @Field("callback_action") String callback_action,
            @Field("user_email") String email,
            @Field("user_password") String password
    );

    @FormUrlEncoded
    @POST("_ajax/Users.ajax.php")
    Call<User> getUserAPI(
            @Field("callback") String callback,
            @Field("callback_action") String callback_action,
            @Field("user_email") String email
    );

    @FormUrlEncoded
    @POST("_ajax/Users.ajax.php")
    Call<UserAddressAPI> getAddressAPI(
            @Field("callback") String callback,
            @Field("callback_action") String callback_action,
            @Field("user_id") long id
    );

    @FormUrlEncoded
    @POST("_ajax/Users.ajax.php")
    Call<Double> setUserCreateAPI(
            @Field("callback") String callback,
            @Field("callback_action") String callback_action,
            @Field("user_id_fb") long user_id_fb,
            @Field("user_cover") String user_cover,
            @Field("user_name") String user_name,
            @Field("user_lastname") String user_lastname,
            @Field("user_email") String user_email,
            @Field("user_password") String user_password,
            @Field("user_genre") String user_genre,
            @Field("user_datebirth") String user_datebirth
    );

    @FormUrlEncoded
    @POST("_ajax/Users.ajax.php")
    Call<Long> setUserAddressCreateAPI(
            @Field("callback") String callback,
            @Field("callback_action") String callback_action,
            @Field("user_id") long user_id,
            @Field("addr_id") long addr_id,
            @Field("addr_street") String addr_street,
            @Field("addr_number") String addr_number,
            @Field("addr_city") String addr_city,
            @Field("addr_state") String addr_state
    );

    //UPLOAD IMAGE
    @Multipart
    @POST("_ajax/Places.ajax.php")
    Call<Long> sendPlace(
            @Part("callback") RequestBody callback,
            @Part("callback_action") RequestBody callback_action,
            @Part("place_user_id") RequestBody place_user_id,
            @Part("place_addr_id") RequestBody place_addr_id,
            @Part("place_id") RequestBody user_id,
            @Part MultipartBody.Part place_thumb,
            @Part("place_name") RequestBody place_name,
            @Part("place_description") RequestBody place_description,
            @Part("place_latitude") RequestBody place_latitude,
            @Part("place_longitude") RequestBody place_longitude
    );

    @FormUrlEncoded
    @POST("_api/places.php")
    Call<List<PlaceAPI>> getListPlaces(
            @Field("key") String key,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("_api/places.php")
    Call<List<PlaceAPI>> getSearchPlaces(
            @Field("key") String keyAPI,
            @Field("token") String tokenAPI,
            @Nullable @Field("query_search") String queryAPI,
            @Nullable @Field("limit") String limitAPI,
            @Nullable @Field("offset") String offsetAPI,
            @Nullable @Field("order") String orderAPI,
            @Nullable @Field("by") String byAPI
    );
}