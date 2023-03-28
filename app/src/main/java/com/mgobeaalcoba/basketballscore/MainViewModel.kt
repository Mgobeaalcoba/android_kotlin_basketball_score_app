package com.mgobeaalcoba.basketballscore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private var _localScoreInt: MutableLiveData<Int> = MutableLiveData()
    private var _visitantScoreInt: MutableLiveData<Int> = MutableLiveData()

    val localScoreInt: LiveData<Int>
        get() = _localScoreInt

    val visitantScoreInt: LiveData<Int>
        get() = _visitantScoreInt

    // Creamos una función init que se ejecuta apenas se instancia un MainViewModel
    init {
        resetScore() // Nos aseguramos que los valores van a arrancar en cero
    }

    // Reset:
    fun resetScore() {
        _localScoreInt.value = 0
        _visitantScoreInt.value = 0
    }

    fun minusOne(local: Boolean) {
        if (local) {
            // Ejecuto el minus solo si el value de localScoreInt no es nulo (safe call)
            _localScoreInt.value = _localScoreInt.value?.minus(1)
        } else {
            _visitantScoreInt.value = _visitantScoreInt.value?.minus(1)
        }
    }

   fun plusOne(local: Boolean) {
        if (local) {
            _localScoreInt.value = _localScoreInt.value?.plus(1)
        } else {
            _visitantScoreInt.value = _visitantScoreInt.value?.plus(1)
        }
    }

    fun plusTwo(local: Boolean) {
        if (local) {
            _localScoreInt.value = _localScoreInt.value?.plus(2)
        } else {
            _visitantScoreInt.value = _visitantScoreInt.value?.plus(2)
        }
    }

    fun createMatch() :MatchScore {
        return MatchScore(_localScoreInt.value!!, _visitantScoreInt.value!!)
    }

}