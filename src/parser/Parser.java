package parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

import ast.exp.And;
import ast.exp.T;

import lexer.Info;
import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;

public class Parser
{
  private static final ErrorType TYPE_SKIP = null;
  
  Lexer lexer;
  Token current;
  String filename;
  BufferedReader tempfstream;
  Stack<Token>  checkStack = new Stack<Token>();
  Token TokenStack[] = new Token[2]; 
  int stackNum=0;
  int moveBack=0;
  
  public enum ErrorType {
      TYPE_UNEXPECT,
      TYPE_AFTER,    
      TYPE_SKIP;
  }
  public Parser(String fname, java.io.InputStream fstream)
  {
    lexer = new Lexer(fname, fstream);
    filename = fname;
    current = lexer.nextToken();    
  }

  // /////////////////////////////////////////////
  // utility methods to connect the lexer
  // and the parser.

  private Token advance()
  {

	if(checkStack.isEmpty())
	{
		current = lexer.nextToken();	
		TokenStack[1] = TokenStack[0];
		TokenStack[0] = current; 
		return TokenStack[1];	 
	}
	else
	{
		Token temp =new Token(current);
		current = checkStack.pop();
		return temp;
	}
	     
  }
  
  private void moveBackAct()
  {
	  while(moveBack>0)
	  {
		   checkStack.push(current);
		   current = TokenStack[1];
		   TokenStack[1] = TokenStack[0];
		   moveBack--;
	  }
  }

  private String eatToken(Kind kind)
  {
	String name=null;
    if (kind == current.kind)
    {
       name = current.lexeme;
       advance();      
    }
    else {
      System.out.print(filename+".c:"+current.TokenInfo.getLineNum()+":"+current.TokenInfo.getColumn()+": error: ");
      System.out.print("Expects: " + kind.toString());
      System.out.println(" But got: " + current.kind.toString());      
      //error(ErrorType.TYPE_UNEXPECT, current, 1);
      //advance();
      //System.exit(1);
    }
    return name;
    
  }

  private void error()
  {
    //System.out.println("Syntax error: compilation aborting...\n");
    while(current.kind != Kind.TOKEN_SEMI)
    {
        advance();
    }
    //System.exit(1);
   
    return;
  }
  
  private void error(ErrorType type, Token TokenCur, int SkipNum , ArrayList<Kind> follow)
  {
	  switch(type)
	  {
		  case TYPE_UNEXPECT:	
			   while( SkipNum-- > 0 )
		       {
		    	   advance();
		       }
			   break;
		  case TYPE_AFTER:
		       System.out.println("Unexpected symbol after " + TokenCur.toString());
		       while( SkipNum-- > 0 )
		       {
		    	   advance();
		       }
		       break;
		  case TYPE_SKIP:
			  System.out.print(filename+".c:"+current.TokenInfo.getLineNum()+":"+current.TokenInfo.getColumn()+": error: ");
		      //System.out.print("Expects: " + kind.toString());
		      //System.out.println(" But got: " + current.kind.toString());  
			  System.out.println("Unexpected symbol around \" " + TokenCur.KindToString() + " \"");
			   while(!follow.contains(current.kind))
			   {
				   advance();
			   }			 
			   break;
	      default:break;	  
	  }
    //System.out.println("Syntax error: compilation aborting...\n");
    //System.exit(1);
    return;
  }

  // ////////////////////////////////////////////////////////////
  // below are method for parsing.

  // A bunch of parsing methods to parse expressions. The messy
  // parts are to deal with precedence and associativity.

  // ExpList -> Exp ExpRest*
  // ->
  // ExpRest -> , Exp
  private java.util.LinkedList<ast.exp.T> parseExpList()
  {
	java.util.LinkedList<ast.exp.T> expList = new java.util.LinkedList<ast.exp.T>();
	ast.exp.T temp;
    if (current.kind == Kind.TOKEN_RPAREN)
      return null;
    temp = parseExp();
    expList.add(temp);

    while (current.kind == Kind.TOKEN_COMMER) {
      advance();
      temp = parseExp();     
      expList.add(temp);
    }
    return expList;
  }

