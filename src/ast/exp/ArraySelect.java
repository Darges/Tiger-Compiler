package ast.exp;

import lexer.Info;

public class ArraySelect extends T
{
  public T array;
  public T index;

  public ArraySelect(T array, T index)
  {
    this.array = array;
    this.index = index;
  }
  
  public ArraySelect(T array, T index, Info info)
  {
    this.array = array;
    this.index = index;
    this.pos = info;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
