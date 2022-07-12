package com.gb.blacknote.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.blacknote.model.Model

class MainViewModel : ViewModel(), Model.MainViewModel {
    private val state = MutableLiveData<Model.MainState>()
    override fun appState() = state
}