package com.salwa.salwa.homepage.ui.cart.location.api;

import com.salwa.salwa.homepage.ui.cart.location.response.KecamatanResponse;
import com.salwa.salwa.homepage.ui.cart.location.response.KelurahanResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetroService {
    public static final String KECAMATAN = "kecamatan?id_kota=3214";

    @GET(KECAMATAN)
    Call<KecamatanResponse> ardRetrieveKecamatan();

    @GET("kelurahan")
    Call<KelurahanResponse> ardRetrieveKelurahan(@Query("id_kecamatan") int id);

}
