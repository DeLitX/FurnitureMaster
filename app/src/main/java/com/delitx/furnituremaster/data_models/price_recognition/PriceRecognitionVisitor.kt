package com.delitx.furnituremaster.data_models.price_recognition

import com.delitx.furnituremaster.data_models.generated.PriceDecodeBaseVisitor
import com.delitx.furnituremaster.data_models.generated.PriceDecodeLexer
import com.delitx.furnituremaster.data_models.generated.PriceDecodeParser

class PriceRecognitionVisitor(val helper: RecogniseHelper) : PriceDecodeBaseVisitor<Double>() {

    override fun visitExprCalc(ctx: PriceDecodeParser.ExprCalcContext?): Double {
        return visit(ctx?.expression())
    }

    override fun visitMinusExpr(ctx: PriceDecodeParser.MinusExprContext?): Double {
        return -visit(ctx?.expression())
    }

    override fun visitLinkExpr(ctx: PriceDecodeParser.LinkExprContext?): Double {
        if (ctx == null) {
            return 0.0
        }
        val text = ctx.getChild(1).text
        return helper.getLinkValue(text.toInt())
    }

    override fun visitMultiplyExpr(ctx: PriceDecodeParser.MultiplyExprContext?): Double {
        return when (ctx?.operatorToken?.type) {
            PriceDecodeLexer.MULTIPLY -> visit(ctx.expression(0)) * visit(ctx.expression(1))
            PriceDecodeLexer.DIVIDE -> visit(ctx.expression(0)) / visit(ctx.expression(1))
            else -> 0.0
        }
    }

    override fun visitAdditiveExpr(ctx: PriceDecodeParser.AdditiveExprContext?): Double {
        return if (ctx?.operatorToken?.type == PriceDecodeLexer.ADD) {
            visit(ctx.expression(0)) + visit(ctx.expression(1))
        } else {
            visit(ctx?.expression(0)) - visit(ctx?.expression(1))
        }
    }

    override fun visitParenthesesExpr(ctx: PriceDecodeParser.ParenthesesExprContext?): Double {
        return visit(ctx?.expression())
    }

    override fun visitNumberExpr(ctx: PriceDecodeParser.NumberExprContext?): Double {
        return ctx?.text?.toDouble() ?: 0.0
    }
    interface RecogniseHelper {
        fun getLinkValue(link: Int): Double
    }
}
