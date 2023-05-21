package com.delitx.furnituremaster.data.local.converters

import android.util.Log
import androidx.room.TypeConverter
import com.delitx.furnituremaster.data_models.*
import com.delitx.furnituremaster.data_models.dtos_string_recognition.StringEvaluator
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        fun <T1, T2> mapToString(map: Map<T1, T2>): String {
            var result = "{"
            var isFirst = true
            for (i in map) {
                if (!isFirst) {
                    result += ","
                }
                result += "[${i.key}]:[${i.value}]"
                isFirst = false
            }
            return "$result}"
        }
    }

    @TypeConverter
    fun fromMultiLanguageString(item: MultiLanguageString): String {
        return item.toString()
    }

    @TypeConverter
    fun toMultiLanguageString(string: String): MultiLanguageString {
        return MultiLanguageString.fromString(string)
    }

    @TypeConverter
    fun fromPropertyValue(item: PropertyValue): String {
        return item.toString()
    }

    @TypeConverter
    fun toPropertyValue(string: String): PropertyValue {
        val map = StringEvaluator.evaluateMap(string)
        Log.d("basketTest", "saveBasket: property string $string")
        return try {
            NamedValue(
                id = map["id"]!!.toInt(),
                name = MultiLanguageString.fromString(map["name"]!!),
                value = map["value"]!!.toInt(),
            )
        } catch (e: NullPointerException) {
            val value = NumeralValue(
                id = map["id"]!!.toInt(),
                minValue = map["minValue"]!!.toInt(),
                maxValue = map["maxValue"]!!.toInt(),
                unit = MultiLanguageString.fromString(map["unit"]!!)
            )
            value.value = map["value"]?.toInt() ?: value.minValue
            value
        }
    }

    @TypeConverter
    fun fromProductPropertiesSet(item: ProductPropertiesSet): String {
        return item.toString()
    }

    @TypeConverter
    fun toProductPropertiesSet(string: String): ProductPropertiesSet {
        val evaluatedSet = StringEvaluator.evaluateMap(string)
        Log.d("basketTest", "saveBasket: evaluatedSet $evaluatedSet")
        return ProductPropertiesSet(set = evaluatedSet.mapValues {
            toPropertyValue(it.value)
        }.mapKeys { it.key.toInt() }.toMutableMap())
    }

    //help for BasketDTO class
    @TypeConverter
    fun toString(item: List<Pair<String, ProductPropertiesSet>>): String {
        var result = "{"
        var isFirst = true
        for (i in item) {
            if (!isFirst) {
                result += ","
            }
            result += "[${i.first}]:[${i.second}]"
            isFirst = false
        }
        return "$result}"
    }

    @TypeConverter
    fun toList(string: String): List<Pair<String, ProductPropertiesSet>> {
        Log.d("basketTest", "saveBasket: string $string")
        val evaluatedSet = StringEvaluator.evaluateList(string)
        Log.d("basketTest", "saveBasket: evaluatedSet $evaluatedSet")
        val result = mutableListOf<Pair<String, ProductPropertiesSet>>()
        for (i in evaluatedSet) {
            result.add(Pair(i.first, toProductPropertiesSet(i.second)))
        }
        return result
    }

    ///list of image links converters
    @TypeConverter
    fun ImageLinksListToString(item: List<Int>): String {
        val gson = GsonBuilder().create()
        val result = gson.toJson(item)
        return result
    }

    @TypeConverter
    fun stringToImageLinksList(string: String): List<Int> {
        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(string,typeToken)
    }

    @TypeConverter
    fun stringListToString(item: List<String>): String {
        val gson = GsonBuilder().create()
        val result = gson.toJson(item)
        return result
    }

    @TypeConverter
    fun stringToStringList(string: String): List<String> {
        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string,typeToken)
    }
}