package com.isaacdreier.calculator;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;

import org.junit.Test;

public class ExpressionTest {
  @Test
  public void shouldParseSingleValue() {
    String[] tokens = { "23" };
    Expression exp = new Expression(tokens, 0, 0);
    assertEquals(exp.getFirstIdx(), 0);
    assertEquals(exp.getLastIdx(), 0);
  }

  @Test
  public void shouldParseSingleOperator() {
    String[] tokens = { "23", "+", "23" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    ArrayList<PartialExpression> children = exp.getChildren();
    assertEquals(2, children.size());
    assertEquals("+", children.get(0).operation);
    assertEquals(null, children.get(0).expression.getChildren());
    assertEquals("+", children.get(1).operation);
    assertEquals(null, children.get(1).expression.getChildren());
  }

  @Test
  public void toStringShouldRepresentExpression() {
    String[] tokens = { "23", "+", "23" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    assertEquals("23 + 23", exp.toString());
  }

  @Test
  public void shouldParseValueWithSign() {
    String[] tokens = { "23", "+", "-", "23" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    ArrayList<PartialExpression> children = exp.getChildren();
    assertEquals(2, children.size());
    assertEquals("+", children.get(0).operation);
    exp.printTree();
    assertEquals(null, children.get(0).expression.getChildren());
    assertEquals("+", children.get(1).operation);
    assertEquals("-", children.get(1).expression.getChildren().get(0).operation);
    assertThat(children.get(1).expression.getChildren(), is(notNullValue()));
  }

  @Test
  public void shouldParseMultipleOperators() {
    String[] tokens = { "23", "+", "-", "23", "*", "234" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    ArrayList<PartialExpression> children = exp.getChildren();
    assertEquals(2, children.size());
    assertEquals(2, children.get(1).expression.getFirstIdx());
    assertEquals(5, children.get(1).expression.getLastIdx());
    assertEquals(2, children.get(1).expression.getChildren().size());
  }

  @Test
  public void shouldParseParenthesis() {
    String[] tokens = { "23", "+", "-", "(", "25", "*", "234", ")" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    ArrayList<PartialExpression> children = exp.getChildren();
    assertEquals(2, children.size());
    assertEquals(2, children.get(1).expression.getFirstIdx());
    assertEquals(1, children.get(1).expression.getChildren().size());
    assertEquals(2, children.get(1).expression.getChildren().get(0).expression.getChildren().size());
  }

  @Test
  public void evalutesSum() {
    String[] tokens = { "23", "+", "3" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    assertEquals(26, exp.evaluate(), 0.0001);
  }

  @Test
  public void evalutesDifference() {
    String[] tokens = { "23", "-", "3" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    assertEquals(20, exp.evaluate(), 0.0001);
  }

  @Test
  public void evalutesProduct() {
    String[] tokens = { "23", "*", "3" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    assertEquals(69, exp.evaluate(), 0.0001);
  }

  @Test
  public void evalutesFraction() {
    String[] tokens = { "24", "/", "3" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    assertEquals(8, exp.evaluate(), 0.0001);
  }

  @Test
  public void evalutesExponent() {
    String[] tokens = { "2", "^", "3" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    assertEquals(8, exp.evaluate(), 0.0001);
  }

  @Test
  public void evalutesComplexExression() {
    String[] tokens = { "23", "+", "-", "(", "25", "*", "234", ")" };
    Expression exp = new Expression(tokens, 0, tokens.length - 1);
    assertEquals(-5827, exp.evaluate(), 0.0001);
  }

}
