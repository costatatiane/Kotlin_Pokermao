package br.com.fiap.pokermao.repository

import br.com.fiap.pokermao.api.PokemonService
import br.com.fiap.pokermao.model.HealthResponse
import br.com.fiap.pokermao.model.Pokemon
import br.com.fiap.pokermao.model.PokemonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepositoryImpl(var pokemonService: PokemonService) :
    PokemonRepository {
    override fun checkHealth(onComplete: () -> Unit, onError: (Throwable?) -> Unit) {
        pokemonService.checkHealth()
            .enqueue(object : Callback<HealthResponse> {
                override fun onFailure(call: Call<HealthResponse>, t: Throwable) {
                    onError(t)
                }
                override fun onResponse(call: Call<HealthResponse>, response:
                Response<HealthResponse>
                ) {
                    onComplete()
                }
            })
    }

    override fun getPokemons(
        size: Int,
        sort: String,
        onComplete: (List<Pokemon>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        pokemonService.getPokemons(size, sort)
            .enqueue(object : Callback<PokemonResponse> {
                override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                    onError(t)
                }
                override fun onResponse(call: Call<PokemonResponse>, response:
                Response<PokemonResponse>) {
                    if (response.isSuccessful) {
                        onComplete(response.body()?.content ?: listOf())
                    } else {
                        onError(Throwable("Não foi possível carregar os Pokémons"))
                    }
                }
            })
    }

    override fun updatePokemon(pokemon: Pokemon, onComplete: (Pokemon?) -> Unit,
                               onError: (Throwable) -> Unit) {
        pokemonService
            .updatePokemon(pokemon)
            .enqueue(object : Callback<Pokemon>{
                override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                    onError(t)
                }
                override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onError(Throwable("Não foi possível realizar a requisição"))
                    }
                }
            })
    }

    override fun getPokemon(number: String, onComplete: (Pokemon?) -> Unit, onError:
        (Throwable) -> Unit) {
        pokemonService
            .getPokemon(number)
            .enqueue(object : Callback<Pokemon>{
                override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                    onError(t)
                }
                override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onError(Throwable("Não foi possível realizar a requisição"))
                    }
                }
            })
    }
}