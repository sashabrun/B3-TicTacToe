
package com.example.tictactoe

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tictactoe.databinding.ActivityMatchHistoryBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.Manifest
import androidx.core.app.NotificationManagerCompat
import android.widget.Toast

class MatchHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchHistoryBinding
    private lateinit var adapter: MatchHistoryAdapter
    private val matches = mutableListOf<MatchHistoryItem>()
    private val NOTIFICATION_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.historyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Match History"

        adapter = MatchHistoryAdapter(matches)
        binding.matchesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.matchesRecyclerView.adapter = adapter

        requestNotificationPermission()

        binding.historyContent.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in))

        loadMatchHistory()
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You'll receive notifications when it's your turn", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMatchHistory() {
        binding.progressBar.visibility = View.VISIBLE
        binding.statsContainer.visibility = View.GONE

        var wins = 0
        var losses = 0
        var draws = 0

        Firebase.firestore.collection("games")
            .whereArrayContains("playersPresent", GameData.myID)
            .whereEqualTo("gameStatus", GameStatus.FINISHED)
            .orderBy("lastUpdated", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                matches.clear()

                for (document in documents) {
                    val game = document.toObject(GameModel::class.java)

                    val result = when {
                        game.winner.isEmpty() -> {
                            draws++
                            MatchResult.DRAW
                        }
                        game.winner == GameData.myID -> {
                            wins++
                            MatchResult.WIN
                        }
                        else -> {
                            losses++
                            MatchResult.LOSS
                        }
                    }

                    val opponent = game.playersPresent.firstOrNull { it != GameData.myID } ?: "Unknown"

                    matches.add(
                        MatchHistoryItem(
                            gameId = game.gameId,
                            date = game.lastUpdated ?: System.currentTimeMillis(),
                            result = result,
                            opponent = opponent
                        )
                    )
                }

                binding.winsCount.text = wins.toString()
                binding.lossesCount.text = losses.toString()
                binding.drawsCount.text = draws.toString()

                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
                binding.statsContainer.visibility = View.VISIBLE

                if (matches.isEmpty()) {
                    binding.emptyStateText.visibility = View.VISIBLE
                } else {
                    binding.emptyStateText.visibility = View.GONE
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load match history", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}