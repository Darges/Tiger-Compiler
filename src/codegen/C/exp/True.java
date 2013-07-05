package codegen.C.exp;

import codegen.C.Visitor;
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
  public void accept(Visitor v)
  {
    v.visit(this);
    return;
  }
}