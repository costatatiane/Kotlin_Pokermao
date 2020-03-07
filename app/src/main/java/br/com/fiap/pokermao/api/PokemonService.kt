package br.com.fiap.pokermao.api

import br.com.fiap.pokermao.model.HealthResponse
import retrofit2.Call
import retrofit2.http.GET

interface PokemonService {
    @GET("/api/pokemon/health")
    fun checkHealth(): Call<HealthResponse>
}