grammar MultiLanguageFromStringDecoder;
finalExpr:'{' pair (','  pair)*  '}' #exprFinal;
pair: '[' (value) ']' ' '* ':' ' '* '['(value)']' #exprPair;
value:finalExpr#exprValueFinal
|word #exprValueWord;
word:.*?;
CHAR:.;