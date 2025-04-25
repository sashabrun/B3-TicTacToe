package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import com.example.tictactoe.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.appcompat.app.AlertDialog
import androidx.activity.OnBackPressedCallback
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation)
        binding.playOfflineBtn.startAnimation(buttonAnimation)
        binding.createOnlineGameBtn.startAnimation(buttonAnimation)
        binding.joinOnlineGameBtn.startAnimation(buttonAnimation)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })

        binding.playOfflineBtn.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            createOfflineGame()
        }

        binding.createOnlineGameBtn.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            createOnlineGame()
        }

        binding.joinOnlineGameBtn.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            joinOnlineGame()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_rules -> {
                val intent = Intent(this, RulesActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun createOfflineGame(){
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )
        startGame()
    }

    fun createOnlineGame(){
        GameData.myID = "X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameId = Random.nextInt(1000..9999).toString(),
                playersPresent = mutableListOf("X")
            )
        )
        startGame()
    }

    fun joinOnlineGame(){
        var gameId = binding.gameIdInput.text.toString()
        if(gameId.isEmpty()){
            binding.gameIdInput.error = "Please enter game ID"
            return
        }
        GameData.myID = "O"
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
                val model = it?.toObject(GameModel::class.java)
                if(model==null){
                    binding.gameIdInput.error = "Please enter valid game ID"
                }else{
                    model.gameStatus = GameStatus.JOINED
                    model.playersPresent.add("O")
                    GameData.saveGameModel(model)
                    startGame()
                }
            }
    }

    fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit the app?")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}