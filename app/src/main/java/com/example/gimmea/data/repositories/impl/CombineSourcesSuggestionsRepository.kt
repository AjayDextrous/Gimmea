package com.example.gimmea.data.repositories.impl

import com.example.gimmea.data.models.Categories
import com.example.gimmea.data.repositories.SuggestionsRepositoryInterface
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext

class CombineSourcesSuggestionsRepository(vararg val sources: SuggestionsRepositoryInterface ): SuggestionsRepositoryInterface, CoroutineScope {

    override val coroutineContext: CoroutineContext = CoroutineName("CombineSuggestionsRepository")

    private val _categories: Flow<Categories> = combine(sources.map { it.categories }){ maps ->
            maps.fold(mutableMapOf<String, MutableList<String>>()) { acc, map ->
                map.forEach { (key, value) ->
                    acc.getOrPut(key) { mutableListOf() }.addAll(value)
                }
                acc
            }
        }

    override val categories: StateFlow<Categories>
        get() = _categories.stateIn(
            scope = this,
            started = SharingStarted.Eagerly,
            initialValue = emptyMap()
        )

    override suspend fun refresh(): Result<Unit> {
        return coroutineScope {
            val results = sources.map { async { it.refresh() } }.awaitAll()

            if (results.all { it.isSuccess }) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("One or more refresh operations failed"))
            }
        }
    }
}