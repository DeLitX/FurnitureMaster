package com.delitx.furnituremaster.data_models.dtos_string_recognition

import com.delitx.furnituremaster.data_models.generated.MultiLanguageFromStringDecoderBaseVisitor
import com.delitx.furnituremaster.data_models.generated.MultiLanguageFromStringDecoderParser
import java.lang.Exception

class MultiLanguageVisitor : MultiLanguageFromStringDecoderBaseVisitor<String>() {
    val resultList = mutableListOf<Pair<String, String>>()
    val resultMap: MutableMap<String, String> = mutableMapOf()
    override fun visitExprPair(ctx: MultiLanguageFromStringDecoderParser.ExprPairContext?): String {
        try {
            val pair = Pair(visit(ctx?.value(0)), visit(ctx?.value(1)))
            resultList.add(pair)
            resultMap[pair.first] = pair.second
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.visitExprPair(ctx) ?: ""
    }

    override fun visitExprValueFinal(ctx: MultiLanguageFromStringDecoderParser.ExprValueFinalContext?): String {
        return ctx!!.text
    }

    override fun visitExprValueWord(ctx: MultiLanguageFromStringDecoderParser.ExprValueWordContext?): String {
        return ctx!!.text
    }
}