  // AtomExp -> (exp)
  // -> INTEGER_LITERAL
  // -> true
  // -> false
  // -> this
  // -> id
  // -> new int [exp]
  // -> new id ()
  
  ArrayList<Kind> AtomFollow = new ArrayList<Kind>(); 
  private ast.exp.T parseAtomExp()
  {
	AtomFollow.add(Kind.TOKEN_RPAREN);
	AtomFollow.add(Kind.TOKEN_LBRACK);
	Info temp = current.TokenInfo;
    switch (current.kind) {
    case TOKEN_LPAREN:
      advance();
      ast.exp.T tempMidExp = parseExp();
      eatToken(Kind.TOKEN_RPAREN);
      return tempMidExp;
    case TOKEN_NUM:
      int number;      
      number = Integer.parseInt(current.lexeme);
      advance();      
      return new ast.exp.Num(number, temp);
    case TOKEN_TRUE:
      advance();
      return new ast.exp.True(temp);
    case TOKEN_FALSE:
        advance();
        return new ast.exp.False(null, null, temp);
    case TOKEN_THIS:
      advance();
      return new ast.exp.This(temp);
    case TOKEN_ID:
      String StrID = current.lexeme;
      advance();
      return new ast.exp.Id(StrID, temp);
    case TOKEN_NEW: {
      advance();
      switch (current.kind) {
      case TOKEN_INT:
        advance();
        eatToken(Kind.TOKEN_LBRACK);
        temp = current.TokenInfo;
        ast.exp.T tempNewInt =  parseExp();
        eatToken(Kind.TOKEN_RBRACK);
        return new ast.exp.NewIntArray(tempNewInt, temp);
      case TOKEN_ID:
    	Token tempID;
    	tempID = advance();
        eatToken(Kind.TOKEN_LPAREN);
        eatToken(Kind.TOKEN_RPAREN);
        return new ast.exp.NewObject(tempID.lexeme, temp);
      default:
        error(ErrorType.TYPE_AFTER, current, 3, null); //包括自己跳过3个
        return null;
      }
    }
    default:
      error(ErrorType.TYPE_SKIP, current, 0, AtomFollow);
      return null;
    }
  }

  // NotExp -> AtomExp
  // -> AtomExp .id (expList)
  // -> AtomExp [exp]
  // -> AtomExp .length
  private ast.exp.T parseNotExp()
  {
	ast.exp.T AtomExp;
	AtomExp = parseAtomExp();
	Info temp = null;
    while (current.kind == Kind.TOKEN_DOT || current.kind == Kind.TOKEN_LBRACK) {
      if (current.kind == Kind.TOKEN_DOT)
      {
        String CallID;
    	advance();
    	temp = current.TokenInfo;
        if (current.kind == Kind.TOKEN_LENGTH)
        {
          advance();
          return new ast.exp.Length(AtomExp, temp);
        }
        CallID = eatToken(Kind.TOKEN_ID);
        eatToken(Kind.TOKEN_LPAREN);
        java.util.LinkedList<ast.exp.T> expList;
        temp = current.TokenInfo;
        expList = parseExpList();
        eatToken(Kind.TOKEN_RPAREN);
        if(expList == null)
        {
        	return new ast.exp.Call(AtomExp, CallID, null, temp);
        }
        else
        {
        	return new ast.exp.Call(AtomExp, CallID, expList, temp);
        }
        
      }
      else 
      {
    	ast.exp.T AtomMidExp;
    	eatToken(Kind.TOKEN_LBRACK);
    	temp = current.TokenInfo;
        AtomMidExp = parseExp();
        eatToken(Kind.TOKEN_RBRACK);
        return new ast.exp.ArraySelect(AtomExp, AtomMidExp, temp);
      }
    }
    return AtomExp;
  }

  // TimesExp -> ! TimesExp
  // -> NotExp
  private ast.exp.T parseTimesExp()
  {
	boolean flag = false;
	Info temp = null;
	ast.exp.T NotExp;
	ast.exp.T TimesExp = null;
   
    while (current.kind == Kind.TOKEN_NOT) {
      flag = true;
      advance();
      TimesExp = parseTimesExp();
      temp = current.TokenInfo;
    }
    
    if(flag)
    {    	
    	return new ast.exp.Not(TimesExp, temp);
    }
    else
    {
    	NotExp = parseNotExp();
    	return NotExp; 
    }
    
   
  }

