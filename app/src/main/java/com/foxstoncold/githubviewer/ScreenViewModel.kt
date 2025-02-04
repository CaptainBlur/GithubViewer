package com.foxstoncold.githubviewer

import androidx.lifecycle.ViewModel
import com.foxstoncold.githubviewer.data.DataRepository

class ScreenViewModel(private val dr: DataRepository): ViewModel() {

    init {
        dr.printTest()
    }
}