package com.example.gimmea.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gimmea.data.models.Categories
import com.example.gimmea.data.repositories.SuggestionsRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DebugViewModel(
    private val appContext: Context,
    private val suggestionsRepository: SuggestionsRepositoryInterface
): ViewModel() {

    val categories: StateFlow<Categories> = suggestionsRepository.categories

    val _currentSuggestion: MutableStateFlow<String> = MutableStateFlow("")
    val currentSuggestion: StateFlow<String> = _currentSuggestion.asStateFlow()

    fun setCurrentSuggestion(suggestion: String) {
        _currentSuggestion.value = suggestion
    }


}