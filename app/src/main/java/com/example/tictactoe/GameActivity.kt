package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.tictactoe.databinding.ActivityGameBinding
import androidx.appcompat.app.AlertDialog
import androidx.activity.OnBackPressedCallback

class GameActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var binding: ActivityGameBinding

    private var gameModel : GameModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })

        val buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale)

        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)

        binding.startGameBtn.setOnClickListener {
            it.startAnimation(buttonAnimation)
            startGame()
        }

        binding.gameBoard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        GameData.fetchGameModel()

        GameData.gameModel.observe(this){
            gameModel = it
            setUI()
        }
    }

    override fun onDestroy() {
        gameModel?.let {
            if (it.gameId != "-1" && it.gameStatus != GameStatus.FINISHED) {
                GameData.markPlayerLeft(it.gameId)
            }
        }
        super.onDestroy()
    }

    fun setUI(){
        gameModel?.apply {
            binding.btn0.text = filledPos[0]
            binding.btn1.text = filledPos[1]
            binding.btn2.text = filledPos[2]
            binding.btn3.text = filledPos[3]
            binding.btn4.text = filledPos[4]
            binding.btn5.text = filledPos[5]
            binding.btn6.text = filledPos[6]
            binding.btn7.text = filledPos[7]
            binding.btn8.text = filledPos[8]

            binding.startGameBtn.visibility = View.VISIBLE

            if (winningLine.isNotEmpty() && gameStatus == GameStatus.FINISHED) {
                binding.gameBoard.highlightWinningLine(winningLine.toTypedArray())
                binding.gameBoard.startAnimation(AnimationUtils.loadAnimation(this@GameActivity, R.anim.victory_animation))
            } else {
                binding.gameBoard.clearWinningLine()
            }

            binding.gameStatusText.text =
                when(gameStatus){
                    GameStatus.CREATED -> {
                        binding.startGameBtn.visibility = View.INVISIBLE
                        "Game ID: $gameId"
                    }
                    GameStatus.JOINED -> {
                        if (playersPresent.size < 2) {
                            binding.startGameBtn.isEnabled = false
                            "Waiting for opponent to join..."
                        } else {
                            binding.startGameBtn.isEnabled = true
                            "Click on start game"
                        }
                    }
                    GameStatus.INPROGRESS -> {
                        binding.startGameBtn.visibility = View.INVISIBLE
                        if (playersPresent.size < 2 && gameId != "-1") {
                            "Opponent left the game"
                        } else {
                            when(GameData.myID){
                                currentPlayer -> "Your turn"
                                else -> "$currentPlayer turn"
                            }
                        }
                    }
                    GameStatus.FINISHED -> {
                        if(winner.isNotEmpty()) {
                            when(GameData.myID){
                                winner -> "You won"
                                else -> "$winner Won"
                            }
                        }
                        else "DRAW"
                    }
                    GameStatus.ABANDONED -> {
                        binding.startGameBtn.isEnabled = false
                        "Opponent left the game"
                    }
                }
        }
    }

    fun startGame(){
        gameModel?.apply {
            if (gameId == "-1" || playersPresent.size >= 2) {
                updateGameData(
                    GameModel(
                        gameId = gameId,
                        gameStatus = GameStatus.INPROGRESS,
                        filledPos = mutableListOf("","","","","","","","",""),
                        winningLine = listOf(),
                        playersPresent = playersPresent
                    )
                )
            } else {
                Toast.makeText(applicationContext, "Waiting for opponent to join", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateGameData(model : GameModel){
        GameData.saveGameModel(model)
    }

    fun checkForWinner(){
        val winningPos = arrayOf(
            intArrayOf(0,1,2),
            intArrayOf(3,4,5),
            intArrayOf(6,7,8),
            intArrayOf(0,3,6),
            intArrayOf(1,4,7),
            intArrayOf(2,5,8),
            intArrayOf(0,4,8),
            intArrayOf(2,4,6),
        )

        gameModel?.apply {
            for (i in winningPos){
                if(
                    filledPos[i[0]] == filledPos[i[1]] &&
                    filledPos[i[1]]== filledPos[i[2]] &&
                    filledPos[i[0]].isNotEmpty()
                ){
                    gameStatus = GameStatus.FINISHED
                    winner = filledPos[i[0]]
                    winningLine = i.toList()
                    break
                }
            }

            if(filledPos.none(){ it.isEmpty() } && gameStatus != GameStatus.FINISHED){
                gameStatus = GameStatus.FINISHED
            }

            updateGameData(this)
        }
    }

    override fun onClick(v: View?) {
        gameModel?.apply {
            if(gameStatus!= GameStatus.INPROGRESS){
                Toast.makeText(applicationContext,"Game not started",Toast.LENGTH_SHORT).show()
                return
            }

            if (gameId != "-1" && playersPresent.size < 2) {
                Toast.makeText(applicationContext, "Opponent left the game", Toast.LENGTH_SHORT).show()
                return
            }

            if(gameId!="-1" && currentPlayer!=GameData.myID ){
                Toast.makeText(applicationContext,"Not your turn",Toast.LENGTH_SHORT).show()
                return
            }

            val clickedPos =(v?.tag  as String).toInt()
            if(filledPos[clickedPos].isEmpty()){
                v.startAnimation(AnimationUtils.loadAnimation(this@GameActivity, R.anim.button_click))

                filledPos[clickedPos] = currentPlayer
                currentPlayer = if(currentPlayer=="X") "O" else "X"
                checkForWinner()
                updateGameData(this)
            }
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Quit the game")
            .setMessage("Are you sure you want to leave the game?")
            .setPositiveButton("Yes") { _, _ ->
                gameModel?.let {
                    if (it.gameId != "-1" && it.gameStatus != GameStatus.FINISHED) {
                        GameData.markPlayerLeft(it.gameId)
                    }
                }
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}