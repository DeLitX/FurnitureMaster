package com.delitx.furnituremaster.data_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "named_value")
data class NamedValue(
    @PrimaryKey
    override var id: Int,
    var name: MultiLanguageString = MultiLanguageString(mapOf()),
    override var value: Int,
    var image: String = "",
    var catalogueImages: List<String> = listOf()
) :
    PropertyValue() {
    override fun getPropertyName(): String {
        return name.getText()
    }

    override fun toString(): String {
        return "{[id]:[$id],[name]:[$name],[value]:[$value],[image]:[$image],[catalogueImages]:[$catalogueImages]}"
    }
    companion object {

        fun fromMap(map: Map<String, String>): NamedValue {
            return NamedValue(
                id = map["id"]!!.toInt(),
                name = MultiLanguageString.fromString(map["name"]!!),
                value = map["value"]!!.toInt(),
                image = map["image"]!!
                // catalogueImages = map["catalogueImages"]!!
            )
        }
    }
}
