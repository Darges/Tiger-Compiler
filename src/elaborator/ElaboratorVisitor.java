package elaborator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lexer.Info;

public class ElaboratorVisitor implements ast.Visitor
{
  public ClassTable classTable; // symbol table for class
  public MethodTable methodTable; // symbol table for each method
  public String currentClass; // the class name being elaborated
  public ast.type.T type; // type of the expression being elaborated
//  public java.util.Hashtable<, int[]> table;
  public Set<String> parameters = new HashSet<String>();
  public ElaboratorVisitor()
  {
    this.classTable = new ClassTable();
    this.methodTable = new MethodTable();
    this.currentClass = null;
    this.type = null;
  }

  private void error(String s)
  {
    //System.out.println("type mismatch");
	  System.out.println(s);
	  //System.exit(1);
  }
  
  private void error(String s ,Info exp)
  {
    //System.out.println("type mismatch");
	  System.out.println("Semantic error "+exp.lineNum+":"+exp.column+": "+s);
	  //System.exit(1);
  }

  // /////////////////////////////////////////////////////
  // expressions
  @Override
  public void visit(ast.exp.Add e)
  {
	    e.left.accept(this);
	    ast.type.T leftty = this.type;
	    e.right.accept(this);
	    if (!this.type.toString().equals(leftty.toString()))
	      error("type mismatch around + left is "+leftty.toString() + " right is "+this.type.toString(), e.pos);
	    this.type = new ast.type.Int();
	    return;
  }

  @Override
  public void visit(ast.exp.And e)
  {
	  //lab modify
	    e.left.accept(this);
	    ast.type.T leftty = this.type;
	    e.right.accept(this);
	    if (!this.type.toString().equals(leftty.toString()))
	    	error("type mismatch around && left is "+leftty.toString() + " right is "+this.type.toString(), e.pos);
	    this.type = new ast.type.Boolean();
	    return;
  }

  @Override
  public void visit(ast.exp.ArraySelect e)
  {
	  //lab modify
	  e.index.accept(this);	  
	  if(!this.type.toString().equals("@int"))
	     error(e.array.toString() + "\'s index must be @int", e.pos);
	  e.array.accept(this);
	  this.type = new ast.type.Int();
	  
  }

  @Override
  public void visit(ast.exp.Call e)
  {
    ast.type.T leftty;
    ast.type.Class ty = null;

    e.exp.accept(this);
    leftty = this.type;
    if (leftty instanceof ast.type.Class) {
      ty = (ast.type.Class) leftty;
      e.type = ty.id;
    } else
      error(e.exp+"is not class");
    MethodType mty = this.classTable.getm(ty.id, e.id);
    java.util.LinkedList<ast.type.T> argsty = new java.util.LinkedList<ast.type.T>();
    if(e.args!=null){
     for (ast.exp.T a : e.args) {
       a.accept(this);
       argsty.addLast(this.type);
     }
    }
    if (mty.argsType.size() != argsty.size())
      error(" args'size is not correct");
    for (int i = 0; i < argsty.size(); i++) {
      ast.dec.Dec dec = (ast.dec.Dec) mty.argsType.get(i);
      if (dec.type.toString().equals(argsty.get(i).toString()))
        ;
      else
      {
    	  ClassBinding temp = classTable.get(argsty.get(i).toString());
    	  ClassBinding prev = null;
    	  
    	  while( (temp.extendss!=null) && !dec.type.toString().equals(temp) )// 查找其基类
    	  {
    		  prev = temp;
    		  temp = classTable.get(temp.extendss);
    	  }	  
    	  
    	  if(!dec.type.toString().equals(prev.extendss))
    	  {    		  
    		  error(dec.type.toString()+" and argsType "+argsty.get(i).toString()+" type mismatch", e.pos);
    		  //classTable.get(argsty.get(i).toString()).
    	  }    	 
      }
       
    }
    this.type = mty.retType;
    e.at = argsty;
    e.rt = this.type;
    return;
  }

  @Override
  public void visit(ast.exp.False e)
  {
	 //lab modify
	  this.type = new ast.type.Boolean();
	  return;
  }

  @Override
  public void visit(ast.exp.Id e)
  {
	
    // first look up the id in method table
    ast.type.T type = this.methodTable.get(e.id);
    // if search failed, then s.id must be a class field.
    if (type == null) {
      type = this.classTable.get(this.currentClass, e.id);
      // mark this id as a field id, this fact will be
      // useful in later phase.
      e.isField = true;
    }
    if (type == null)
      error(e.id + "is not define!", e.pos);
    this.type = type;
    // record this type on this node for future use.
    e.type = type;
    return;
  }

