package com.example.tictactoe

enum class MatchResult {
    WIN, LOSS, DRAW
}

data class MatchHistoryItem(
    val gameId: String,
    val date: Long,
    val result: MatchResult,
    val opponent: String
)