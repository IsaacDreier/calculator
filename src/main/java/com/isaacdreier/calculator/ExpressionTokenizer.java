package com.isaacdreier.calculator;

import java.util.ArrayList;

public class ExpressionTokenizer {
  private String exp;
  private ArrayList<String> tokens;

  public ExpressionTokenizer() {
    this.exp = null;
    this.tokens = null;
  }

  private boolean isTerm(char c) {
    return (c >= '0' && c <= '9' || c == '.');
  }

  private boolean isWhitespace(char c) {
    return (c == ' ' || c == '\t');
  }

  private boolean isOperator(char c) {
    return (c == '+' ||   c == '-' || c == '*' || c == '/' || c == '^' || c == '(' || c== ')');
  }

  private int getNextNonWhitespace(String exp,int i) {
    while(i < exp.length() && isWhitespace(exp.charAt(i)))
      i++;
    return i;
  }

  //Iterate over expression keeping track of start of term or operator, get substring at the end,
  //add substring to tokenList
  private String[] parseExpression() {
    tokens = new ArrayList<>();
    int beginTokenIdx = 0;
    int endTokenIdx = 0;
    //each iteration of this loop results in a seperate token
    for (int i = 0; i < exp.length(); i = getNextNonWhitespace(exp, endTokenIdx)) {
      beginTokenIdx = i;
      endTokenIdx = i;
      char c = exp.charAt(i);
      if (isTerm(c)) {
        while (true) {
          if (endTokenIdx >= exp.length() || !isTerm(exp.charAt(endTokenIdx))) {
            tokens.add(exp.substring(beginTokenIdx, endTokenIdx));
            break;
          }
          endTokenIdx++;
        }
      } else if (isOperator(c)) {
        tokens.add(String.valueOf(c));
        endTokenIdx++;
      } else {
        throw new IllegalCharacterException();
      }
    }
    String[] strTokens = new String[tokens.size()];
    for (int i = 0; i < tokens.size(); i++) {
      strTokens[i] = tokens.get(i);
    }
    return strTokens;
  }

  public String[] getTokenList(String expression) {
    this.exp = expression;
    return parseExpression();
  }
}
