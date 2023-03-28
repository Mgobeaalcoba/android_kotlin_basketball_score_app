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

        // Asigno nuestra instanciación del MainViewModel, que es viewModel como atributo mainViewModel del binding:
        // mainViewModel es la variable que creamos en el layout!!!
        binding.mainViewModel = viewModel

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

        // Decisión: Sigo manejando los metodos de MainViewModel.minusOne y .createMatch por acá
        // dado que minusOne me retorna un booleano que lo uso para lanzar un toast al user y un log
        // al developer. En el caso del .createMatch es una función que está acá y no en el MainViewModel

        // Equipo Local:
        binding.localMinusButton.setOnClickListener {
            val exeToast = viewModel.minusOne(true)
            if (exeToast) {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
        }

        // Equipo visitante:
        binding.visitantMinusButton.setOnClickListener {
            val exeToast = viewModel.minusOne(false)
            if (exeToast) {
                Log.w("MainActivity","The score is already at zero")
                Toast.makeText(this, getString(R.string.already_at_zero),Toast.LENGTH_SHORT).show()
            }
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