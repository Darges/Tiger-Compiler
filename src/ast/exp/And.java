package ast.exp;

import lexer.Info;

public class And extends T
{
  public T left;
  public T right;

  public And(T left, T right)
  {
    this.left = left;
    this.right = right;
  }

  public And(T left, T right, Info info)
  {
    this.left = left;
    this.right = right;
    this.pos = info;
  }
  
  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
