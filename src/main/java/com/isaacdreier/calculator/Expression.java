package com.isaacdreier.calculator;

import java.util.ArrayList;

public class Expression {
  private String[] tokens;
  private int firstIdx;
  private int lastIdx;
  private ArrayList<PartialExpression> children;

  public Expression(String[] tokens, int firstIdx, int lastIdx) {
    if (tokens.length <= 0)
      throw new IllegalArgumentException("tokens argument cannot be empty");
    if (firstIdx < 0 || firstIdx > tokens.length)
      throw new IllegalArgumentException("firstIdx is out of bounds of the tokens array");
    if (lastIdx < 0 || lastIdx > tokens.length || lastIdx < firstIdx)
      throw new IllegalArgumentException("lastIdx is not a valid index");
    this.tokens = tokens;
    this.firstIdx = firstIdx;
    this.lastIdx = lastIdx;
    this.children = null;
    splitExpression();
  }

  public ArrayList<PartialExpression> getChildren() {
    return children;
  }

  public int getFirstIdx() {
    return firstIdx;
  }

  public int getLastIdx() {
    return lastIdx;
  }

  static private boolean isOperator(String str) {
    if (str == null)
      return false;
    return (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("^"));
  }

  interface Comparitor {
    public boolean compare(int idx);
  }

  class IsAddSubtract implements Comparitor {
    public boolean compare(int idx) {
      String cur = tokens[idx];
      String prev = (idx > firstIdx) ? tokens[idx - 1] : null;
      if (isOperator(cur) && isOperator(prev) && cur.equals("+"))
        throw new IllegalStateException("Expression cannot have two operators back to back, except '-'");
      else if (isOperator(cur) && isOperator(prev) && cur.equals("-")) // treats '-' as a sign rather than an operator
        return false;
      return (cur.equals("+") || cur.equals("-"));
    }
  }

  class IsMultiplyDivide implements Comparitor {
    public boolean compare(int idx) {
      String cur = tokens[idx];
      String prev = (idx > 0) ? tokens[idx - 1] : null;
      if (isOperator(cur) && isOperator(prev) && !cur.equals("("))
        throw new IllegalStateException("Expression cannot have two operators back to back, except '-'");
      return (cur.equals("*") || cur.equals("/") || (!isOperator(prev) && cur.equals("(")));
    }
  }

  class IsExponent implements Comparitor {
    public boolean compare(int idx) {
      String cur = tokens[idx];
      String prev = (idx > firstIdx) ? tokens[idx] : null;
      if (isOperator(cur) && isOperator(prev) && cur.equals("+"))
        throw new IllegalStateException("Expression cannot have two operators back to back, except '-'");
      return (cur.equals("^"));
    }
  }

  private int skipParenthesis(int current) {
    int cur = current + 1;
    while (cur < lastIdx && !tokens[cur].equals(")")) {
      if (tokens[cur].equals("(")) {
        cur = skipParenthesis(cur);
        continue;
      }
      cur++;
    }
    return cur + 1;
  }

  /**
   * Finds the next operator that matches according to the Comparitor's compare
   * function
   * 
   * @param start the starting index
   * @param comp  a Comparitor whose compare function returns true when it matches
   *              the token at given index
   * @return the index of the next matching operator
   */
  private int nextOperator(int start, Comparitor comp) {
    int cur = start;
    while (cur < this.lastIdx && !comp.compare(cur)) {
      if (tokens[cur].equals("(")) {
        cur = this.skipParenthesis(cur);
      } else
        cur++;
    }
    return cur;
  }

  /**
   * Analyzes the expression to find out what the first applicable order of
   * operations
   * operators are and returns the comparitor class
   * 
   * @return the Comparitor for the operator level
   */

  private Comparitor getOperatorComparitor(int current) {
    int cur = current;
    Comparitor comp = new IsAddSubtract();
    int opIdx = nextOperator(cur, comp);
    if (opIdx < lastIdx)
      return comp;
    comp = new IsMultiplyDivide();
    opIdx = nextOperator(cur, comp);
    if (opIdx < lastIdx)
      return comp;
    comp = new IsExponent();
    opIdx = nextOperator(cur, comp);
    if (opIdx < lastIdx)
      return comp;
    return null;
  }

