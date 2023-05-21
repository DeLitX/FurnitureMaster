// Generated from D:/AndroidStudio/Projects/MaestroKitchen/app/src/main/java/com/delitx/maestrokitchen/pojo/string_recognition\MultiLanguageFromStringDecoder.g4 by ANTLR 4.9
package com.delitx.furnituremaster.data_models.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MultiLanguageFromStringDecoderParser}.
 */
public interface MultiLanguageFromStringDecoderListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code exprFinal}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#finalExpr}.
	 * @param ctx the parse tree
	 */
	void enterExprFinal(MultiLanguageFromStringDecoderParser.ExprFinalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprFinal}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#finalExpr}.
	 * @param ctx the parse tree
	 */
	void exitExprFinal(MultiLanguageFromStringDecoderParser.ExprFinalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprPair}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterExprPair(MultiLanguageFromStringDecoderParser.ExprPairContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprPair}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitExprPair(MultiLanguageFromStringDecoderParser.ExprPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link MultiLanguageFromStringDecoderParser#word}.
	 * @param ctx the parse tree
	 */
	void enterWord(MultiLanguageFromStringDecoderParser.WordContext ctx);
	/**
	 * Exit a parse tree produced by {@link MultiLanguageFromStringDecoderParser#word}.
	 * @param ctx the parse tree
	 */
	void exitWord(MultiLanguageFromStringDecoderParser.WordContext ctx);
}