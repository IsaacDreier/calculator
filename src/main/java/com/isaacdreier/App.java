package com.isaacdreier;

import com.isaacdreier.calculator.Expression;
import com.isaacdreier.calculator.ExpressionTokenizer;

public class App 
{
    public static void main( String[] args )
    {
      try {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer();
        String[] tokens = tokenizer.getTokenList("32^2*4+6(523+45)");
        Expression exp = new Expression(tokens, 0, tokens.length - 1);
        System.out.println(exp.evaluate());

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
}
