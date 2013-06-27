package ast.exp;

import lexer.Info;

public class Num extends T
{
  public int num;

  public Num(int num)
  {
    this.num = num;
  }
  
  public Num(int num, Info info)
  {
    this.num = num;
    this.pos = info;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
