// Generated from D:/AndroidStudio/Projects/MaestroKitchen/app/src/main/java/com/delitx/maestrokitchen/pojo/string_recognition\MultiLanguageFromStringDecoder.g4 by ANTLR 4.9
package com.delitx.furnituremaster.data_models.dtos_string_recognition;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MultiLanguageFromStringDecoderParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MultiLanguageFromStringDecoderVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code exprFinal}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#finalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprFinal(MultiLanguageFromStringDecoderParser.ExprFinalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprPair}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPair(MultiLanguageFromStringDecoderParser.ExprPairContext ctx);
	/**
	 * Visit a parse tree produced by {@link MultiLanguageFromStringDecoderParser#word}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWord(MultiLanguageFromStringDecoderParser.WordContext ctx);
}