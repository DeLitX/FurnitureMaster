package com.delitx.furnituremaster.data_models.price_recognition

import com.delitx.furnituremaster.data_models.Product
import com.delitx.furnituremaster.data_models.generated.PriceDecodeLexer
import com.delitx.furnituremaster.data_models.generated.PriceDecodeParser
import com.delitx.furnituremaster.exceptions.WrongDataException
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

object PriceEvaluator {
    suspend fun evaluate(item: Product): Double {
        val helper = object : PriceRecognitionVisitor.RecogniseHelper {
            override fun getLinkValue(link: Int): Double {
                val property = item.propertiesList.keys.find { it.id == link }
                var value =
                    item.propertiesList[property]?.find { it.id == item.chosenConfiguration!!.set[link]?.id ?: "" }
                if (value == null && item.propertiesList[property] != null && item.propertiesList[property]?.size == 1 /*&& item.propertiesList[property]!![0] is NumeralValue*/) {
                    value = item.propertiesList[property]!![0]
                }
                if (value == null) {
                    throw WrongDataException()
                }
                return value.value.toDouble()
            }
        }
        val inputStream = CharStreams.fromString(item.priceFormula)
        val grammarLexer = PriceDecodeLexer(inputStream)
        val tokens = CommonTokenStream(grammarLexer)
        val parser = PriceDecodeParser(tokens)
        val tree = parser.expr()
        val visitor = PriceRecognitionVisitor(helper)
        var result = 0.0
        result = visitor.visit(tree)
        return result
    }
}
