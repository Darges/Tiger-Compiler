package lexer;

public class Token
{
  // Lab 1, exercise 1: read the MiniJava specification
  // carefully, and answer these two questions:
  //   1. whether or not one should add other token kinds?
  //   2. which tokens come with an extra "lexeme", and
  //      which don't?
  // It's highly recommended that these token names are
  // alphabetically ordered, if you add new ones.
  public enum Kind {
    TOKEN_ADD, // "+"
    TOKEN_AND, // "&&"
    TOKEN_ASSIGN, // "="
    TOKEN_BOOLEAN, // "boolean"
    TOKEN_CLASS, // "class"
    TOKEN_COMMER, // ","
    TOKEN_DOT, // "."
    TOKEN_ELSE, // "else"
    TOKEN_EOF, // EOF
    TOKEN_EXTENDS, // "extends"
    TOKEN_FALSE, // "false"
    TOKEN_ID, // Identifier   
    TOKEN_IF, // "if"
    TOKEN_INT, // "int"
    TOKEN_LBRACE, // "{"
    TOKEN_LBRACK, // "["
    TOKEN_LENGTH, // "length"
    TOKEN_LPAREN, // "("
    TOKEN_LT, // "<"
    TOKEN_MAIN, // "main"
    TOKEN_NEW, // "new"
    TOKEN_NOT, // "!"
    TOKEN_NUM, // IntegerLiteral
    // "out" is not a Java key word, but we treat it as
    // a MiniJava keyword, which will make the
    // compilation a little easier. Similar cases apply
    // for "println", "System" and "String".
    TOKEN_OUT, // "out"
    TOKEN_PRINTLN, // "println"
    TOKEN_PUBLIC, // "public"
    TOKEN_RBRACE, // "}"
    TOKEN_RBRACK, // "]"
    TOKEN_RETURN, // "return"
    TOKEN_RPAREN, // ")"
    TOKEN_SEMI, // ";"
    TOKEN_STATIC, // "static"
    TOKEN_STRING, // "String"
    TOKEN_SUB, // "-"
    TOKEN_SYSTEM, // "System"
    TOKEN_THIS, // "this"
    TOKEN_TIMES, // "*"
    TOKEN_TRUE, // "true"
    TOKEN_VOID, // "void"
    TOKEN_WHILE, // "while"
  }

  public Kind kind; // kind of the token
  public String lexeme; // extra lexeme for this token, if any
  public Integer lineNum; // on which line of the source file this token appears
  public Integer column;
  public Info TokenInfo;
  
  // Some tokens don't come with lexeme but 
  // others do.
  public Token(Kind kind, Integer lineNum)
  {
    this.kind = kind;
    this.lineNum = lineNum;
    
  }
  
  public Token(Kind kind, Integer lineNum, Integer column)
  {
    this.kind = kind;
    this.lineNum = lineNum;
    this.column = column;
    TokenInfo = new Info(lineNum, column, null);
  }

  public Token(Kind kind, Integer lineNum, String lexeme)
  {
	
    this(kind, lineNum);
    if(kind == Kind.TOKEN_NUM)
    {
       lexeme = lexeme;	
    }
    this.lexeme = lexeme;
  }
  
  public Token(Kind kind, Integer lineNum, String lexeme, Integer column)
  {
	
    this(kind, lineNum);
    if(kind == Kind.TOKEN_NUM)
    {
       lexeme = lexeme;	
    }
    this.lexeme = lexeme;
    this.column = column;
    TokenInfo = new Info(lineNum, column, null);
  }
  
  public Token(Kind kind, Info TokenInfo)
  {
	this.TokenInfo = TokenInfo;    
  }

  public Token(Token current) {
	this.kind = current.kind;
	this.lexeme = current.lexeme;
	this.lineNum = current.lineNum;
	this.column = current.column;
	TokenInfo = new Info(lineNum, column, null);
	// TODO Auto-generated constructor stub
  }

@Override
  public String toString()
  {
    String s;

    // to check that the "lineNum" field has been properly set.
    if (this.lineNum == null)
      new util.Todo();

    s = ": " + ((this.lexeme == null) ? "<NONE>" : this.lexeme) + " : at line "
        + this.lineNum.toString() + " : at column " + this.column.toString();
    return this.kind.toString() + s;
  }
  


  public String KindToString()
  {
	  String s;
	  switch(this.kind)
	  {
	  case      TOKEN_ADD: s="+";break;
	  case      TOKEN_AND: s="&&";break;
	  case	    TOKEN_ASSIGN: s = "=";break;
	  case      TOKEN_BOOLEAN: s = "boolean";break;
	  case	    TOKEN_CLASS: s = "class";break;
	  case	    TOKEN_COMMER: s = ":";break;
	  case	    TOKEN_DOT: s = ".";break;
	  case	    TOKEN_ELSE: s = "else";break;
	  case	    TOKEN_EOF: s = "EOF";break;
	  case	    TOKEN_EXTENDS: s = "extends";break;
	  case	    TOKEN_FALSE: s = "false";break;
	  case      TOKEN_ID: s = "Identifier"   ;break;
	  case	    TOKEN_IF: s = "if";break;
	  case	    TOKEN_INT: s = "int";break;
	  case	    TOKEN_LBRACE: s = "{";break;
	  case	    TOKEN_LBRACK: s = "[";break;
	  case      TOKEN_LENGTH: s = "length";break;
	  case	    TOKEN_LPAREN: s = "(";break;
	  case      TOKEN_LT: s = "<";break;
	  case	    TOKEN_MAIN: s = "main";break;
	  case      TOKEN_NEW: s = "new";break;
	  case	    TOKEN_NOT: s = "!";break;
	  case	    TOKEN_NUM: s = "IntegerLiteral";break;

	  case	    TOKEN_OUT:    s = "out";break;
	  case	    TOKEN_PRINTLN: s = "println";break;
	  case	    TOKEN_PUBLIC: s = "public";break;
	  case	    TOKEN_RBRACE: s = "}";break;
	  case	    TOKEN_RBRACK: s = "]";break;
	  case      TOKEN_RETURN: s = "return";break;
	  case	    TOKEN_RPAREN: s = ")";break;
	  case	    TOKEN_SEMI:   s = ";";break;
	  case	    TOKEN_STATIC: s = "static";break;
	  case	    TOKEN_STRING: s = "String";break;
	  case	    TOKEN_SUB:    s = "-";break;
	  case	    TOKEN_SYSTEM: s = "System";break;
	  case	    TOKEN_THIS:   s = "this";break;
	  case	    TOKEN_TIMES:  s = "*";break;
	  case	    TOKEN_TRUE:   s = "true";;break;
	  case	    TOKEN_VOID:   s = "void";;break;
	  case	    TOKEN_WHILE:  s = "while";;break;
	  default:  s="";
		        break;
	  }
	  return s;
  }
}