  // AddSubExp -> TimesExp * TimesExp
  // -> TimesExp
  private ast.exp.T parseAddSubExp()
  {
	boolean flag = false;
	Info temp = null;
	ast.exp.T left = null;
	ast.exp.T right = null;
	left = parseTimesExp();
    while (current.kind == Kind.TOKEN_TIMES) {
      flag = true;
      advance();
      right = parseTimesExp();
      temp = current.TokenInfo;
    }
    if (flag)
    {
    	return new ast.exp.Times(left, right, temp);
    }
    else
    {
    	return left;
    }
    
  }

  // LtExp -> AddSubExp + AddSubExp
  // -> AddSubExp - AddSubExp
  // -> AddSubExp
  private ast.exp.T parseLtExp()
  {
	int flag = 0;//0 for none;1 for +;2 for -
	Info temp = null;
	ast.exp.T left = null;
	ast.exp.T right = null;
	left = parseAddSubExp();
    while (current.kind == Kind.TOKEN_ADD || current.kind == Kind.TOKEN_SUB) {
      if(current.kind == Kind.TOKEN_ADD)
    	  flag = 1;
      else
    	  flag = 2;
      advance();
      right = parseAddSubExp();
      temp = current.TokenInfo;
    }
    switch (flag)
    {
        case 1: return new ast.exp.Add(left, right, temp);
        case 2: return new ast.exp.Sub(left, right, temp);
        default: return left;
    }

  }

  // AndExp -> LtExp < LtExp
  // -> LtExp
  private ast.exp.T parseAndExp()
  {
	boolean flag = false;
	Info temp = null;
	ast.exp.T left;
	ast.exp.T right = null;
    left = parseLtExp();
    while (current.kind == Kind.TOKEN_LT) {
      advance();
      right =  parseLtExp();
      temp = current.TokenInfo;
      flag = true;
    }
    if(flag)
    	return new ast.exp.Lt(left, right, temp);
    else 
    	return left;
  }

  // Exp -> AndExp && AndExp
  // -> AndExp
  private ast.exp.T parseExp()
  {
	boolean flag = false;
	Info temp = null;
	ast.exp.T left;
	ast.exp.T right = null;
	left = parseAndExp();
    while (current.kind == Kind.TOKEN_AND) {
      advance();
      right = parseAndExp();
      temp = current.TokenInfo;
      flag =true;
    }
    if(flag)
    {
    	return new And(left, right, temp);
    }
    else
    {
    	return left;
    }
    
  }