  /**
   * @returns the string represntation of the expression
   */
  public String toString() {
    String str = "";
    for (int i = firstIdx; i <= lastIdx; i++) {
      if (i > firstIdx)
        str += " ";
      str += tokens[i];
    }
    return str;
  }

  private void printSpace(int num) {
    for (int i = 0; i < num; i++)
      System.out.print("==");
  }

  public void _printTree(int level, String operation) {
    printSpace(level);
    if (operation != null)
      System.out.printf("(%s) %s\n", operation, toString());
    else
      System.out.printf("(+) %s\n", toString());
    if (children != null) {
      children.forEach(n -> {
        String op = n.operation;
        n.expression._printTree(level + 1, op);
      });
    }
  }

  public void printTree() {
    _printTree(0, null);
  }

  private int getEndOfExpression(int current, Comparitor comp) {
    int cur = current;
    int start = cur;
    int nextOpIdx = nextOperator(cur, comp);
    if (nextOpIdx == lastIdx) {
      if (isOperator(tokens[nextOpIdx])) {
        // the final token is an illegal operator
        throw new IllegalStateException("Cannot have trailing operator");
      }
      return nextOpIdx;
    }
    // TODO one operand operators like tan() and sin() need to be checked here
    if (nextOpIdx == start)
      throw new IllegalStateException("Expression ");

    return nextOpIdx - 1;
  }

  private void addPartial(String operator, int startIdx, int lastIdx) {
    Expression newExp = new Expression(this.tokens, startIdx, lastIdx);
    PartialExpression pExp = new PartialExpression(operator, newExp);
    children.add(pExp);
  }

  private void splitExpression() {
    if (tokens.length - 1 - firstIdx == 0)
      return; // There is only a single value and this.firstIdx is pointing to it.
    this.children = new ArrayList<>();
    int start = firstIdx;
    int curIdx = firstIdx;
    String operator = "+";
    Comparitor opComp = null;
    while (opComp == null) {
      curIdx = firstIdx;
      if (this.tokens[curIdx].equals("-")) {
        operator = "-";
        curIdx++;
      }
      opComp = this.getOperatorComparitor(curIdx); // order of operations level.
      if (opComp == null) { // if opComparitor returns null, Undetermined, the expression is either a value
                            // or in parenthesis
        if (tokens[firstIdx].equals("(")) {
          if (tokens[lastIdx].equals(")")) {
            firstIdx++;
            lastIdx--;
          }
        } else if (operator.equals("-") && tokens[curIdx].equals("(")) {
          addPartial(operator, curIdx, lastIdx);
          return;
        } else {
          children = null;
          return;
        }
      }
    }

    while (curIdx <= lastIdx) {
      start = curIdx;
      curIdx = getEndOfExpression(curIdx, opComp);
      if (tokens[curIdx].equals(")") && operator.equals("("))
        addPartial("*", start, curIdx - 1);
      else
        addPartial(operator, start, curIdx);
      if (curIdx == lastIdx)
        break;
      int opIdx = nextOperator(curIdx, opComp);
      operator = tokens[opIdx];
      if (operator.equals("("))
        curIdx++;
      curIdx = opIdx + 1;
    }
  }

  static private double operate(double rightOperand, String operator, double leftOperand) {
    if (operator.equals("+"))
      return rightOperand + leftOperand;
    if (operator.equals("-"))
      return rightOperand - leftOperand;
    if (operator.equals("*"))
      return rightOperand * leftOperand;
    if (operator.equals("/"))
      return rightOperand / leftOperand;
    if (operator.equals("^"))
      return Math.pow(rightOperand, leftOperand);
    return 0;
  }

  public double evaluate() {
    if (children == null && firstIdx -lastIdx ==0)
      return Double.parseDouble(tokens[firstIdx]);
    double value = 0;
    for (int i = 0; i < children.size(); i++) {
      PartialExpression child = children.get(i);
      value = operate(value, child.operation, child.expression.evaluate());
    }
    return value;
  }

}
