package ast.exp;

import lexer.Info;

public class Call extends T
{
  public T exp;
  public String id;
  public java.util.LinkedList<T> args;
  public String type; // type of first field "exp"
  public java.util.LinkedList<ast.type.T> at; // arg's type
  public ast.type.T rt;

  public Call(T exp, String id, java.util.LinkedList<T> args)
  {
    this.exp = exp;
    this.id = id;
    this.args = args;
  }
  
  public Call(T exp, String id, java.util.LinkedList<T> args, Info info)
  {
    this.exp = exp;
    this.id = id;
    this.args = args;
    this.type = null;
    this.pos = info;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
