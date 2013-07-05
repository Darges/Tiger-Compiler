package codegen.C.exp;

import lexer.Info;
import codegen.C.Visitor;

public class False extends T
{
  public False()
  {
	  
  }
  
  public False(Info info)
  {
	  this.pos = info;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
    return;
  }
}