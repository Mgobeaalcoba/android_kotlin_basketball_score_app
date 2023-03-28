package com.mgobeaalcoba.basketballscore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mgobeaalcoba.basketballscore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instanciamos en "onCreate" nuestro viewModel:
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Equipo Local:

        binding.localMinusButton.setOnClickListener {
            if (viewModel.localScoreInt != 0) {
                viewModel.minusOne(true)
                binding.localScoreText.text = viewModel.localScoreInt.toString()
            } else {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        binding.localPlusButton.setOnClickListener {
            viewModel.plusOne(true)
            binding.localScoreText.text = viewModel.localScoreInt.toString()
        }

        binding.localTwoPointsButton.setOnClickListener {
            viewModel.plusTwo(true)
            binding.localScoreText.text = viewModel.localScoreInt.toString()
        }

        // Equipo visitante:

        binding.visitantMinusButton.setOnClickListener {
            if (viewModel.visitantScoreInt != 0) {
                viewModel.minusOne(false)
                binding.visitantScoreText.text = viewModel.visitantScoreInt.toString()
            } else {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        binding.visitantPlusButton.setOnClickListener {
            viewModel.plusOne(false)
            binding.visitantScoreText.text = viewModel.visitantScoreInt.toString()
        }

        binding.visitantTwoPointsButton.setOnClickListener {
            viewModel.plusTwo(false)
            binding.visitantScoreText.text = viewModel.visitantScoreInt.toString()
        }

        // Reloj

        binding.restartButton.setOnClickListener {
            viewModel.resetScore()
            binding.localScoreText.text = viewModel.localScoreInt.toString()
            binding.visitantScoreText.text = viewModel.visitantScoreInt.toString()
        }

        binding.resultsButton.setOnClickListener {
            //val match = MatchScore(localScoreInt, visitantScoreInt)
            openDetailActivity(viewModel.createMatch())
        }

    }

    private fun openDetailActivity(matchScore : MatchScore) {
        // Creamos in intent activity:
        val intent = Intent(this, ScoreActivity::class.java) // Explicit intent

        // Solo voy a pasar como putExtra el objeto matchScore:
        intent.putExtra(ScoreActivity.MATCH_SCORE_KEY, matchScore)

        // Enviamos el objeto matchScore a la siguiente activity:
        startActivity(intent)
    }

}