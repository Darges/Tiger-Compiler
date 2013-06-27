package ast.exp;

import lexer.Info;

public class True extends T
{
  public True()
  {
	  
  }
  
  public True(Info info)
  {
	  this.pos = info;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
