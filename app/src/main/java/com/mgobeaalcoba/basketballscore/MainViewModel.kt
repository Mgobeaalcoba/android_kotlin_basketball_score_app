package com.mgobeaalcoba.basketballscore

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var localScoreInt = 0
    var visitantScoreInt = 0

    // Reset:
    fun resetScore() {
        localScoreInt = 0
        visitantScoreInt = 0
    }

    fun minusOne(local: Boolean) {
        if (local) {
            --localScoreInt
        } else {
            --visitantScoreInt
        }
    }

   fun plusOne(local: Boolean) {
        if (local) {
            ++localScoreInt
        } else {
            ++visitantScoreInt
        }
    }

    fun plusTwo(local: Boolean) {
        if (local) {
            localScoreInt += 2
        } else {
            visitantScoreInt += 2
        }
    }

    fun createMatch() :MatchScore {
        return MatchScore(localScoreInt, visitantScoreInt)
    }

}