package com.mgobeaalcoba.basketballscore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mgobeaalcoba.basketballscore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargamos nuestro data binding:
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instanciamos en "onCreate" nuestro viewModel:
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Generamos los Observadores de las variables del MainViewModel:
        viewModel.localScoreInt.observe(this, Observer {
                localScoreValue: Int ->
            binding.localScoreText.text = localScoreValue.toString()
        })

        viewModel.visitantScoreInt.observe(this, Observer {
                visitantScoreValue: Int ->
            binding.visitantScoreText.text = visitantScoreValue.toString()
        })

        setupButtons()
    }

    private fun setupButtons() {

        // Equipo Local:
        binding.localMinusButton.setOnClickListener {
            if (viewModel.localScoreInt.value != 0) { // TODO: Lógica variable que tengo que sacarla aún
                viewModel.minusOne(true)
            } else {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        binding.localPlusButton.setOnClickListener {
            viewModel.plusOne(true)
        }

        binding.localTwoPointsButton.setOnClickListener {
            viewModel.plusTwo(true)
        }

        // Equipo visitante:
        binding.visitantMinusButton.setOnClickListener {
            if (viewModel.visitantScoreInt.value != 0) { // TODO: Lógica variable que tengo que sacarla aún
                viewModel.minusOne(false)
            } else {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        binding.visitantPlusButton.setOnClickListener {
            viewModel.plusOne(false)
        }

        binding.visitantTwoPointsButton.setOnClickListener {
            viewModel.plusTwo(false)
        }

        // Reloj

        binding.restartButton.setOnClickListener {
            viewModel.resetScore()
        }

        binding.resultsButton.setOnClickListener {
            openDetailActivity(viewModel.createMatch())
        }

    }

    private fun openDetailActivity(matchScore : MatchScore) {
        // Creamos un intent activity:
        val intent = Intent(this, ScoreActivity::class.java) // Explicit intent

        // Solo voy a pasar como putExtra el objeto matchScore:
        intent.putExtra(ScoreActivity.MATCH_SCORE_KEY, matchScore)

        // Enviamos el objeto matchScore a la siguiente activity:
        startActivity(intent)
    }

}