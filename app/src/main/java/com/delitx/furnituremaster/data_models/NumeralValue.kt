package com.delitx.furnituremaster.data_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "numeral_value")
data class NumeralValue(
    @PrimaryKey
    override var id: Int,
    var minValue: Int,
    var maxValue: Int,
    var unit: MultiLanguageString
) : PropertyValue() {
    override var value: Int = minValue
        set(value) {
            if (value <minValue) {
                field = minValue
            } else if (value> maxValue) {
                field = maxValue
            }
            field = value
        }
    override fun getPropertyName(): String {
        return "$value ${unit.getText()}"
    }

    override fun toString(): String {
        return "{[id]:[$id],[minValue]:[$minValue],[maxValue]:[$maxValue],[unit]:[$unit],[value]:[$value]}"
    }
}
