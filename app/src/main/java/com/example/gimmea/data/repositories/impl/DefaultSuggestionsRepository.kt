package com.example.gimmea.data.repositories.impl

import android.content.Context
import cc.ekblad.toml.model.TomlValue
import cc.ekblad.toml.serialization.from
import cc.ekblad.toml.tomlMapper
import com.example.gimmea.data.models.Categories
import com.example.gimmea.data.models.SuggestionsPack
import com.example.gimmea.data.repositories.SuggestionsRepositoryInterface
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.CoroutineContext

class DefaultSuggestionsRepository(context: Context) : SuggestionsRepositoryInterface,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = CoroutineName("DefaultSuggestionsRepository")

    private val _categories: MutableStateFlow<Categories> = MutableStateFlow(emptyMap())

    init {
        val tomlFileStream = context.assets.open("default_pack.toml")
        val mapper = tomlMapper {}
        val suggestionPack = mapper.decode<SuggestionsPack>(TomlValue.from(tomlFileStream))
        _categories.value = suggestionPack.categories
    }

    override val categories: StateFlow<Categories>
        get() = _categories.asStateFlow()

    override suspend fun refresh(): Result<Unit> {
        return Result.success(Unit)
    }
}