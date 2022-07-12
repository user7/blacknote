package com.gb.blacknote.ui

import androidx.fragment.app.Fragment
import com.gb.blacknote.model.MainViewModel
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {
    private val viewModel by inject<MainViewModel>()
}