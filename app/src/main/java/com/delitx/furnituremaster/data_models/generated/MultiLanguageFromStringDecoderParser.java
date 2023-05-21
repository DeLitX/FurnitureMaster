package com.delitx.furnituremaster.data_models.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MultiLanguageFromStringDecoderParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, CHAR=8;
	public static final int
		RULE_finalExpr = 0, RULE_pair = 1, RULE_value = 2, RULE_word = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"finalExpr", "pair", "value", "word"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "','", "'}'", "'['", "']'", "' '", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "CHAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MultiLanguageFromStringDecoder.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MultiLanguageFromStringDecoderParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class FinalExprContext extends ParserRuleContext {
		public FinalExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_finalExpr; }
	 
		public FinalExprContext() { }
		public void copyFrom(FinalExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprFinalContext extends FinalExprContext {
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
		}
		public ExprFinalContext(FinalExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).enterExprFinal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).exitExprFinal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MultiLanguageFromStringDecoderVisitor ) return ((MultiLanguageFromStringDecoderVisitor<? extends T>)visitor).visitExprFinal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FinalExprContext finalExpr() throws RecognitionException {
		FinalExprContext _localctx = new FinalExprContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_finalExpr);
		int _la;
		try {
			_localctx = new ExprFinalContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			match(T__0);
			setState(9);
			pair();
			setState(14);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(10);
				match(T__1);
				setState(11);
				pair();
				}
				}
				setState(16);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(17);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairContext extends ParserRuleContext {
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
	 
		public PairContext() { }
		public void copyFrom(PairContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprPairContext extends PairContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public ExprPairContext(PairContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).enterExprPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).exitExprPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MultiLanguageFromStringDecoderVisitor ) return ((MultiLanguageFromStringDecoderVisitor<? extends T>)visitor).visitExprPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_pair);
		int _la;
		try {
			_localctx = new ExprPairContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(19);
			match(T__3);
			{
			setState(20);
			value();
			}
			setState(21);
			match(T__4);
			setState(25);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(22);
				match(T__5);
				}
				}
				setState(27);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(28);
			match(T__6);
			setState(32);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(29);
				match(T__5);
				}
				}
				setState(34);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(35);
			match(T__3);
			{
			setState(36);
			value();
			}
			setState(37);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
	 
		public ValueContext() { }
		public void copyFrom(ValueContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprValueFinalContext extends ValueContext {
		public FinalExprContext finalExpr() {
			return getRuleContext(FinalExprContext.class,0);
		}
		public ExprValueFinalContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).enterExprValueFinal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).exitExprValueFinal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MultiLanguageFromStringDecoderVisitor ) return ((MultiLanguageFromStringDecoderVisitor<? extends T>)visitor).visitExprValueFinal(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprValueWordContext extends ValueContext {
		public WordContext word() {
			return getRuleContext(WordContext.class,0);
		}
		public ExprValueWordContext(ValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).enterExprValueWord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).exitExprValueWord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MultiLanguageFromStringDecoderVisitor ) return ((MultiLanguageFromStringDecoderVisitor<? extends T>)visitor).visitExprValueWord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_value);
		try {
			setState(41);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				_localctx = new ExprValueFinalContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(39);
				finalExpr();
				}
				break;
			case 2:
				_localctx = new ExprValueWordContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(40);
				word();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WordContext extends ParserRuleContext {
		public WordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_word; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).enterWord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MultiLanguageFromStringDecoderListener ) ((MultiLanguageFromStringDecoderListener)listener).exitWord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MultiLanguageFromStringDecoderVisitor ) return ((MultiLanguageFromStringDecoderVisitor<? extends T>)visitor).visitWord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WordContext word() throws RecognitionException {
		WordContext _localctx = new WordContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_word);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(43);
					matchWildcard();
					}
					} 
				}
				setState(48);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\n\64\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\7\2\17\n\2\f\2\16\2\22\13\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\3\7\3\32\n\3\f\3\16\3\35\13\3\3\3\3\3\7\3!\n\3\f\3\16"+
		"\3$\13\3\3\3\3\3\3\3\3\3\3\4\3\4\5\4,\n\4\3\5\7\5/\n\5\f\5\16\5\62\13"+
		"\5\3\5\3\60\2\6\2\4\6\b\2\2\2\64\2\n\3\2\2\2\4\25\3\2\2\2\6+\3\2\2\2\b"+
		"\60\3\2\2\2\n\13\7\3\2\2\13\20\5\4\3\2\f\r\7\4\2\2\r\17\5\4\3\2\16\f\3"+
		"\2\2\2\17\22\3\2\2\2\20\16\3\2\2\2\20\21\3\2\2\2\21\23\3\2\2\2\22\20\3"+
		"\2\2\2\23\24\7\5\2\2\24\3\3\2\2\2\25\26\7\6\2\2\26\27\5\6\4\2\27\33\7"+
		"\7\2\2\30\32\7\b\2\2\31\30\3\2\2\2\32\35\3\2\2\2\33\31\3\2\2\2\33\34\3"+
		"\2\2\2\34\36\3\2\2\2\35\33\3\2\2\2\36\"\7\t\2\2\37!\7\b\2\2 \37\3\2\2"+
		"\2!$\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#%\3\2\2\2$\"\3\2\2\2%&\7\6\2\2&\'\5"+
		"\6\4\2\'(\7\7\2\2(\5\3\2\2\2),\5\2\2\2*,\5\b\5\2+)\3\2\2\2+*\3\2\2\2,"+
		"\7\3\2\2\2-/\13\2\2\2.-\3\2\2\2/\62\3\2\2\2\60\61\3\2\2\2\60.\3\2\2\2"+
		"\61\t\3\2\2\2\62\60\3\2\2\2\7\20\33\"+\60";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}