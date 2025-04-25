package com.example.tictactoe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MatchHistoryAdapter(private val matches: List<MatchHistoryItem>) :
    RecyclerView.Adapter<MatchHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gameIdTextView: TextView = view.findViewById(R.id.game_id_text)
        val dateTextView: TextView = view.findViewById(R.id.date_text)
        val resultTextView: TextView = view.findViewById(R.id.result_text)
        val opponentTextView: TextView = view.findViewById(R.id.opponent_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matches[position]

        holder.gameIdTextView.text = "Game #${match.gameId}"

        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        holder.dateTextView.text = dateFormat.format(Date(match.date))

        when (match.result) {
            MatchResult.WIN -> {
                holder.resultTextView.text = "WIN"
                holder.resultTextView.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark)
                )
            }
            MatchResult.LOSS -> {
                holder.resultTextView.text = "LOSS"
                holder.resultTextView.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark)
                )
            }
            MatchResult.DRAW -> {
                holder.resultTextView.text = "DRAW"
                holder.resultTextView.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray)
                )
            }
        }

        holder.opponentTextView.text = "vs ${match.opponent}"
    }

    override fun getItemCount() = matches.size
}