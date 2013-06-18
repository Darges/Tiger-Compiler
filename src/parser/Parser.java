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

  private void advance()
  {
	if(checkStack.isEmpty())
	{
		current = lexer.nextToken();	
		TokenStack[1] = TokenStack[0];
		TokenStack[0] = current; 
	}
	else
	{
		current = checkStack.pop();
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

  private void eatToken(Kind kind)
  {
    if (kind == current.kind)
      advance();
    else {
      System.out.print(filename+".c:"+current.TokenInfo.getLineNum()+":"+current.TokenInfo.getColumn()+": error: ");
      System.out.print("Expects: " + kind.toString());
      System.out.println(" But got: " + current.kind.toString());      
      //error(ErrorType.TYPE_UNEXPECT, current, 1);
      //advance();
      //System.exit(1);
    }
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
  private void parseExpList()
  {
    if (current.kind == Kind.TOKEN_RPAREN)
      return;
    parseExp();
    while (current.kind == Kind.TOKEN_COMMER) {
      advance();
      parseExp();
    }
    return;
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
  private void parseAtomExp()
  {
	AtomFollow.add(Kind.TOKEN_RPAREN);
	//AtomFollow.add(Kind.TOKEN_LBRACK);
    switch (current.kind) {
    case TOKEN_LPAREN:
      advance();
      parseExp();
      eatToken(Kind.TOKEN_RPAREN);
      return;
    case TOKEN_NUM:
      advance();
      return;
    case TOKEN_TRUE:
      advance();
      return;
    case TOKEN_FALSE:
        advance();
        return;
    case TOKEN_THIS:
      advance();
      return;
    case TOKEN_ID:
      advance();
      return;
    case TOKEN_NEW: {
      advance();
      switch (current.kind) {
      case TOKEN_INT:
        advance();
        eatToken(Kind.TOKEN_LBRACK);
        parseExp();
        eatToken(Kind.TOKEN_RBRACK);
        return;
      case TOKEN_ID:
        advance();
        eatToken(Kind.TOKEN_LPAREN);
        eatToken(Kind.TOKEN_RPAREN);
        return;
      default:
        error(ErrorType.TYPE_AFTER, current, 3, null); //包括自己跳过3个
        return;
      }
    }
    default:
      error(ErrorType.TYPE_SKIP, current, 0, AtomFollow);
      return;
    }
  }

  // NotExp -> AtomExp
  // -> AtomExp .id (expList)
  // -> AtomExp [exp]
  // -> AtomExp .length
  private void parseNotExp()
  {
    parseAtomExp();
    while (current.kind == Kind.TOKEN_DOT || current.kind == Kind.TOKEN_LBRACK) {
      if (current.kind == Kind.TOKEN_DOT) {
        advance();
        if (current.kind == Kind.TOKEN_LENGTH) {
          advance();
          return;
        }
        eatToken(Kind.TOKEN_ID);
        eatToken(Kind.TOKEN_LPAREN);
        parseExpList();
        eatToken(Kind.TOKEN_RPAREN);
      } else {
        advance();
        parseExp();
        eatToken(Kind.TOKEN_RBRACK);
      }
    }
    return;
  }

  // TimesExp -> ! TimesExp
  // -> NotExp
  private void parseTimesExp()
  {
    while (current.kind == Kind.TOKEN_NOT) {
      advance();
    }
    parseNotExp();
    return;
  }

  // AddSubExp -> TimesExp * TimesExp
  // -> TimesExp
  private void parseAddSubExp()
  {
    parseTimesExp();
    while (current.kind == Kind.TOKEN_TIMES) {
      advance();
      parseTimesExp();
    }
    return;
  }

  // LtExp -> AddSubExp + AddSubExp
  // -> AddSubExp - AddSubExp
  // -> AddSubExp
  private void parseLtExp()
  {
    parseAddSubExp();
    while (current.kind == Kind.TOKEN_ADD || current.kind == Kind.TOKEN_SUB) {
      advance();
      parseAddSubExp();
    }
    return;
  }

  // AndExp -> LtExp < LtExp
  // -> LtExp
  private void parseAndExp()
  {
    parseLtExp();
    while (current.kind == Kind.TOKEN_LT) {
      advance();
      parseLtExp();
    }
    return;
  }

  // Exp -> AndExp && AndExp
  // -> AndExp
  private void parseExp()
  {
    parseAndExp();
    while (current.kind == Kind.TOKEN_AND) {
      advance();
      parseAndExp();
    }
    return;
  }

  // Statement -> { Statement* }
  // -> if ( Exp ) Statement else Statement
  // -> while ( Exp ) Statement
  // -> System.out.println ( Exp ) ;
  // -> id = Exp ;
  // -> id [ Exp ]= Exp ;
  ArrayList<Kind> StateFollow = new ArrayList<Kind>(); 
  private void parseStatement()
  {
	  StateFollow.add(Kind.TOKEN_RETURN);
	  StateFollow.add(Kind.TOKEN_RBRACE);
	  //StateFollow.add(Kind.TOKEN_SEMI);
	  //StateFollow.add(Kind.TOKEN_WHILE);
	  //StateFollow.add(Kind.TOKEN_SYSTEM);
	  //StateFollow.add(Kind.TOKEN_ID);
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a statement.
	  switch (current.kind) {
	    case TOKEN_LBRACE:
	      advance();
	      parseStatements();
	      eatToken(Kind.TOKEN_RBRACE);
	      return;
	    case TOKEN_IF:
	      advance();
	      eatToken(Kind.TOKEN_LPAREN);
	      parseExp();
	      eatToken(Kind.TOKEN_RPAREN);
	      parseStatement();
	      //advance();
	      eatToken(Kind.TOKEN_ELSE);
	      parseStatement();
	      return;
	    case TOKEN_WHILE:
    	  advance();
	      eatToken(Kind.TOKEN_LPAREN);
	      parseExp();
	      eatToken(Kind.TOKEN_RPAREN);
	      parseStatement();
	      return;
	    case TOKEN_SYSTEM:
	      advance();
	      eatToken(Kind.TOKEN_DOT); 
	      eatToken(Kind.TOKEN_OUT);
	      eatToken(Kind.TOKEN_DOT); 
	      eatToken(Kind.TOKEN_PRINTLN);
	      eatToken(Kind.TOKEN_LPAREN);
	      parseExp();
	      eatToken(Kind.TOKEN_RPAREN);
	      eatToken(Kind.TOKEN_SEMI);
	      return;
	    case TOKEN_ID:
	      advance();
	      if(current.kind == Kind.TOKEN_ASSIGN)
	      {
	    	  advance();
	    	  parseExp();
	    	  //advance(); 
	    	  eatToken(Kind.TOKEN_SEMI);
	      }
	      else if(current.kind == Kind.TOKEN_LBRACK)
	      {
	    	  advance();
	    	  parseExp();
	    	  eatToken(Kind.TOKEN_RBRACK);
	    	  eatToken(Kind.TOKEN_ASSIGN);
	    	  parseExp();
	    	  eatToken(Kind.TOKEN_SEMI);
	      }
	      else
	      {
	    	  error(ErrorType.TYPE_SKIP, current, 0, StateFollow);
	      }
	      return;
	    default:
	      error(ErrorType.TYPE_SKIP, current, 0, StateFollow);
	      return;
	    }  
  }

  // Statements -> Statement Statements
  // ->
  private void parseStatements()
  {
    while (current.kind == Kind.TOKEN_LBRACE || current.kind == Kind.TOKEN_IF
        || current.kind == Kind.TOKEN_WHILE
        || current.kind == Kind.TOKEN_SYSTEM || current.kind == Kind.TOKEN_ID 
        ) {//特殊处理= 和 [
      parseStatement();
    }
    return;
  }

  // Type -> int []
  // -> boolean
  // -> int
  // -> id   ??type 不应当推出id吧
  private void parseType()
  {
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a type.
	switch (current.kind) {
	case TOKEN_INT:
		advance(); 
		if(current.kind == Kind.TOKEN_LBRACK)
		{
			advance();
			eatToken(Kind.TOKEN_RBRACK);
			return;
		}
		else
		{
			return;
		}
	case TOKEN_BOOLEAN:
		advance(); 
		return;
	case TOKEN_ID:
		advance(); 
		return;
	default:
		error();
		return;
	}
    //new util.Todo();
  }

  // VarDecl -> Type id ;
  private void parseVarDecl()
  {
    // to parse the "Type" nonterminal in this method, instead of writing
    // a fresh one.FUCK!!!!
	    //parseType();
	  switch (current.kind) {
		case TOKEN_INT:
			advance(); 
			if(current.kind == Kind.TOKEN_LBRACK)
			{
				advance();
				eatToken(Kind.TOKEN_RBRACK);
			    eatToken(Kind.TOKEN_ID);
				eatToken(Kind.TOKEN_SEMI);
				break;
			}
			else
			{
				advance();
				eatToken(Kind.TOKEN_SEMI);
				break;
			}
		case TOKEN_BOOLEAN:
			advance(); 
			eatToken(Kind.TOKEN_ID);
			eatToken(Kind.TOKEN_SEMI);
			break;
		case TOKEN_ID:
			advance(); 
			if(current.kind == Kind.TOKEN_ID)
			{
				advance(); 
				eatToken(Kind.TOKEN_SEMI);
			}
			else
			{
				moveBack = 1;
				return;
			}
			break;
		default:
			error();
			break;
		} 	   
	    return;
  }

  // VarDecls -> VarDecl VarDecls
  // ->
  private void parseVarDecls()
  {
    //while (current.kind == Kind.TOKEN_INT || current.kind == Kind.TOKEN_BOOLEAN
    //    || current.kind == Kind.TOKEN_ID) {
    while (current.kind == Kind.TOKEN_INT || current.kind == Kind.TOKEN_BOOLEAN
    		|| current.kind == Kind.TOKEN_ID) {
      parseVarDecl();
    }
    if(moveBack > 0)
    {
    	moveBackAct();
    }
    return;
  }

  // FormalList -> Type id FormalRest*
  // ->
  // FormalRest -> , Type id
  private void parseFormalList()
  {
    if (current.kind == Kind.TOKEN_INT || current.kind == Kind.TOKEN_BOOLEAN
        || current.kind == Kind.TOKEN_ID) {
      parseType();
      eatToken(Kind.TOKEN_ID);
      while (current.kind == Kind.TOKEN_COMMER) {
        advance();
        parseType();
        eatToken(Kind.TOKEN_ID);
      }
    }
    return;
  }

  // Method -> public Type id ( FormalList )
  // { VarDecl* Statement* return Exp ;}
  private void parseMethod()
  {
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a method.
	  
	  eatToken(Kind.TOKEN_PUBLIC);
	  parseType();
	  
	  eatToken(Kind.TOKEN_ID);
	  eatToken(Kind.TOKEN_LPAREN);
	  parseFormalList();
	  eatToken(Kind.TOKEN_RPAREN);
	  eatToken(Kind.TOKEN_LBRACE);
	  parseVarDecls();
	  parseStatements();
	  eatToken(Kind.TOKEN_RETURN);
	  parseExp();
	  //advance();
	  eatToken(Kind.TOKEN_SEMI);
	  eatToken(Kind.TOKEN_RBRACE);	  
	  
    return;
  }

  // MethodDecls -> MethodDecl MethodDecls
  // ->
  private void parseMethodDecls()
  {
    while (current.kind == Kind.TOKEN_PUBLIC) {
      parseMethod();
    }
    return;
  }

  // ClassDecl -> class id { VarDecl* MethodDecl* }
  // -> class id extends id { VarDecl* MethodDecl* }
  private void parseClassDecl()
  {
    eatToken(Kind.TOKEN_CLASS);
    eatToken(Kind.TOKEN_ID);
    if (current.kind == Kind.TOKEN_EXTENDS) {
      eatToken(Kind.TOKEN_EXTENDS);
      eatToken(Kind.TOKEN_ID);
    }
    eatToken(Kind.TOKEN_LBRACE);
    parseVarDecls();
    parseMethodDecls();
    eatToken(Kind.TOKEN_RBRACE);
    return;
  }

  // ClassDecls -> ClassDecl ClassDecls
  // ->
  private void parseClassDecls()
  {
    while (current.kind == Kind.TOKEN_CLASS) {
      parseClassDecl();
    }
    return;
  }

  // MainClass -> class id
  // {
  // public static void main ( String [] id )
  // {
  // Statement
  // }
  // }
  private void parseMainClass()
  {
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a main class as described by the
    // grammar above.
	eatToken(Kind.TOKEN_CLASS);
	eatToken(Kind.TOKEN_ID);
    eatToken(Kind.TOKEN_LBRACE);
    eatToken(Kind.TOKEN_PUBLIC);
    eatToken(Kind.TOKEN_STATIC);
    eatToken(Kind.TOKEN_VOID);
    eatToken(Kind.TOKEN_MAIN);
    eatToken(Kind.TOKEN_LPAREN);
    eatToken(Kind.TOKEN_STRING);
    eatToken(Kind.TOKEN_LBRACK);
    eatToken(Kind.TOKEN_RBRACK);
    eatToken(Kind.TOKEN_ID);
    eatToken(Kind.TOKEN_RPAREN); 
    eatToken(Kind.TOKEN_LBRACE);
    parseStatement();
    eatToken(Kind.TOKEN_RBRACE);
    eatToken(Kind.TOKEN_RBRACE);    
    //new util.Todo();
  }

  // Program -> MainClass ClassDecl*
  private void parseProgram()
  {
    parseMainClass();
    parseClassDecls();
    eatToken(Kind.TOKEN_EOF);
    return;
  }

  public ast.program.T parse()
  {
    parseProgram();
    return null;
  }
}
