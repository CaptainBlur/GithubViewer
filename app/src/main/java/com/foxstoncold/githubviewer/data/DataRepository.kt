package com.foxstoncold.githubviewer.data

import com.foxstoncold.githubviewer.sl


class DataRepository(
    private val api: GHApi
) {
    fun printTest(){
        sl.w("test")
    }
}