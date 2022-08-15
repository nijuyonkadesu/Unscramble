package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    init { // to initialize lateinit properties when class created for the first time
        Log.d("GameFragment", "GameViewModel created")
        getNextWord()
    }
    private var wordsList: MutableList<String>? = mutableListOf()
    private lateinit var currentWord: String
    private var _score = 0
    val score: Int
        get() = _score
    private var _currentWordCount = 0
    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord  // backing

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel is destroyed")
    }

    private fun getNextWord(){
        do{
            currentWord = allWordsList.random()
        } while (wordsList?.contains(currentWord) == true)
        wordsList?.add(currentWord)
        ++_currentWordCount
        do {
            val tempWord = currentWord.toCharArray()
            tempWord.shuffle()
            _currentScrambledWord = String(tempWord)
        } while (_currentScrambledWord == currentWord)
    }

    fun nextWord(): Boolean{
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        }else false
    }

    private fun increaseScore(){
        _score += SCORE_INCREASE
    }

    fun isUserWordCorrect(playerWord: String): Boolean{
        return if(playerWord.equals(currentWord, true)){
            increaseScore()
            true
        } else false
    }
}

/*
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()
        Log.d("GameFragment", tempWord.contentToString())

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList?.contains(currentWord) == true) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList?.add(currentWord)
        }
    }
*/