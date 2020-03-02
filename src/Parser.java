import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Parser{
  List<Token> allTokens = new ArrayList<Token>();
  SymbolTable symbolTable = new SymbolTable();
  int index = -1;
  int loopTime = 0;
  String s;
  
  public Parser() {
    Lexer lexer = new Lexer("testId.txt");
    allTokens = lexer.getAllTokens();
  }
  
  boolean parseProgram() {
  	boolean f = true;
  	while (index != allTokens.size() - 1 && f) {
  		f = parseAssignment();
  		loopTime += 1;
	}
  	return f;
  }
  
  boolean parseAssignment() {
    Token token = nextToken();
    
    if (token.getType() == "EOF") {
      return true;
	}
    if(loopTime % 3 == 0) {
      if(!parseId(token)) {
        s = "Error: Expecting identifier, line " + String.valueOf(token.getLine());
	  	return false;
      }
	} else if (loopTime % 3 == 1) {
      if (!parseAssignOp(token)) {
      	s = "Error: Expecting assignment operator, line " + String.valueOf(token.getLine());
	  	return false;
      }
	} else if (loopTime % 3 == 2) {
	  return parseExpression(token);
	}
    return true;
  }
  
  boolean parseId(Token token) {
  	if (token.getType() == "ID") {
  	  symbolTable.add(token.getValue());
  	  return true;
	} else {
  	  return false;
	}
  }
  
  boolean parseAssignOp(Token token) {
  	if (token.getType() == "ASSMT") {
  	  return true;
	} else {
  	  return false;
	}
  }
  
  boolean parseExpression(Token token) {
    boolean f = false;
 	while(index < allTokens.size() && token.getType() != "EOF") {
	  if (token.getType() == "ID" || token.getType() == "INT") {
	    token = nextToken();
	    f = true;
	    if (token.getType() == "PLUS") {
		  token = putToken();
		  if (token.getType() == "ID" && symbolTable.getAddress(token.getValue()) == -1) {
			s = "Error: Identifier not defined, line " + String.valueOf(token.getLine());
			return false;
		  } else {
			token = nextToken();
			continue;
		  }
		} else if (token.getType() == "ID") {
	      token = putToken();
		  if (token.getType() == "ID" && symbolTable.getAddress(token.getValue()) == -1) {
			s = "Error: Identifier not defined, line " + String.valueOf(token.getLine());
			return false;
		  } else {
			return true;
		  }
		} else if(token.getType() == "EOF") {
		  return true;
		} else {
	      s = "Error: Expecting identifier or add operator, line " + String.valueOf(token.getLine());
	      return false;
		}
	  } else if (token.getType() == "PLUS" && f) {
		f = false;
		token = nextToken();
		continue;
	  } else {
	    s = "Error: Expecting identifier or integer, line " + String.valueOf(token.getLine());
	    return false;
	  }
	}
	return true;
  }
  
  
  Token nextToken() {
  	index += 1;
  	return allTokens.get(index);
  }
  
  Token putToken() {
  	index = index - 1;
 	return allTokens.get(index);
  }
  
  
  @Override
  public String toString() {
	return "Parser{" +
				   "allTokens=" + allTokens +
				   ", symbolTable=" + symbolTable +
				   '}';
  }
  
  public static void main(String args[]) {
    Parser parser = new Parser();
    System.out.println(parser.allTokens);
    boolean f = parser.parseProgram();
    if (f) {
      System.out.println("Valid Program");
	} else {
      System.out.println(parser.s);
      System.out.println("Invalid Program");
	}
 
  }
  
 
}