  // Statement -> { Statement* }
  // -> if ( Exp ) Statement else Statement
  // -> while ( Exp ) Statement
  // -> System.out.println ( Exp ) ;
  // -> id = Exp ;
  // -> id [ Exp ]= Exp ;
  ArrayList<Kind> StateFollow = new ArrayList<Kind>(); 
  private ast.stm.T parseStatement()
  {
	  //StateFollow.add(Kind.TOKEN_RETURN);
	  StateFollow.add(Kind.TOKEN_LBRACE);
	  StateFollow.add(Kind.TOKEN_SEMI);
	  StateFollow.add(Kind.TOKEN_WHILE);
	  StateFollow.add(Kind.TOKEN_SYSTEM);
	  StateFollow.add(Kind.TOKEN_ID);
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a statement.
	  String StmId;
	  switch (current.kind) {
	    case TOKEN_LBRACE:
	      java.util.LinkedList<ast.stm.T> stmList;
	      advance();
	      stmList = parseStatements();
	      eatToken(Kind.TOKEN_RBRACE);
	      return new ast.stm.Block(stmList);
	    case TOKEN_IF:
	      advance();
	      eatToken(Kind.TOKEN_LPAREN);
	      ast.exp.T temp;
	      temp = parseExp();
	      eatToken(Kind.TOKEN_RPAREN);
	      ast.stm.T stmIfTemp;
	      stmIfTemp = parseStatement();
	      //advance();
	      eatToken(Kind.TOKEN_ELSE);
	      ast.stm.T stmElseTemp;
	      stmElseTemp =  parseStatement();
	      return new ast.stm.If(temp , stmIfTemp, stmElseTemp);
	    case TOKEN_WHILE:
    	  advance();
	      eatToken(Kind.TOKEN_LPAREN);
	      ast.exp.T ExpCondition = parseExp();
	      eatToken(Kind.TOKEN_RPAREN);
	      java.util.LinkedList<ast.stm.T> WhileStm = parseStatements();
	      return new ast.stm.While(ExpCondition, WhileStm);
	    case TOKEN_SYSTEM:
	      advance();
	      eatToken(Kind.TOKEN_DOT); 
	      eatToken(Kind.TOKEN_OUT);
	      eatToken(Kind.TOKEN_DOT); 
	      eatToken(Kind.TOKEN_PRINTLN);
	      eatToken(Kind.TOKEN_LPAREN);
	      ast.exp.T exp;
	      exp = parseExp();
	      eatToken(Kind.TOKEN_RPAREN);
	      eatToken(Kind.TOKEN_SEMI);
	      return new ast.stm.Print(exp);
	    case TOKEN_ID:
	      StmId = current.lexeme;	
	      advance();
	      if(current.kind == Kind.TOKEN_ASSIGN)
	      {
	    	  advance();
	    	  ast.exp.T ExpAssign = parseExp();
	    	  //advance(); 
	    	  eatToken(Kind.TOKEN_SEMI);
	    	  return new ast.stm.Assign(StmId, ExpAssign);
	    	  
	      }
	      else if(current.kind == Kind.TOKEN_LBRACK)
	      {
	    	  advance();
	    	  ast.exp.T ArrayExp = parseExp();
	    	  eatToken(Kind.TOKEN_RBRACK);
	    	  eatToken(Kind.TOKEN_ASSIGN);
	    	  ast.exp.T AssignExp = parseExp();
	    	  eatToken(Kind.TOKEN_SEMI);
	    	  return new ast.stm.AssignArray(StmId, ArrayExp, AssignExp);
	      }
	      else
	      {
	    	  error(ErrorType.TYPE_SKIP, current, 0, StateFollow);
	      }
	      return null;
	    default:
	      error(ErrorType.TYPE_SKIP, current, 0, StateFollow);
	      return null;
	    }  
  }

  // Statements -> Statement Statements
  // ->
  private java.util.LinkedList<ast.stm.T> parseStatements()
  {
	java.util.LinkedList<ast.stm.T> stmList = new java.util.LinkedList<ast.stm.T>();
	ast.stm.T temp = null;
    while (current.kind == Kind.TOKEN_LBRACE || current.kind == Kind.TOKEN_IF
        || current.kind == Kind.TOKEN_WHILE
        || current.kind == Kind.TOKEN_SYSTEM || current.kind == Kind.TOKEN_ID 
        ) {//特殊处理= 和 [
    	temp = parseStatement();
    	//if(temp!=null)
        stmList.add(temp);
    }
    return stmList;
  }

  // Type -> int []
  // -> boolean
  // -> int
  // -> id   ??type 不应当推出id吧
  private ast.type.T parseType()
  {
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a type.
	String VarId;
	switch (current.kind) {
	case TOKEN_INT:
		advance(); 
		if(current.kind == Kind.TOKEN_LBRACK)
		{
			advance();
			eatToken(Kind.TOKEN_RBRACK);
			return new ast.type.IntArray();
		}
		else
		{
			return new ast.type.Int();
		}
	case TOKEN_BOOLEAN:
		advance(); 
		return new ast.type.Boolean();
	case TOKEN_ID:
		VarId = current.lexeme;
		advance(); 
		return new ast.type.Class(VarId);
	default:
		error();
		return null;
	}
    //new util.Todo();
  }

