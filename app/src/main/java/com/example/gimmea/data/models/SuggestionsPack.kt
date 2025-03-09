package com.example.gimmea.data.models

typealias Categories = Map<String, List<String>>

data class SuggestionsPack(
    val name: String,
    val categories: Categories
) {
    override fun toString(): String {
        return "[SuggestionsPack] \nname = $name\ncategories = \n\t${categories.keys.joinToString("\n\t")}\n"
    }
}
