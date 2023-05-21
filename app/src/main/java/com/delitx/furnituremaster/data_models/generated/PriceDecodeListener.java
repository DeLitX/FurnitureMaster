package com.delitx.furnituremaster.data_models.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PriceDecodeParser}.
 */
public interface PriceDecodeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code exprCalc}
	 * labeled alternative in {@link PriceDecodeParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprCalc(PriceDecodeParser.ExprCalcContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprCalc}
	 * labeled alternative in {@link PriceDecodeParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprCalc(PriceDecodeParser.ExprCalcContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNumberExpr(PriceDecodeParser.NumberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNumberExpr(PriceDecodeParser.NumberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code linkExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLinkExpr(PriceDecodeParser.LinkExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code linkExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLinkExpr(PriceDecodeParser.LinkExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multiplyExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplyExpr(PriceDecodeParser.MultiplyExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multiplyExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplyExpr(PriceDecodeParser.MultiplyExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code additiveExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpr(PriceDecodeParser.AdditiveExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code additiveExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpr(PriceDecodeParser.AdditiveExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesesExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesesExpr(PriceDecodeParser.ParenthesesExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesesExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesesExpr(PriceDecodeParser.ParenthesesExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minusExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpr(PriceDecodeParser.MinusExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minusExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpr(PriceDecodeParser.MinusExprContext ctx);
}