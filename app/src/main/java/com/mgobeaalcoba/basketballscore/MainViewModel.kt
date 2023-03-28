package com.mgobeaalcoba.basketballscore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var localScoreInt: MutableLiveData<Int> = MutableLiveData()
    var visitantScoreInt: MutableLiveData<Int> = MutableLiveData()

    // Creamos una función init que se ejecuta apenas se instancia un MainViewModel
    init {
        resetScore() // Nos aseguramos que los valores van a arrancar en cero
    }

    // Reset:
    fun resetScore() {
        localScoreInt.value = 0
        visitantScoreInt.value = 0
    }

    fun minusOne(local: Boolean) {
        if (local) {
            // Ejecuto el minus solo si el value de localScoreInt no es nulo (safe call)
            localScoreInt.value = localScoreInt.value?.minus(1)
        } else {
            visitantScoreInt.value = visitantScoreInt.value?.minus(1)
        }
    }

   fun plusOne(local: Boolean) {
        if (local) {
            localScoreInt.value = localScoreInt.value?.plus(1)
        } else {
            visitantScoreInt.value = visitantScoreInt.value?.plus(1)
        }
    }

    fun plusTwo(local: Boolean) {
        if (local) {
            localScoreInt.value = localScoreInt.value?.plus(2)
        } else {
            visitantScoreInt.value = visitantScoreInt.value?.plus(2)
        }
    }

    fun createMatch() :MatchScore {
        return MatchScore(localScoreInt.value!!, visitantScoreInt.value!!)
    }

}