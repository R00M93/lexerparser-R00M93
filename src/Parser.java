import javax.swing.plaf.synth.SynthScrollBarUI;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Parser {
  private List<Token> allTokens; // store the list from lexer
  private SymbolTable symbolTable = new SymbolTable(); // use class SymbolTable
  private ByteCodeInterpreter bi = new ByteCodeInterpreter(10); // use class ByteCodeInterpreter
  private int index = -1; // the index in allTokens list
  private int loopTime = 0; // to show the parseAssignment the next step
  private Token storeToken; // store the ID before =
  private String s; // store the error type
  
  public Parser() {
	Lexer lexer = new Lexer("testNew.txt");
	allTokens = lexer.getAllTokens();
  }
  
  private boolean parseProgram() { //parse the whole program to
	boolean f = true;
	while (index != allTokens.size() - 1 && f) { //when f is false, exit the while loop
	  f = parseAssignment();
	  loopTime += 1; // increase the loopTime to see which if to do
	}
	return f;
  }
  
  private boolean parseAssignment() {
	Token token = nextToken(); // get next token
	if (token.getType() == "EOF") {
	  return true;
	}
	if (loopTime % 3 == 0) { // if the token is the first to parse
	  if (!parseId(token)) {
		s = "Error: Expecting identifier, line " + String.valueOf(token.getLine());
		return false; //wrong assignment
	  } else {
		storeToken = token;//store the ID before the = for bytecodeInterpreter
	  }
	} else if (loopTime % 3 == 1) { // if the token is the second to parse
	  if (!parseAssignOp(token)) {
		s = "Error: Expecting assignment operator, line " + String.valueOf(token.getLine());
		return false; //wrong assignment
	  }
	} else if (loopTime % 3 == 2) { // parse the expression
	  if (parseExpression(token)) {
		bi.generate(ByteCodeInterpreter.STORE, symbolTable.getAddress(storeToken.getValue())); // execute the store command
		bi.run();
		return true;
	  } else {
		return false;
	  }
	}
	return true;
  }
  
  private boolean parseId(Token token) { //parse the ID
	if (token.getType() == "ID") {
	  symbolTable.add(token.getValue()); // add it to symbolTable
	  return true;
	} else {
	  return false;
	}
  }
  
  private boolean parseAssignOp(Token token) { // parse the Assignment operation
	if (token.getType() == "ASSMT") {
	  return true;
	} else {
	  return false;
	}
  }
  
  @Override
  public boolean equals(Object o) { // equals method
	if (this == o) return true;
	if (!(o instanceof Parser)) return false;
	Parser parser = (Parser) o;
	return Objects.equals(allTokens, parser.allTokens);
  }
  
  
  private boolean parseExpression(Token token) { // parse the expression
	boolean f = false;
	int x = 1;
	while (index < allTokens.size() && token.getType() != "EOF") { //index must within the list size and token's type shouldn't be EOF
	  if (token.getType() == "ID" || token.getType() == "INT") { // parse if token is ID or INT
		token = nextToken(); // start to parse the next token
		f = true;
		if (token.getType() == "PLUS" || token.getType() == "MULTI" || token.getType() == "MINUS") { // if it is + * /
		  token = putToken(); // parse the previous token
		  if (token.getType() == "ID" && symbolTable.getAddress(token.getValue()) == -1) { // if ID is not in symbolTable
			s = "Error: Identifier not defined, line " + String.valueOf(token.getLine());
			return false;
		  } else if (token.getType() == "ID" && symbolTable.getAddress(token.getValue()) != -1) { //add another else if for valid ID
			bi.generate(ByteCodeInterpreter.LOAD, symbolTable.getAddress(token.getValue())); // add command to the bytecode arraylist for ID
			Token next = allTokens.get(index + 1);
			Token prev = allTokens.get(index - 1);
			if (prev.getType() == "MULTI" || next.getType() == "MULTI") {
			  bi.remove();
			  x *= bi.getMemory().get(symbolTable.getAddress(token.getValue()));
			} else if (next.getType() == "PLUS" && x != 1) {
			  bi.generate(ByteCodeInterpreter.LOADI, x);
			  x = 1;
			} else if (prev.getType() == "MINUS") {
			  bi.remove();
			  bi.generate(ByteCodeInterpreter.LOADI, bi.getMemory().get(symbolTable.getAddress(token.getValue())) * -1);
			}
			token = nextToken();
		  } else if (token.getType() == "INT") { // if token is INT
			bi.generate(ByteCodeInterpreter.LOADI, Integer.parseInt(token.getValue()));// add command to the bytecode arraylist for INT
			Token next = allTokens.get(index + 1);
			Token prev = allTokens.get(index - 1);
			if (prev.getType() == "MULTI" || next.getType() == "MULTI") {
			  bi.remove();
			  x *= Integer.parseInt(token.getValue());
			} else if (next.getType() == "PLUS" && x != 1) {
			  bi.generate(ByteCodeInterpreter.LOADI, x);
			  x = 1;
			} else if (prev.getType() == "MINUS") {
			  bi.remove();
			  bi.generate(ByteCodeInterpreter.LOADI, Integer.parseInt(token.getValue()) * -1);
			}
			token = nextToken();
		  } else {
			token = nextToken();
		  }
		} else if (token.getType() == "ID") { // if it is ID
		  token = putToken(); // parse the previous token
		  if (token.getType() == "ID" && symbolTable.getAddress(token.getValue()) == -1) {// if ID is not in symbolTable
			s = "Error: Identifier not defined, line " + String.valueOf(token.getLine());
			return false;
		  } else if (token.getType() == "ID" && symbolTable.getAddress(token.getValue()) != -1) {
			bi.generate(ByteCodeInterpreter.LOAD, symbolTable.getAddress(token.getValue()));// add command to the bytecode arraylist for ID
			Token next = allTokens.get(index + 1);
			Token prev = allTokens.get(index - 1);
			if (prev.getType() == "MULTI" || next.getType() == "MULTI") {
			  bi.remove();
			  x *= bi.getMemory().get(symbolTable.getAddress(token.getValue()));
			} else if (next.getType() == "PLUS" && x != 1) {
			  bi.generate(ByteCodeInterpreter.LOADI, x);
			  x = 1;
			} else if (prev.getType() == "MINUS") {
			  bi.remove();
			  bi.generate(ByteCodeInterpreter.LOADI, bi.getMemory().get(symbolTable.getAddress(token.getValue())) * -1);
			}
			return true;
		  } else if (token.getType() == "INT") {// if token is INT
			bi.generate(ByteCodeInterpreter.LOADI, Integer.parseInt(token.getValue()));// add command to the bytecode arraylist for INT
			Token next = allTokens.get(index + 1);
			Token prev = allTokens.get(index - 1);
			if (prev.getType() == "MULTI" || next.getType() == "MULTI") {
			  bi.remove();
			  x *= Integer.parseInt(token.getValue());
			} else if (next.getType() == "PLUS" && x != 1) {
			  bi.generate(ByteCodeInterpreter.LOADI, x);
			  x = 1;
			} else if (prev.getType() == "MINUS") {
			  bi.remove();
			  bi.generate(ByteCodeInterpreter.LOADI, Integer.parseInt(token.getValue()) * -1);
			}
			return true;
		  } else {
			return true;
		  }
		} else if (token.getType() == "EOF") {//if it is the end of file
		  token = putToken();// parse the previous token
		  if (token.getType() == "ID" && symbolTable.getAddress(token.getValue()) == -1) {// if ID is not in symbolTable
			s = "Error: Identifier not defined, line " + String.valueOf(token.getLine());
			return false;
		  } else if (token.getType() == "ID" && symbolTable.getAddress(token.getValue()) != -1) {
			bi.generate(ByteCodeInterpreter.LOAD, symbolTable.getAddress(token.getValue()));// add command to the bytecode arraylist for ID
			Token next = allTokens.get(index + 1);
			Token prev = allTokens.get(index - 1);
			System.out.println(symbolTable.getAddress(token.getValue()));
			if (prev.getType() == "MULTI" || next.getType() == "MULTI") {
			  bi.remove();
			  x *= bi.getMemory().get(symbolTable.getAddress(token.getValue()));
			} else if (next.getType() == "PLUS" && x != 1) {
			  bi.generate(ByteCodeInterpreter.LOADI, x);
			  x = 1;
			} else if (prev.getType() == "MINUS") {
			  bi.remove();
			  bi.generate(ByteCodeInterpreter.LOADI, bi.getMemory().get(symbolTable.getAddress(token.getValue())) * -1);
			}
			token = nextToken();
		  } else if (token.getType() == "INT") {// if token is INT
			bi.generate(ByteCodeInterpreter.LOADI, Integer.parseInt(token.getValue()));// add command to the bytecode arraylist for INT
			Token next = allTokens.get(index + 1);
			Token prev = allTokens.get(index - 1);
			if (prev.getType() == "MULTI" || next.getType() == "MULTI") {
			  bi.remove();
			  x *= Integer.parseInt(token.getValue());
			} else if (next.getType() == "PLUS" && x != 1) {
			  bi.generate(ByteCodeInterpreter.LOADI, x);
			  x = 1;
			} else if (prev.getType() == "MINUS") {
			  bi.remove();
			  bi.generate(ByteCodeInterpreter.LOADI, Integer.parseInt(token.getValue()) * -1);
			}
			token = nextToken();
		  } else {
			token = nextToken();
		  }
		} else {
		  s = "Error: Expecting identifier or add operator, line " + String.valueOf(token.getLine());
		  return false;
		}
	  } else if ((token.getType() == "PLUS" || token.getType() == "MULTI" || token.getType() == "MINUS") && f) { //if token is + * / and before it is INT or ID
		if (f) {
		  f = false; //set the f to false
		  if (token.getType() == "MINUS") {
			x = -1;
		  }
		  token = nextToken();
		  
		} else {
		  s = "Error: Expecting identifier or integer, line " + String.valueOf(token.getLine());
		  return false;
		}
	  } else {
		s = "Error: Expecting identifier or integer, line " + String.valueOf(token.getLine());
		return false;
	  }
	}
	if (x != 1) {
	  bi.generate(ByteCodeInterpreter.LOADI, x);
	}
	if (f) { //to see if the last thing is not + * /
	  return true;
	} else {
	  s = "Error: Expecting identifier or integer, line " + String.valueOf(token.getLine());
	  return false;
	}
  }
  
  
  private Token nextToken() { // increase the index and get the next token
	index += 1;
	return allTokens.get(index);
  }
  
  private Token putToken() { // decrease the index and get the previous token
	index = index - 1;
	return allTokens.get(index);
  }
  
  
  @Override
  public String toString() { //toString method
	return "Parser{" +
				   "allTokens=" + allTokens +
				   ", symbolTable=" + symbolTable +
				   '}';
  }
  
  
  public static void main(String args[]) {
	Parser parser = new Parser();
	System.out.println(parser.allTokens);
	boolean f = parser.parseProgram();
	if (f) { // if true, print the answer
	  System.out.println("Valid Program");
	  //parser.bi.run(); // run the interpreter
	  System.out.println(parser.symbolTable.toString()); // Bytecodeinterpreter answer
	  System.out.println(parser.bi.toString());
	} else { // if false, print the answer
	  System.out.println(parser.s);
	  System.out.println("Invalid Program");
	}
  }
}