  // VarDecl -> Type id ;
  private ast.dec.T parseVarDecl()
  {
    // to parse the "Type" nonterminal in this method, instead of writing
    // a fresh one.FUCK!!!!
	    //parseType();
	  ast.dec.T VarDec;
	  String VarId;
	  String ClassId;
	  switch (current.kind) {
		case TOKEN_INT:
			advance(); 
			if(current.kind == Kind.TOKEN_LBRACK)
			{
				advance();
				eatToken(Kind.TOKEN_RBRACK);
				VarId = eatToken(Kind.TOKEN_ID);
				eatToken(Kind.TOKEN_SEMI);
				return new ast.dec.Dec(new ast.type.IntArray(), VarId);
				
			}
			else
			{
				VarId = current.lexeme;
				advance();
				eatToken(Kind.TOKEN_SEMI);
				return new ast.dec.Dec(new ast.type.Int(), VarId);
				
			}
		case TOKEN_BOOLEAN:
			advance(); 
			VarId = eatToken(Kind.TOKEN_ID);
			eatToken(Kind.TOKEN_SEMI);
			return new ast.dec.Dec(new ast.type.Boolean(), VarId);
			
		case TOKEN_ID:
			ClassId = current.lexeme;
			advance(); 
			if(current.kind == Kind.TOKEN_ID)
			{
				VarId = current.lexeme;
				advance(); 
				eatToken(Kind.TOKEN_SEMI);
				return new ast.dec.Dec(new ast.type.Class(ClassId), VarId);
			}
			else
			{
				moveBack = 1;
				return null;
			}
			
		default:
			error();
			break;
		} 	   
	    return null;
  }

  // VarDecls -> VarDecl VarDecls
  // ->
  private java.util.LinkedList<ast.dec.T> parseVarDecls()
  {
    //while (current.kind == Kind.TOKEN_INT || current.kind == Kind.TOKEN_BOOLEAN
    //    || current.kind == Kind.TOKEN_ID) {
	java.util.LinkedList<ast.dec.T> DecList = new java.util.LinkedList<ast.dec.T>();
	ast.dec.T temp;
	while (current.kind == Kind.TOKEN_INT || current.kind == Kind.TOKEN_BOOLEAN
    		|| current.kind == Kind.TOKEN_ID) {
		temp = parseVarDecl();
		if(temp != null)
		{
			DecList.add(temp);
		}    	
    }
    if(moveBack > 0)
    {
    	moveBackAct();
    }
    return DecList;
  }

  // FormalList -> Type id FormalRest*
  // ->
  // FormalRest -> , Type id
  private java.util.LinkedList<ast.dec.T> parseFormalList()
  {
	java.util.LinkedList<ast.dec.T> DecList = new java.util.LinkedList<ast.dec.T>();  

	ast.type.T DecType;
	String DecID;
    if (current.kind == Kind.TOKEN_INT || current.kind == Kind.TOKEN_BOOLEAN
        || current.kind == Kind.TOKEN_ID) {
      DecType = parseType();
      DecID = eatToken(Kind.TOKEN_ID);
      DecList.add(new ast.dec.Dec(DecType, DecID));
      while (current.kind == Kind.TOKEN_COMMER) {
        advance();
        DecType = parseType();
        DecID = eatToken(Kind.TOKEN_ID);
        DecList.add(new ast.dec.Dec(DecType, DecID));
      }
    }
    return DecList;
  }

  // Method -> public Type id ( FormalList )
  // { VarDecl* Statement* return Exp ;}
  private ast.method.Method parseMethod()
  {
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a method.
	  ast.method.T MethodId;
	  String TypeId;
	  ast.type.T TypeRet;
	  String ReturnId;
	  java.util.LinkedList<ast.dec.T> DecListIner = new java.util.LinkedList<ast.dec.T>();
	  java.util.LinkedList<ast.stm.T> stmList = new java.util.LinkedList<ast.stm.T>();
	  eatToken(Kind.TOKEN_PUBLIC);
	  TypeRet = parseType();
	  
	  TypeId = eatToken(Kind.TOKEN_ID);
	  eatToken(Kind.TOKEN_LPAREN);
	  java.util.LinkedList<ast.dec.T> DecList =  parseFormalList();
	  eatToken(Kind.TOKEN_RPAREN);
	  eatToken(Kind.TOKEN_LBRACE);
	  DecListIner = parseVarDecls();
	  stmList = parseStatements();
	  eatToken(Kind.TOKEN_RETURN);
	  ast.exp.T RetExp = parseExp();
	  //advance();
	  eatToken(Kind.TOKEN_SEMI);
	  eatToken(Kind.TOKEN_RBRACE);	  
	  
    return new ast.method.Method(TypeRet, TypeId, DecList, DecListIner, stmList, RetExp);
  }

