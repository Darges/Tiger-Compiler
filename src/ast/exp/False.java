package ast.exp;

import lexer.Info;

public class False extends T
{
  public False(T left, T right)
  {
  }
  
  public False(T left, T right, Info info)
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
