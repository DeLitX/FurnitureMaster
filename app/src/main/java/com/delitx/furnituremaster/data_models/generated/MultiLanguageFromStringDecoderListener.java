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
	 * Enter a parse tree produced by the {@code exprValueFinal}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#value}.
	 * @param ctx the parse tree
	 */
	void enterExprValueFinal(MultiLanguageFromStringDecoderParser.ExprValueFinalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprValueFinal}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#value}.
	 * @param ctx the parse tree
	 */
	void exitExprValueFinal(MultiLanguageFromStringDecoderParser.ExprValueFinalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprValueWord}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#value}.
	 * @param ctx the parse tree
	 */
	void enterExprValueWord(MultiLanguageFromStringDecoderParser.ExprValueWordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprValueWord}
	 * labeled alternative in {@link MultiLanguageFromStringDecoderParser#value}.
	 * @param ctx the parse tree
	 */
	void exitExprValueWord(MultiLanguageFromStringDecoderParser.ExprValueWordContext ctx);
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