  @Override
  public void visit(ast.exp.Length e)
  {
	  e.array.accept(this);	  
	  if(!this.type.toString().equals("@int[]"))
	     error("only the IntArray have the length", e.pos);
	  else
		 this.type = new ast.type.Int(); 
  }

  @Override
  public void visit(ast.exp.Lt e)
  {
    e.left.accept(this);
    ast.type.T ty = this.type;
    e.right.accept(this);
    if (!this.type.toString().equals(ty.toString()))
      error(" '<' left and right type mismatch", e.pos);
    this.type = new ast.type.Boolean();
    return;
  }

  @Override
  public void visit(ast.exp.NewIntArray e)
  {
	  //lab modify
	 
	  e.exp.accept(this);  
	  this.type = new ast.type.IntArray();	  
	  return;
  }

  @Override
  public void visit(ast.exp.NewObject e)
  {
    this.type = new ast.type.Class(e.id);
    return;
  }

  @Override
  public void visit(ast.exp.Not e)
  {
	  //lab modify
	  e.exp.accept(this);	  
	  if(! this.type.toString().equals("@boolean"))
		  error( "'!' right is not boolean", e.pos);
	  this.type = new ast.type.Boolean();
	  return;
  }

  @Override
  public void visit(ast.exp.Num e)
  {
    this.type = new ast.type.Int();
    return;
  }

  @Override
  public void visit(ast.exp.Sub e)
  {
    e.left.accept(this);
    ast.type.T leftty = this.type;
    e.right.accept(this);
    if (!this.type.toString().equals(leftty.toString()))
    	error("type mismatch around - left is "+leftty.toString() + " right is "+this.type.toString(), e.pos);
    this.type = new ast.type.Int();
    return;
  }

  @Override
  public void visit(ast.exp.This e)
  {
    this.type = new ast.type.Class(this.currentClass);
    return;
  }

  @Override
  public void visit(ast.exp.Times e)
  {
    e.left.accept(this);
    ast.type.T leftty = this.type;
    e.right.accept(this);
    if (!this.type.toString().equals(leftty.toString()))
    	error("type mismatch around * left is "+leftty.toString() + " right is "+this.type.toString(), e.pos);
    this.type = new ast.type.Int();
    return;
  }

  @Override
  public void visit(ast.exp.True e)
  {
	  //lab modify
	  this.type = new ast.type.Boolean();
  }

  // statements
  @Override
  public void visit(ast.stm.Assign s)
  {
	parameters.remove(s.id);
    // first look up the id in method table
    ast.type.T type = this.methodTable.get(s.id);
    // if search failed, then s.id must
    if (type == null)
      type = this.classTable.get(this.currentClass, s.id);
    if (type == null)
      error(s.id+" is not definited!", s.exp.pos);
    s.type = type;
    s.exp.accept(this);    
    if(!this.type.toString().equals(type.toString()))
    	error(s.id+" = "+s.type +" type mismatch ", s.exp.pos);//类型不匹配
    return;
  }

  @Override
  public void visit(ast.stm.AssignArray s)
  {
	  parameters.remove(s.id);
	  //lab modify ??
	  ast.type.T type = this.methodTable.get(s.id);
	  if(type == null)
		  type = this.classTable.get(this.currentClass, s.id);
	  if(type == null)
		  error(s.id+" is not definited !", s.exp.pos);
	  if(type.toString().equals("@int[]"))
		  type=new ast.type.Int();
	  else
		  ;//??
	  s.exp.accept(this);
	  s.index.accept(this);
	  
	  if(!this.type.toString().equals(type.toString()))
		  error("AssignArray left and right type mismatch", s.exp.pos);
  }

  @Override
  public void visit(ast.stm.Block s)
  {
	  //lab modify
	  for (ast.stm.T t : s.stms)
	  {
	      t.accept(this);
	  }
  }

  @Override
  public void visit(ast.stm.If s)
  {
    s.condition.accept(this);
    if (!this.type.toString().equals("@boolean"))
      error("If's condition is not boolean", s.condition.pos);
    s.thenn.accept(this);
    s.elsee.accept(this);
    return;
  }

