package com.example.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object GameData {
    private var _gameModel : MutableLiveData<GameModel> = MutableLiveData()
    var gameModel : LiveData<GameModel> = _gameModel
    var myID = ""

    fun saveGameModel(model : GameModel){
        model.lastUpdated = System.currentTimeMillis()

        _gameModel.postValue(model)
        if(model.gameId!="-1"){
            Firebase.firestore.collection("games")
                .document(model.gameId)
                .set(model)
            fetchGameModel()
        }
    }

    fun fetchGameModel(){
        gameModel.value?.apply {
            if(gameId!="-1"){
                Firebase.firestore.collection("games")
                    .document(gameId)
                    .addSnapshotListener { value, _ ->
                        val model = value?.toObject(GameModel::class.java)
                        _gameModel.postValue(model)
                    }
            }
        }
    }

    fun markPlayerLeft(gameId: String) {
        if (gameId != "-1") {
            gameModel.value?.apply {
                val updatedModel = this.copy()
                updatedModel.playersPresent.remove(myID)

                if ((updatedModel.gameStatus == GameStatus.JOINED || updatedModel.gameStatus == GameStatus.INPROGRESS) &&
                    updatedModel.playersPresent.size < 2) {
                    updatedModel.gameStatus = GameStatus.ABANDONED
                }

                saveGameModel(updatedModel)
            }
        }
    }
}