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
    // TODO: Cuando trabajemos sobre el resto del ciclo de vida de la Activity vamos a tener que pasar las
    // TODO: variables de MianActivity a MainViewModel.

    private lateinit var viewModel: MainViewModel
    private var localScoreInt = 0
    private var visitantScoreInt = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instanciamos en "onCreate" nuestro viewModel:
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Equipo Local:

        binding.localMinusButton.setOnClickListener {
            if (localScoreInt != 0) {
                minusOne(true)
                binding.localScoreText.text = localScoreInt.toString()
            } else {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        binding.localPlusButton.setOnClickListener {
            plusOne(true)
            binding.localScoreText.text = localScoreInt.toString()
        }

        binding.localTwoPointsButton.setOnClickListener {
            plusTwo(true)
            binding.localScoreText.text = localScoreInt.toString()
        }

        // Equipo visitante:

        binding.visitantMinusButton.setOnClickListener {
            if (visitantScoreInt != 0) {
                minusOne(false)
                binding.visitantScoreText.text = visitantScoreInt.toString()
            } else {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        binding.visitantPlusButton.setOnClickListener {
            plusOne(false)
            binding.visitantScoreText.text = visitantScoreInt.toString()
        }

        binding.visitantTwoPointsButton.setOnClickListener {
            plusTwo(false)
            binding.visitantScoreText.text = visitantScoreInt.toString()
        }

        // Reloj

        binding.restartButton.setOnClickListener {
            resetScore()
            binding.localScoreText.text = localScoreInt.toString()
            binding.visitantScoreText.text = visitantScoreInt.toString()
        }

        binding.resultsButton.setOnClickListener {
            val match = MatchScore(localScoreInt, visitantScoreInt)
            openDetailActivity(match)
        }

    }

    private fun resetScore() {
        localScoreInt = 0
        visitantScoreInt = 0
    }

    private fun minusOne(local: Boolean) {
        if (local) {
            --localScoreInt
        } else {
            --visitantScoreInt
        }
    }

    private fun plusOne(local: Boolean) {
        if (local) {
            ++localScoreInt
        } else {
            ++visitantScoreInt
        }
    }

    private fun plusTwo(local: Boolean) {
        if (local) {
            localScoreInt += 2
        } else {
            visitantScoreInt += 2
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