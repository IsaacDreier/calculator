package com.isaacdreier.calculator;

public class PartialExpression {
  public String operation;
  public Expression expression;

  public PartialExpression(String operation, Expression expression) {
    this.operation = operation;
    this.expression = expression;
  }
  
}
