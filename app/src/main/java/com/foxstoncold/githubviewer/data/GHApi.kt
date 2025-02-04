package com.foxstoncold.githubviewer.data

import com.foxstoncold.githubviewer.sl
import retrofit2.Retrofit

class GHApi(private val retrofit: Retrofit) {
    init {
        sl.en()
    }
}