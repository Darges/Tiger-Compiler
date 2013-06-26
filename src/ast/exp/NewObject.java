package ast.exp;

import lexer.Info;

public class NewObject extends T
{
  public String id;

  public NewObject(String id)
  {
    this.id = id;
  }
  
  public NewObject(String id, Info info)
  {
    this.id = id;
    this.pos = info;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
