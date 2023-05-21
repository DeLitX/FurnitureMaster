grammar PriceDecode;
expr:
expression EOF #exprCalc;
expression
:
operatorToken='(' expression ')' #parenthesesExpr
|SUBTRACT expression #minusExpr
|expression operatorToken=(MULTIPLY|DIVIDE) expression #multiplyExpr
|expression operatorToken=(ADD|SUBTRACT) expression #additiveExpr
|NUMBER_DOUBLE #numberExpr
|'$' NUMBER_DOUBLE #linkExpr;

NUMBER_DOUBLE:'0'..'9'+ ('.' '0'..'9'+)?;
ADD:'+';
SUBTRACT:'-';
MULTIPLY:'*';
DIVIDE:'/';
