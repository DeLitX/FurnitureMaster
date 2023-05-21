// Generated from D:/AndroidStudio/Projects/MaestroKitchen/app/src/main/java/com/delitx/maestrokitchen/pojo/string_recognition\MultiLanguageFromStringDecoder.g4 by ANTLR 4.9
package com.delitx.furnituremaster.data_models.generated;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link MultiLanguageFromStringDecoderVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class MultiLanguageFromStringDecoderBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements MultiLanguageFromStringDecoderVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExprFinal(MultiLanguageFromStringDecoderParser.ExprFinalContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitExprPair(MultiLanguageFromStringDecoderParser.ExprPairContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitWord(MultiLanguageFromStringDecoderParser.WordContext ctx) { return visitChildren(ctx); }
}