package com.delitx.furnituremaster.data_models

import com.delitx.furnituremaster.data.local.converters.Converters
import com.delitx.furnituremaster.data_models.dtos_string_recognition.StringEvaluator
import java.util.*

data class MultiLanguageString(private val mTexts: Map<String, String>) {
    fun getText(): String {
        return mTexts[Locale.getDefault().language] ?: mTexts["en"] ?: ""
    }
    fun getDefaultName(): String {
        return mTexts["en"] ?: ""
    }
    override fun toString(): String {
        return Converters.mapToString(mTexts)
    }
    companion object {
        fun fromString(string: String): MultiLanguageString {
            return MultiLanguageString(StringEvaluator.evaluateMap(string))
        }
    }
}
