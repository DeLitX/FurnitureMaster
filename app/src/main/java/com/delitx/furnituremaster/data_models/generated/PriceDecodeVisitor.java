package com.delitx.furnituremaster.data_models.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PriceDecodeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PriceDecodeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code exprCalc}
	 * labeled alternative in {@link PriceDecodeParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCalc(PriceDecodeParser.ExprCalcContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberExpr(PriceDecodeParser.NumberExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code linkExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkExpr(PriceDecodeParser.LinkExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiplyExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplyExpr(PriceDecodeParser.MultiplyExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code additiveExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(PriceDecodeParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesesExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesesExpr(PriceDecodeParser.ParenthesesExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code minusExpr}
	 * labeled alternative in {@link PriceDecodeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinusExpr(PriceDecodeParser.MinusExprContext ctx);
}