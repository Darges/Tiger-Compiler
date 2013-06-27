package ast.stm;

public class While extends T
{
  public ast.exp.T condition;
  public java.util.LinkedList<ast.stm.T> body;

  public While(ast.exp.T condition, java.util.LinkedList<ast.stm.T> body)
  {
    this.condition = condition;
    this.body = body;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
  }
}
