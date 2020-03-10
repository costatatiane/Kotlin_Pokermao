package com.example.pokermao.view.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import br.com.fiap.pokermao.R
import br.com.fiap.pokermao.view.ViewState
import br.com.fiap.pokermao.view.detail.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DetailActivity : AppCompatActivity() {

    val detailViewModel: DetailViewModel by viewModel()
    val picasso: Picasso by inject()

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initTTS()

        detailViewModel.getPokemon(intent.getStringExtra("POKEMON_NUMBER"))

        detailViewModel.viewState.observe(this, Observer { viewState ->
            when(viewState) {
                is ViewState.Loading -> {
                    containerLoading.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    containerLoading.visibility = View.GONE

                    // semelhante ao guard let
                    viewState.data?.let { pokemon ->
                        picasso.load("https://pokedexdx.herokuapp.com${pokemon.imageUrl}").into(ivPokemon)
                        tvPokemonName.text = "${pokemon.number} ${pokemon.name}"
                        tvPokemonDescription.text = pokemon.description

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(pokemon.description, TextToSpeech.QUEUE_FLUSH, null, null)
                        } else {
                            tts.speak(pokemon.description, TextToSpeech.QUEUE_FLUSH, null)
                        }
                    }

                }
                is ViewState.Failed -> {
                    containerLoading.visibility = View.GONE
                    showMessage(viewState.throwable.message ?: "Deu ruim")
                }
            }

        })
    }

    private fun initTTS() {
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {
            if( it != TextToSpeech.ERROR) {
                tts.language = Locale.US
            }
        })
    }

    private fun showMessage(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}