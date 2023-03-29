package com.example.appfinal.conexiones;

import com.example.appfinal.configuraciones.Constantes;
import com.example.appfinal.modelos.Respuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiConexiones {

    @GET("/lol/summoner/v4/summoners/by-name/{name}"+"?api_key="+ Constantes.API_KEY)
    Call<Respuesta> getJugador(@Path("name") String name);

}
