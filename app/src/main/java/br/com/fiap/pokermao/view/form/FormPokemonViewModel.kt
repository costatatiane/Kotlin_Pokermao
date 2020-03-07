package br.com.fiap.pokermao.view.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.pokermao.model.Pokemon
import br.com.fiap.pokermao.repository.PokemonRepository
import br.com.fiap.pokermao.view.ViewState

class FormPokemonViewModel(
    val pokemonRepository: PokemonRepository
) : ViewModel() {

    val viewState: MutableLiveData<ViewState<String>> = MutableLiveData()

    fun updatePokemon(pokemon: Pokemon) {
        viewState.value = ViewState.Loading
        pokemonRepository.updatePokemon(
            pokemon = pokemon,
            onComplete = { pokemon ->
                viewState.value = ViewState.Success("Dados atualizados com sucesso")
            },
            onError = {error ->
                viewState.value = ViewState.Failed(error)
            }
        )
    }
}