package br.com.fiap.pokermao.view.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.pokermao.model.Pokemon
import br.com.fiap.pokermao.repository.PokemonRepository
import br.com.fiap.pokermao.view.ViewState

class ListPokemonsViewModel (val pokemonRepository: PokemonRepository) : ViewModel() {

    val viewState: MutableLiveData<ViewState<List<Pokemon>>> = MutableLiveData()

    fun getPokemons() {
        viewState.value = ViewState.Loading
        pokemonRepository.getPokemons(
            150, "number,asc", {pokemonsList ->
                viewState.value = ViewState.Success(pokemonsList)
            }, { error ->
                viewState.value = ViewState.Failed(error)
            }
        )
    }
}