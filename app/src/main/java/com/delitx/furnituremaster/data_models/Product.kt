package com.delitx.furnituremaster.data_models

data class Product(
    var id: Int,
    var name: MultiLanguageString,
    var priceFormula: String,
    val imageUrl: List<CommentedImage>,
    var propertiesList: Map<ProductProperty, List<PropertyValue>>,
    var order: List<Int>,
    var chosenConfiguration: ProductPropertiesSet? = null,
    var amount: Int = 1,
    var modelPath: String? = null
) {
    fun configurationToPresentableString(): String {
        var result: String = ""
        var isFirst = true
        val properties = propertiesList.keys
        for (i in chosenConfiguration?.set ?: mapOf()) {
            if (!isFirst) {
                result += '\n'
            }
            result += "${properties.find { it.id == i.key }?.name?.getText() ?: ""} : ${i.value.getPropertyName()}"
            isFirst = false
        }
        return result
    }
}
