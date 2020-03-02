public class Token {
  private String type;
  private String value;
  private int line;
  
  public Token(String s1, String s2, int l) {
  	this.type = s1;
  	this.value = s2;
  	this.line = l;
  }
  
  public String getType() {
    return this.type;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public int getLine() {
    return this.line;
  }
  
  public String toString() {
    String s = "";
    s = s + this.type + " " + this.value;
    return s;
  }
}
