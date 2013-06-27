package ast.exp;

import lexer.Info;

public class Lt extends T
{
  public T left;
  public T right;

  public Lt(T left, T right)
  {
    this.left = left;
    this.right = right;
  }
  
  public Lt(T left, T right, Info info)
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
