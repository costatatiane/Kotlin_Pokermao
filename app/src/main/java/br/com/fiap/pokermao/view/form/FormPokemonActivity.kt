package br.com.fiap.pokermao.view.form

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import br.com.fiap.pokermao.R
import br.com.fiap.pokermao.model.Pokemon
import br.com.fiap.pokermao.view.ViewState
import br.com.fiap.pokermao.view.list.ListPokemonsActivity
import br.com.fiap.pokermao.view.list.ListPokemonsAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_form_pokemon.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class FormPokemonActivity : AppCompatActivity() {

    val formPokemonViewModel: FormPokemonViewModel by viewModel()
    val picasso: Picasso by inject()
    lateinit var pokemon : Pokemon

    private fun showMessage(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pokemon)
        setValues()

        btSaveForm.setOnClickListener {
            pokemon.attack = sbAttack.progress
            pokemon.defense = sbDefense.progress
            pokemon.velocity = sbVelocity.progress
            pokemon.ps = sbPS.progress
            formPokemonViewModel.updatePokemon(pokemon)
        }

        formPokemonViewModel.viewState.observe(this, Observer { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    containerLoading.visibility = View.VISIBLE
                }
                is ViewState.Failed -> {
                    containerLoading.visibility = View.GONE
                    showMessage(viewState.throwable.message ?: "Deu ruim")
                }
                is ViewState.Success -> {
                    containerLoading.visibility = View.GONE
                    showMessage(viewState.data)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })
    }

    private fun setValues() {
        pokemon = intent.getParcelableExtra(EXTRA_POKEMON)
        tvPokemonNameForm.text = pokemon.name

        picasso.load("https://pokedexdx.herokuapp.com${pokemon.imageUrl}").into(ivPokemonForm)

        sbAttack.progress = pokemon.attack
        sbDefense.progress = pokemon.defense
        sbPS.progress = pokemon.ps
        sbVelocity.progress = pokemon.velocity

        tvAttackValue.text = pokemon.attack.toString()
        tvDefenseValue.text = pokemon.defense.toString()
        tvPSValue.text = pokemon.ps.toString()
        tvVelocityValue.text = pokemon.velocity.toString()

        setListener(sbAttack, tvAttackValue)
        setListener(sbDefense, tvDefenseValue)
        setListener(sbVelocity, tvVelocityValue)
        setListener(sbPS, tvPSValue)
    }

    private fun setListener(seekBar: SeekBar, textView: TextView) {
        // mexendo na barra de progresso e replicar na caixa de texto conforme alterações
        seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser:
            Boolean) {
                textView.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    companion object {
        val REQUEST_CHANGE_DATA = 200
        val EXTRA_POKEMON = "EXTRA_POKEMON"
    }
}
