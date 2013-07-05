package codegen.C.type;

import codegen.C.Visitor;

public class Boolean extends T
{
  public Boolean()
  {
  }

  @Override
  public String toString()
  {
    return "@boolean";
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
