package com.example.gimmea

import cc.ekblad.toml.model.TomlValue
import cc.ekblad.toml.serialization.from
import cc.ekblad.toml.tomlMapper
import com.example.gimmea.data.models.SuggestionsPack
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

import java.nio.file.Path

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TomlParserUnitTests {
    @Test
    fun tomlParsing_parseDefaultPack_ShouldSucceed() { // TODO: proper naming convention
        val mapper = tomlMapper {}
        val tomlFile = Path.of("src/main/assets/default_pack.toml")
        val suggestionPack = mapper.decode<SuggestionsPack>(TomlValue.from(tomlFile))
        assertEquals("default", suggestionPack.name)
        assertNotEquals(0, suggestionPack.categories.size)
        suggestionPack.categories.forEach { (_, suggestions) ->
            assertNotEquals(0, suggestions.size)
        }
    }
}