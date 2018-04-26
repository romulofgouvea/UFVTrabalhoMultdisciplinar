package br.com.stevanini.espplay.utils.RetrofitAPI.io;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import br.com.stevanini.espplay.domain.User;
import br.com.stevanini.espplay.utils.ItemDes;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.stevanini.espplay.helpers.Constantes.BASE_URL;

public class RetrofitHelper {

    private Retrofit retrofit;

    private Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    private Retrofit getRetrofitInstanceUser() {
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new ItemDes()).create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public RetrofitAPI getItemApi() {
        return getRetrofitInstance().create(RetrofitAPI.class);
    }
    public RetrofitAPI getUserApi() { return getRetrofitInstance().create(RetrofitAPI.class); }

}

