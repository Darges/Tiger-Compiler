package ast.exp;

import lexer.Info;

public class Not extends T
{
  public T exp;

  public Not(T exp)
  {
    this.exp = exp;
  }
  
  public Not(T exp, Info info)
  {
    this.exp = exp;
    this.pos = info;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
