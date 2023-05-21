package com.delitx.furnituremaster.data_models.network_dtos

import com.delitx.furnituremaster.data_models.MultiLanguageString
import com.delitx.furnituremaster.data_models.NumeralValue
import com.google.gson.annotations.SerializedName

data class NumeralValueNetworkDto(
    var id: Int,
    @SerializedName("min_value")
    var minValue: Int,
    @SerializedName("max_value")
    var maxValue: Int,
    var unit: Map<String, String>
) {
    fun toModel(): NumeralValue {
        return NumeralValue(
            id = id,
            minValue = minValue,
            maxValue = maxValue,
            unit = MultiLanguageString(unit)
        )
    }
}
