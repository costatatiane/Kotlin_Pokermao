package br.com.fiap.pokermao.view.list

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.fiap.pokermao.R
import br.com.fiap.pokermao.view.ViewState
import br.com.fiap.pokermao.view.form.FormPokemonActivity
import kotlinx.android.synthetic.main.activity_list_pokemons.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ListPokemonsActivity : AppCompatActivity() {


    private val listPokemonsViewModel: ListPokemonsViewModel by viewModel()

    private val adapter: ListPokemonsAdapter by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pokemons)

        rvPokemons.adapter = adapter
        rvPokemons.layoutManager = GridLayoutManager(this, 3)

        listPokemonsViewModel.getPokemons()

        listPokemonsViewModel.viewState.observe(this, Observer { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    containerLoading.visibility = View.VISIBLE
                }
                is ViewState.Failed -> {
                    containerLoading.visibility = View.GONE
                    val message = viewState.throwable.message
                    if (message != "") Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                is ViewState.Success -> {
                    containerLoading.visibility = View.GONE

                    adapter.setItems(viewState.data)

                    adapter.setOnClickListener {
                        val pokemonDetail = Intent(this, FormPokemonActivity::class.java)
                        pokemonDetail.putExtra("POKEMON", it)
                        Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                        startActivityForResult(pokemonDetail, FormPokemonActivity.REQUEST_CHANGE_DATA)
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            FormPokemonActivity.REQUEST_CHANGE_DATA -> {
                when(resultCode) {
                    Activity.RESULT_OK -> listPokemonsViewModel.getPokemons()
                }
            }
        }
    }
}
