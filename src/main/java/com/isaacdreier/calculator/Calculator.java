package com.isaacdreier.calculator;

import java.util.ArrayList;

public class Calculator {
  private ExpressionTokenizer tokenizer;

  public Calculator() {
    this.tokenizer = new ExpressionTokenizer();
  }

  private boolean isTokenOperator(String t) {
    char c = t.charAt(0);
    return (c == '+' || c == '-' || c == '*' || c == '/' || c == '^');
  }

  // private double _evaluate(ArrayList<String> tokens, int current) {
  //   ArrayList<String> sansParenthesis = 
  // }

  public double evaluate(String expression) {
    String[] tokens = tokenizer.getTokenList(expression);
    return 0;
    // return _evaluate(tokens, 0);
  }

  
}
