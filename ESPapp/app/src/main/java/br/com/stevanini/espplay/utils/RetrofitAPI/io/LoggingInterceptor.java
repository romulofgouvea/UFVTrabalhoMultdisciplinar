package br.com.stevanini.espplay.utils.RetrofitAPI.io;


import java.io.IOException;

import br.com.stevanini.espplay.utils.RetrofitAPI.util.Logger;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Logger.d(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Logger.d(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));


        final String responseString = new String(response.body().bytes());

        Logger.d("Response: " + responseString);

        return  response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), responseString))
                .build();
    }
}