  // MethodDecls -> MethodDecl MethodDecls
  // ->
  private java.util.LinkedList<ast.method.T> parseMethodDecls()
  {
	java.util.LinkedList<ast.method.T> MethodList = new java.util.LinkedList<ast.method.T>();
    while (current.kind == Kind.TOKEN_PUBLIC) {
    	MethodList.add(parseMethod());
    }
    return MethodList;
  }

  // ClassDecl -> class id { VarDecl* MethodDecl* }
  // -> class id extends id { VarDecl* MethodDecl* }
  private ast.classs.T parseClassDecl()
  {
	String ClassID;
	String ClassExtends = "";
	String ClassExtendsID = null;
	java.util.LinkedList<ast.dec.T> DecList = new java.util.LinkedList<ast.dec.T>();
	java.util.LinkedList<ast.method.T> MethodList = new java.util.LinkedList<ast.method.T>();
    eatToken(Kind.TOKEN_CLASS);
    ClassID = eatToken(Kind.TOKEN_ID);
    if (current.kind == Kind.TOKEN_EXTENDS) {
      ClassExtends = eatToken(Kind.TOKEN_EXTENDS);
      ClassExtendsID = eatToken(Kind.TOKEN_ID);
    }
    eatToken(Kind.TOKEN_LBRACE);
    DecList = parseVarDecls();
    MethodList = parseMethodDecls();
    eatToken(Kind.TOKEN_RBRACE);
    return new ast.classs.Class(ClassID, ClassExtendsID, DecList, MethodList);
  }

  // ClassDecls -> ClassDecl ClassDecls
  // ->
  private java.util.LinkedList<ast.classs.T>  parseClassDecls()
  {
	java.util.LinkedList<ast.classs.T> DecList = new java.util.LinkedList<ast.classs.T>();
    while (current.kind == Kind.TOKEN_CLASS) {
    	DecList.add(parseClassDecl());
    }
    return DecList;
  }

  // MainClass -> class id
  // {
  // public static void main ( String [] id )
  // {
  // Statement
  // }
  // }
  private ast.mainClass.MainClass parseMainClass()
  {
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a main class as described by the
    // grammar above.
	String MainClassName;
	String arg;
	ast.stm.T StmList;
	eatToken(Kind.TOKEN_CLASS);
	MainClassName = eatToken(Kind.TOKEN_ID);
	
    eatToken(Kind.TOKEN_LBRACE);
    eatToken(Kind.TOKEN_PUBLIC);
    eatToken(Kind.TOKEN_STATIC);
    eatToken(Kind.TOKEN_VOID);
    eatToken(Kind.TOKEN_MAIN);
    eatToken(Kind.TOKEN_LPAREN);
    eatToken(Kind.TOKEN_STRING);
    eatToken(Kind.TOKEN_LBRACK);
    eatToken(Kind.TOKEN_RBRACK);
    arg = eatToken(Kind.TOKEN_ID);
    
    eatToken(Kind.TOKEN_RPAREN); 
    eatToken(Kind.TOKEN_LBRACE);
    StmList = parseStatement();
    eatToken(Kind.TOKEN_RBRACE);
    eatToken(Kind.TOKEN_RBRACE);    
    return new ast.mainClass.MainClass(
    		MainClassName, arg, StmList); 
    //new util.Todo();
  }

  // Program -> MainClass ClassDecl*
  private ast.program.Program parseProgram()
  {
	ast.mainClass.MainClass mainclass = parseMainClass();
	java.util.LinkedList<ast.classs.T> Myclass = parseClassDecls();
    eatToken(Kind.TOKEN_EOF);
    return new ast.program.Program(mainclass, Myclass);
  }

  public ast.program.T parse()
  {   
    return  parseProgram();
    //return new ast.program.Program(factorial,
    //	      new util.Flist<ast.classs.T>().addAll(fac));
  }
}
