package com.example.gimmea.di

import com.example.gimmea.data.repositories.SuggestionsRepositoryInterface
import com.example.gimmea.data.repositories.impl.DefaultSuggestionsRepository
import com.example.gimmea.data.repositories.impl.SokkyoSuggestionsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<SuggestionsRepositoryInterface> { SokkyoSuggestionsRepository(androidContext()) }
}