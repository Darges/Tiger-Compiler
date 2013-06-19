package lexer;

public class Info {
	  public Integer lineNum; // on which line of the source file this token appears
	  public Integer column;
	  public String lineInfo;
	  public Info()
	  {
		  lineNum  = 0;
		  column   = 0;
		  lineInfo = null;
	  }  
	  
	  public Info(Integer lineNum, Integer column, String lineInfo)
	  {
		  this.lineNum  = lineNum;
		  this.column   = column;
		  this.lineInfo = lineInfo;
	  }
	  
	  public Integer getLineNum()
	  {
		  return lineNum;
	  }
	  
	  public Integer getColumn()
	  {
		  return column;
	  } 
}
