package br.com.fiap.pokermao.repository

interface PokemonRepository {
    fun checkHealth(
        onComplete:() -> Unit,
        onError: (Throwable?) -> Unit
    )
}