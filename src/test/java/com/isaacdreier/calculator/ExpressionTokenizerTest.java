package com.isaacdreier.calculator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class ExpressionTokenizerTest {
  

  @Test
  public void shouldParseExpressionWithoutWhitespace() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer();
    String[] tokens = tokenizer.getTokenList("23+548-19");
    assertEquals(0, tokens[0].compareTo("23"));
    assertEquals(0, tokens[1].compareTo("+"));
    assertEquals(0, tokens[2].compareTo("548"));
    assertEquals(0, tokens[3].compareTo("-"));
    assertEquals(0, tokens[4].compareTo("19"));
  }

  @Test
  public void shouldParseExpressionWithWhitespace() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer();
    String[] tokens = tokenizer.getTokenList("23 +548- 19");
    assertEquals(0, tokens[0].compareTo("23"));
    assertEquals(0, tokens[1].compareTo("+"));
    assertEquals(0, tokens[2].compareTo("548"));
    assertEquals(0, tokens[3].compareTo("-"));
    assertEquals(0, tokens[4].compareTo("19"));
  }

  @Test
  public void shouldHandleAllOperators() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer();
    String[] tokens = tokenizer.getTokenList("+-*/^()");
    assertEquals(0, tokens[0].compareTo("+"));
    assertEquals(0, tokens[1].compareTo("-"));
    assertEquals(0, tokens[2].compareTo("*"));
    assertEquals(0, tokens[3].compareTo("/"));
    assertEquals(0, tokens[4].compareTo("^"));
    assertEquals(0, tokens[5].compareTo("("));
    assertEquals(0, tokens[6].compareTo(")"));
  }

  @Test(expected = IllegalCharacterException.class)
  public void shouldThrowExceptionOnIllegalCharacters() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer();
    tokenizer.getTokenList("aba+324");
  }

  @Test //(expected = IllegalCharacterException.class)
  public void returnsZeroLengthArrayOnEmptyExpression() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer();
    String[] tokens = tokenizer.getTokenList("");
    assertEquals(0, tokens.length);
  }
}
