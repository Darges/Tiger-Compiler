package ast.exp;

import lexer.Info;

public class NewIntArray extends T
{
  public T exp;

  public NewIntArray(T exp)
  {
    this.exp = exp;
  }
  
  public NewIntArray(T exp, Info info)
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
