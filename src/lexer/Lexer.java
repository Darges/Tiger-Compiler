package lexer;

import java.io.InputStream;
import java.util.Hashtable;

import util.Todo;

import lexer.Token.Kind;

public class Lexer
{
  String fname; // the input file name to be compiled	
  InputStream fstream; // input stream for the above file
  String tempStr = "";
  String lineInfo;
  boolean flag = false;
  public static int count=0;
  public static int linenum=1;
  
  public static Hashtable<String,Kind> table = 
	new Hashtable<String,Kind>();
  public Lexer(String fname, InputStream fstream)
  {
    this.fname = fname;
    this.fstream = fstream;
    table.put("boolean", Kind.TOKEN_BOOLEAN);
    table.put("class", Kind.TOKEN_CLASS);
    table.put("else", Kind.TOKEN_ELSE);
    table.put("extends", Kind.TOKEN_EXTENDS);
    table.put("false", Kind.TOKEN_FALSE);
    table.put("if", Kind.TOKEN_IF);
    table.put("int", Kind.TOKEN_INT);
    table.put("length", Kind.TOKEN_LENGTH);
    table.put("main", Kind.TOKEN_MAIN);
    table.put("new", Kind.TOKEN_NEW);
    table.put("int", Kind.TOKEN_INT);
    table.put("out", Kind.TOKEN_OUT);
    table.put("println", Kind.TOKEN_PRINTLN);
    table.put("public", Kind.TOKEN_PUBLIC);
    table.put("return", Kind.TOKEN_RETURN);
    table.put("static", Kind.TOKEN_STATIC);
    table.put("String", Kind.TOKEN_STRING);
    table.put("System", Kind.TOKEN_SYSTEM);
    table.put("this", Kind.TOKEN_THIS);
    table.put("void", Kind.TOKEN_VOID);
    table.put("true", Kind.TOKEN_TRUE);
    table.put("while", Kind.TOKEN_WHILE);
  }

  // When called, return the next token (refer to the code "Token.java")
  // from the input stream.
  // Return TOKEN_EOF when reaching the end of the input stream.
  private Token nextTokenInternal() throws Exception
  {
	
    int c = this.fstream.read();
    count++;  
    if (-1 == c)
      // The value for "lineNum" is now "null",<=50
      // you should modify this to an appropriate
      // line number for the "EOF" token.
      return new Token(Kind.TOKEN_EOF, linenum, count);

    // skip all kinds of "blanks"
    while ((' ' == c || '\t' == c || '\n' == c || '\r' == c || '/' == c)| flag ) {
      if( '\n' == c )
      {
    	  linenum++;  //find a line break 
    	  count = 0;
    	  flag = false;  //换行则注释处理结束
      }
      if( ' ' == c)
      {
    	  count++;
      }
      if( '\t' == c)
      {
    	  count += 4; // length of \t
      }
      c = this.fstream.read();
      count++;
      if( '/' == c)  //用于注释处理
      {
    	  flag = true; 
      }
      else
      {
    	  //error;
      }
    }
    if (-1 == c)
      return new Token(Kind.TOKEN_EOF, linenum, count); 
      
    if ('+' == c)
      return new Token(Kind.TOKEN_ADD, linenum, count);   
    if ('&' == c)
    {
      this.fstream.mark(1);
      c = this.fstream.read();
      count++;
      if(c=='&')
      {
    	  return new Token(Kind.TOKEN_AND, linenum, count);
      }
      else
      {
    	  this.fstream.reset();
    	  count--;
    	  //this.fstream.skip(count-1);
    	  return null;
      }
    }
    if('='==c)
      return new Token(Kind.TOKEN_ASSIGN, linenum, count);
    if(','==c)
      return new Token(Kind.TOKEN_COMMER, linenum, count);
    if('.'==c)
      return new Token(Kind.TOKEN_DOT, linenum, count);
    if('{'==c)
      return new Token(Kind.TOKEN_LBRACE, linenum, count);
    if('['==c)
      return new Token(Kind.TOKEN_LBRACK, linenum, count);     
    if('('==c)
      return new Token(Kind.TOKEN_LPAREN, linenum, count);    
    if('<'==c)
      return new Token(Kind.TOKEN_LT, linenum, count);   
    if('!'==c)
      return new Token(Kind.TOKEN_NOT, linenum, count); 
    if('}'==c)
        return new Token(Kind.TOKEN_RBRACE, linenum, count);  
    if(']'==c)
        return new Token(Kind.TOKEN_RBRACK, linenum, count);  
    if(')'==c)
        return new Token(Kind.TOKEN_RPAREN, linenum, count);  
    if(';'==c)
        return new Token(Kind.TOKEN_SEMI, linenum, count);  
    if('-'==c)
        return new Token(Kind.TOKEN_SUB, linenum, count); 
    if('*'==c)
        return new Token(Kind.TOKEN_TIMES, linenum, count);  
    
    // IntegerLiteral
    if(c>='0'&&c<='9')
    {
      //tempStr;
    	tempStr="";
      tempStr+=c;
      this.fstream.mark(1);
	  c = this.fstream.read();
	  count++;
      while(c>='0'&&c<='9')
      {    	
    	  this.fstream.mark(1);
    	  c = this.fstream.read();
    	  count++;
    	  tempStr+=c;
      }
      this.fstream.reset();
      count--;
	  //this.fstream.skip(1); 	  
	  return new Token(Kind.TOKEN_NUM, linenum, tempStr, count);
    } 	
    
    if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c=='_'))
    {
      //tempStr=null;
      tempStr="";
      do{
    	  tempStr+=(char)c;
          this.fstream.mark(1);
      	  c = this.fstream.read();
      	  count++;
      }while((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c=='_')||(c>='0'&&c<='9'));
      
      this.fstream.reset();
      count--;
  	  //this.fstream.skip(1); 
  	  Token.Kind tempkind = table.get(tempStr);
  	  if(null!=tempkind)
  	  {
  		  return new Token(tempkind, linenum, count);
  	  }
  	  else
  	  {
  		  return new Token(Kind.TOKEN_ID, linenum, tempStr, count);	
  	  }	  
    }      
      // Lab 1, exercise 2: supply missing code to
      // lex other kinds of tokens.
      // Hint: think carefully about the basic
      // data structure and algorithms. The code
      // is not that much and may be less than 50 lines. If you
      // find you are writing a lot of code, you
      // are on the wrong way.
      new Todo();
      return null;
    
  }

  public Token nextToken()
  {
    Token t = null;

    try {
      t = this.nextTokenInternal();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    if (control.Control.lex)
      System.out.println(t.toString());
    return t;
  }
}
