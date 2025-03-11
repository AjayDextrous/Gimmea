package com.example.gimmea.data.repositories.impl

import android.content.Context
import com.example.gimmea.data.models.Categories
import com.example.gimmea.data.repositories.SuggestionsRepositoryInterface
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


class WebsiteSuggestionsRepository(context: Context, url: String): SuggestionsRepositoryInterface {
    override val categories: StateFlow<Categories>
        get() = TODO("Not yet implemented")

    override suspend fun refresh(): Result<Unit> {
        TODO("Not yet implemented")
    }
}

interface FileDownloadService {
    @GET
    fun downloadTomlFile(@Url url: String?): Call<String?>?
}