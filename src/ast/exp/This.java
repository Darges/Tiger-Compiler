package ast.exp;

import lexer.Info;

public class This extends T
{
  public This()
  {
  }
  
  public This(Info info)
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
