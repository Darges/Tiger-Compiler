package ast.exp;

import lexer.Info;

public class Id extends T
{
  public String id; // name of the id
  public ast.type.T type; // type of the id
  public boolean isField; // whether or not this is a class field

  public Id(String id)
  {
    this.id = id;
    this.type = null;
    this.isField = false;
  }

  public Id(String id, ast.type.T type, boolean isField)
  {
    this.id = id;
    this.type = type;
    this.isField = isField;
  }
  
  public Id(String id, Info info)
  {
    this.id = id;
    this.type = null;
    this.isField = false;
    this.pos = info;
  }
  
  public Id(String id, ast.type.T type, boolean isField, Info info)
  {
    this.id = id;
    this.type = type;
    this.isField = isField;
    this.pos = info;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
