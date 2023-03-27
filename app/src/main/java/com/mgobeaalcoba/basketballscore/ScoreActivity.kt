package com.mgobeaalcoba.basketballscore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mgobeaalcoba.basketballscore.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity() {
    // Companion object con las KEY´s:
    companion object {
        const val MATCH_SCORE_KEY = "matchscore"
    }
    // Función de apertura de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Levantamos el objeto que nos trajimos de MainActivity
        val bundle = intent.extras!!

        // Reconstruyo el objeto de clase MatchScore para usarlo:
        val matchScore = bundle.getParcelable<MatchScore>(MATCH_SCORE_KEY)!!

        // Especificamos de que objeto son los atributos que vamos a mostrar en el layout:
        binding.matchscore = matchScore

        if (matchScore.localScore > matchScore.visitanScore) {
            binding.finalMessage.text = getString(R.string.local_win)
        } else if (matchScore.localScore < matchScore.visitanScore) {
            binding.finalMessage.text = getString(R.string.visitant_win)
        } else {
            binding.finalMessage.text = getString(R.string.final_message)
        }

    }
}