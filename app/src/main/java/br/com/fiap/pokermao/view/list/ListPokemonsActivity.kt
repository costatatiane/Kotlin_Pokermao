package br.com.fiap.pokermao.view.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.fiap.pokermao.R
import br.com.fiap.pokermao.view.form.FormPokemonActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_list_pokemons.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ListPokemonsActivity : AppCompatActivity() {

    val listPokemonsViewModel: ListPokemonsViewModel by viewModel()
    val picasso: Picasso by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pokemons)

        listPokemonsViewModel.getPokemons()

        listPokemonsViewModel.isLoading.observe(this, Observer { response ->
            containerLoading.visibility = if(response) View.VISIBLE else View.GONE
        })
        listPokemonsViewModel.messageError.observe(this, Observer { error ->
            if(error != "") Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })
        listPokemonsViewModel.pokemons.observe(this, Observer { listPokemons ->
            rvPokemons.adapter = ListPokemonsAdapter(listPokemons, picasso) { pokemon ->
                val pokemonDetail = Intent(this, FormPokemonActivity::class.java)
                pokemonDetail.putExtra("POKEMON", pokemon)
                startActivity(pokemonDetail)
                finish()
            }
            rvPokemons.layoutManager = GridLayoutManager(this, 3)

        })
    }
}
