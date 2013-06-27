package ast.exp;

import lexer.Info;

public class Length extends T
{
  public T array;

  public Length(T array)
  {
    this.array = array;
  }
  
  public Length(T array, Info info)
  {
    this.array = array;
    this.pos = info;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
