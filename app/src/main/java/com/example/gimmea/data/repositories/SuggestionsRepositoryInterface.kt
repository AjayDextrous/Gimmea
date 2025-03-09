package com.example.gimmea.data.repositories

import com.example.gimmea.data.models.Categories
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository interface for Categories and Suggestions.
 */
interface SuggestionsRepositoryInterface {

    /**
     * StateFlow of Categories.
     */
    val categories: StateFlow<Categories>

    /**
     * Triggers a refresh of the data. Returns an error if the refresh fails.
     */
    suspend fun refresh(): Result<Unit>

}