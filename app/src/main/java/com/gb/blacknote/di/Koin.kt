package com.gb.blacknote.di

import com.gb.blacknote.model.MainViewModel
import org.koin.dsl.module

val koinModule = module {
    single<MainViewModel> { MainViewModel() }
}