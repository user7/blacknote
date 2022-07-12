package com.gb.blacknote.di

import com.gb.blacknote.model.Model
import com.gb.blacknote.viewmodel.MainViewModel
import org.koin.dsl.module

val koinModule = module {
    single<Model.MainViewModel> { MainViewModel() }
}