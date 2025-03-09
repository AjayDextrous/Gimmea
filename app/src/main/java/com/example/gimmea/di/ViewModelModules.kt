package com.example.gimmea.di

import com.example.gimmea.viewmodels.DebugViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val debugViewModelModule = module {
    viewModel { DebugViewModel(androidContext(), get()) }
}