  @Override
  public void visit(ast.stm.Print s)
  {
    s.exp.accept(this);
    if (!this.type.toString().equals("@int"))
      error("Print's exp id not int", s.exp.pos);
    return;
  }

  @Override
  public void visit(ast.stm.While s)
  {
	  //lab modify
	  s.condition.accept(this);
	  if(!this.type.toString().equals("@boolean"))
		  error("While's condition is not boolean", s.condition.pos);
	  for (ast.stm.T t : s.body)
	  {
	      t.accept(this);
	  }
	  return;	  
  }

  // type
  @Override
  public void visit(ast.type.Boolean t)
  {	  
	  this.type = new ast.type.Boolean();
  }

  @Override
  public void visit(ast.type.Class t)
  {
	  //modify
	  this.type = new ast.type.Class(t.id);
	  
  }

  @Override
  public void visit(ast.type.Int t)
  {
	//modify
	this.type = new ast.type.Int();
    //System.out.println(t.toString());
  }

  @Override
  public void visit(ast.type.IntArray t)
  {
	  
	  this.type = new ast.type.IntArray();
  }

  // dec
  @Override
  public void visit(ast.dec.Dec d)
  {	  
	 // parameters.add();
	  if(d.type!=null)
	     d.type.accept(this); 

  }

  // method
  @Override
  public void visit(ast.method.Method m)
  {
    // construct the method table
    this.methodTable.put(m.formals, m.locals);
    
    if (control.Control.elabMethodTable)
       this.methodTable.dump();
    if(!parameters.isEmpty())
       parameters.clear();//
    //parameters.add(m.formals);
//    for (ast.dec.T s : m.formals)
//    {    
//    	parameters.add(((ast.dec.Dec)s).id);
//    }
    for (ast.dec.T s : m.locals)
    {
    	parameters.add(((ast.dec.Dec)s).id);
    }    	
    for (ast.stm.T s : m.stms)
      s.accept(this);
    m.retExp.accept(this);
    if(!parameters.isEmpty())
    {
    	Iterator<String> i = parameters.iterator();//先迭代出来  
	    while(i.hasNext()){//遍历  
	    	
	        System.out.println("Warning: "+i.next()+" have not been assigned value");  
	    }  
    }
    this.methodTable.dump();  //lab 2 modify
  
    return;
  }

  // class
  @Override
  public void visit(ast.classs.Class c)
  {
    this.currentClass = c.id;
    
    for (ast.method.T m : c.methods) {
      m.accept(this);      
    }
    
    return;
  }

  // main class
  @Override
  public void visit(ast.mainClass.MainClass c)
  {
    this.currentClass = c.id;
    // "main" has an argument "arg" of type "String[]", but
    // one has no chance to use it. So it's safe to skip it...

    c.stm.accept(this);
    return;
  }

  // ////////////////////////////////////////////////////////
  // step 1: build class table
  // class table for Main class
  private void buildMainClass(ast.mainClass.MainClass main)
  {
    this.classTable.put(main.id, new ClassBinding(null));
  }

  // class table for normal classes
  private void buildClass(ast.classs.Class c)
  {
    this.classTable.put(c.id, new ClassBinding(c.extendss));
    
    for (ast.dec.T dec : c.decs) {
      ast.dec.Dec d = (ast.dec.Dec) dec;
      this.classTable.put(c.id, d.id, d.type);
    }
    for (ast.method.T method : c.methods) {
      ast.method.Method m = (ast.method.Method) method;
      
      this.classTable.put(c.id, m.id, new MethodType(m.retType, m.formals));
    }
    
  }

  // step 1: end
  // ///////////////////////////////////////////////////

  // program
  @Override
  public void visit(ast.program.Program p)
  {
    // ////////////////////////////////////////////////
    // step 1: build a symbol table for class (the class table)
    // a class table is a mapping from class names to class bindings
    // classTable: className -> ClassBinding{extends, fields, methods}
    buildMainClass((ast.mainClass.MainClass) p.mainClass);
    for (ast.classs.T c : p.classes) {
      buildClass((ast.classs.Class) c);
    }

    // we can double check that the class table is OK!
    if (control.Control.elabClassTable) {
      this.classTable.dump();
    }

    // ////////////////////////////////////////////////
    // step 2: elaborate each class in turn, under the class table
    // built above.
    p.mainClass.accept(this);
    for (ast.classs.T c : p.classes) {
      c.accept(this);      
    }

  }
}
