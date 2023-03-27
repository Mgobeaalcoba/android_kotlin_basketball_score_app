package com.mgobeaalcoba.basketballscore

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MatchScore (val localScore: Int, val visitanScore: Int) : Parcelable {

    fun getScoreString(): String {
        return "$localScore - $visitanScore"
    }
}