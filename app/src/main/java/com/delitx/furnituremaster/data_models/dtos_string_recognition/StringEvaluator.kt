package com.delitx.furnituremaster.data_models.dtos_string_recognition

import com.delitx.furnituremaster.data_models.generated.MultiLanguageFromStringDecoderLexer
import com.delitx.furnituremaster.data_models.generated.MultiLanguageFromStringDecoderParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

object StringEvaluator {
    fun evaluateMap(string: String): Map<String, String> {
        val inputStream = CharStreams.fromString(string)
        val grammarLexer = MultiLanguageFromStringDecoderLexer(inputStream)
        val tokens = CommonTokenStream(grammarLexer)
        val parser = MultiLanguageFromStringDecoderParser(tokens)
        val tree = parser.finalExpr()
        val visitor = MultiLanguageVisitor()
        visitor.visit(tree)
        return visitor.resultMap
    }
    fun evaluateList(string: String): List<Pair<String, String>> {
        val inputStream = CharStreams.fromString(string)
        val grammarLexer = MultiLanguageFromStringDecoderLexer(inputStream)
        val tokens = CommonTokenStream(grammarLexer)
        val parser = MultiLanguageFromStringDecoderParser(tokens)
        val tree = parser.finalExpr()
        val visitor = MultiLanguageVisitor()
        visitor.visit(tree)
        return visitor.resultList
    }
}
