package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private var wordsList: MutableList<String>? = mutableListOf()
    private lateinit var currentWord: String

    private var _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentScrambledWord = MutableLiveData<String>() // object remains same, only value within is changed
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if(it==null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

    private var _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    init { // to initialize lateinit properties when class created for the first time
           // moving init block after defining class properties -> solves NPE
        Log.d("GameFragment", "GameViewModel created")
        getNextWord()
    }

    private fun getNextWord(){
        do{
            currentWord = allWordsList.random()
        } while (wordsList?.contains(currentWord) == true)
        wordsList?.add(currentWord)
        _currentWordCount.value = (_currentWordCount.value)?.inc()
        do {
            val tempWord = currentWord.toCharArray()
            tempWord.shuffle()
            _currentScrambledWord.value = String(tempWord)
        } while (_currentScrambledWord.value == currentWord)
    }

    fun nextWord(): Boolean{
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        }else false
    }

    private fun increaseScore(){
        _score.value = (_score.value)?.plus(SCORE_INCREASE) // += won't work, coz it is LiveData, not Int
    }

    fun isUserWordCorrect(playerWord: String): Boolean{
        return if(playerWord.equals(currentWord, true)){
            increaseScore()
            true
        } else false
    }

    /**
    * Re-initializes the game data to restart the game.
    */
    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value = 0
        wordsList?.clear()
        getNextWord()
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