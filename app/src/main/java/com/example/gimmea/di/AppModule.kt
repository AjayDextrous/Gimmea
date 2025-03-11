package com.example.gimmea.di

import com.example.gimmea.data.repositories.SuggestionsRepositoryInterface
import com.example.gimmea.data.repositories.impl.CombineSourcesSuggestionsRepository
import com.example.gimmea.data.repositories.impl.DefaultSuggestionsRepository
import com.example.gimmea.data.repositories.impl.FileDownloadService
import com.example.gimmea.data.repositories.impl.SokkyoSuggestionsRepository
import com.example.gimmea.data.repositories.impl.WebsiteSuggestionsRepository
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit



val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://example.com/") // Base URL is required but we use dynamic URLs
            .client(get<OkHttpClient>())
            .build()
    }

//    single<FileDownloadService> {
//
//    }

    single<SuggestionsRepositoryInterface> {
        CombineSourcesSuggestionsRepository(
            DefaultSuggestionsRepository(androidContext()),
            SokkyoSuggestionsRepository(androidContext()),
//            WebsiteSuggestionsRepository(
//                androidContext(),
//                "https://gist.githubusercontent.com/AjayDextrous/c86c1b8a28db9dcc50c91eda8aa090d8/raw/399f2658cf57478b011c2fcbc605744493a1501c/online_pack.toml"
//            )
        )
    }
}