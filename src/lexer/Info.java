package lexer;

public class Info {
	  public Integer lineNum; // on which line of the source file this token appears
	  public Integer column;
	  public String lineInfo;
	  public String fname; 
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
	  
	  public Info(Integer lineNum, Integer column, String lineInfo ,String fname)
	  {
		  this.lineNum  = lineNum;
		  this.column   = column;
		  this.lineInfo = lineInfo;
		  this.fname = fname;
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
