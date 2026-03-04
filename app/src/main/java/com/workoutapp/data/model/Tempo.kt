package com.workoutapp.data.model

data class Tempo(
    val eccentric: Int,
    val bottomPause: Int,
    val concentric: Int,
    val topPause: Int
) {
    override fun toString(): String = "$eccentric-$bottomPause-$concentric-$topPause"
}
