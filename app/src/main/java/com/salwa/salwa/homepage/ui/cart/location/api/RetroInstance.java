package com.salwa.salwa.homepage.ui.cart.location.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroInstance {
    private static final String BASE_URL = "https://dev.farizdotid.com/api/daerahindonesia/";


    private static Retrofit retrofit;

    public static Retrofit konekRetrofit() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
