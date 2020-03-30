import java.util.Objects;

public class Token {
  private String type;
  private String value;
  private int line;
  //data members for token
  
  public Token(String s1, String s2, int l) {
	this.type = s1;
	this.value = s2;
	this.line = l;
  }
  //constructor for token class
  
  public String getType() {
	return this.type;
  } // get type
  
  public String getValue() {
	return this.value;
  } // get value
  
  public int getLine() { // get line
	return this.line;
  }
  
  @Override
  public boolean equals(Object o) { // equals method
	if (this == o) return true;
	if (!(o instanceof Token)) return false;
	Token token = (Token) o;
	return getLine() == token.getLine() &&
				   Objects.equals(getType(), token.getType()) &&
				   Objects.equals(getValue(), token.getValue());
  }
  
  public String toString() { //toString method
	String s = "";
	s = s + this.type + " " + this.value;
	return s;
  }
}
