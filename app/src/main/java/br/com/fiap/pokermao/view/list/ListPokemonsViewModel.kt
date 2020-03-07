package br.com.fiap.pokermao.view.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.pokermao.model.Pokemon
import br.com.fiap.pokermao.repository.PokemonRepository

class ListPokemonsViewModel (val pokemonRepository: PokemonRepository) : ViewModel() {

    val messageError: MutableLiveData<String> = MutableLiveData()
    val pokemons: MutableLiveData<List<Pokemon>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getPokemons() {
        isLoading.value = true
        pokemonRepository.getPokemons(
            150, "number,asc", {pokemonsList ->
                pokemons.value = pokemonsList
                messageError.value = ""
                isLoading.value = false
            }, { error ->
                pokemons.value = emptyList()
                messageError.value = error.message
                isLoading.value = false
            }
        )
    }
}