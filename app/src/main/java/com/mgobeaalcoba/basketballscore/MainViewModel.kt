package com.mgobeaalcoba.basketballscore

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var localScoreInt = 0
    var visitantScoreInt = 0
    val match = MatchScore(localScoreInt, visitantScoreInt)

    // Reset:
    fun resetScore() {
        localScoreInt = 0
        visitantScoreInt = 0
    }

}