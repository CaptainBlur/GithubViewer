package com.foxstoncold.githubviewer.di

import com.foxstoncold.githubviewer.ScreenViewModel
import com.foxstoncold.githubviewer.data.DataRepository
import com.foxstoncold.githubviewer.data.GHApi
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single<Retrofit> { retrofit() }
    single { GHApi(get()) }

    single { DataRepository(get()) }

    viewModel { ScreenViewModel(get()) }
}

fun retrofit(): Retrofit = Retrofit.Builder()
    .baseUrl("https://api.github.com")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()