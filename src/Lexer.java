import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to build an array of Tokens from an input file
 *
 * @author wolberd
 * @see Token
 * @see Parser
 * @see ByteCodeInterpreter
 */
public class Lexer {
  
  private String buffer;
  private int index = 0; // the index in buffer
  private int lineN = 1; // data member for line
  
  public static final String INTTOKEN = "INT";
  public static final String IDTOKEN = "ID";
  public static final String ASSMTTOKEN = "ASSMT";
  public static final String PLUSTOKEN = "PLUS";
  /*  public static final String MULTITOKEN = "MULTI"; //*
	public static final String DIVIDETOKEN = "DIVIDE"; // /
	public static final String LBRACKETTOKEN = "LBRACKET"; // (
	public static final String RBRACKETTOKEN = "RBRACKET"; // )
	*/
  public static final String EOFTOKEN = "EOF";
  
  /**
   * call getInput to get the file data into our buffer
   *
   * @param fileName the file we'll open
   */
  public Lexer(String fileName) {
	getInput(fileName);
	
  }
  
  /**
   * Reads given file into the data member buffer
   *
   * @param fileName name of file to parse
   */
  private void getInput(String fileName) {
	try {
	  Path filePath = Paths.get(fileName);
	  byte[] allBytes = Files.readAllBytes(filePath);
	  buffer = new String(allBytes);
	} catch (IOException e) {
	  System.out.println("You did not enter a valid file name in the run arguments.");
	  System.out.println("Please enter a string to be parsed:");
	  Scanner scanner = new Scanner(System.in);
	  buffer = scanner.nextLine();
	}
	buffer += " ";
	
  }
  
  public Token getNextToken() {
	boolean f = false;
	boolean lineF = true;
	while (index < buffer.length() && ((int) buffer.charAt(index) == 32 || (int) buffer.charAt(index) == 13 || (int) buffer.charAt(index) == 9 || (int) buffer.charAt(index) == 10)) {
	  // if met the space and nextline, increase the index
	  if (((int) buffer.charAt(index) == 13 || (int) buffer.charAt(index) == 10) && lineF) { // solve the problem of '\n\r'
		lineN += 1; // set the line for each token
		lineF = false;
	  }
	  index += 1;
	}
	if (index == buffer.length() - 1 || index == buffer.length()) // get the end of file token
	  return new Token(EOFTOKEN, "-", lineN);
	char c = buffer.charAt(index);
	if (Character.isLetter(c)) {
	  return new Token(IDTOKEN, getIdentifier(), lineN); // get the ID token
	} else if (Character.isDigit(c)) {
	  return new Token(INTTOKEN, getInteger(), lineN); //get the INT token
	} else if (c == '=') {
	  index += 1;
	  return new Token(ASSMTTOKEN, "=", lineN); //get the = token
	} else if (c == '+') {
	  index += 1;
	  return new Token(PLUSTOKEN, "+", lineN); // get the + token
	} /*else if (c == '*') {
	  index += 1;
	  return new Token(MULTITOKEN, "*", lineN); // get the * token
	} else if (c == '/') {
	  index += 1;
	  return new Token(DIVIDETOKEN, "/", lineN); // get the / token
	} else if (c == '(') {
	  index += 1;
	  return new Token(LBRACKETTOKEN, "(", lineN); // get the ( token
	} else if (c == ')') {
	  index += 1;
	  return new Token(RBRACKETTOKEN, ")", lineN); // get the ) token
	}
	*/ else {
	  index += 1;
	  return new Token("UNKNOWN", Character.toString(c), lineN); // get the unknown token
	}
  }
  
  private String getIdentifier() { //get the ID from buffer
	int i = index;
	i += 1;
	while (Character.isLetter(buffer.charAt(i)) || Character.isDigit(buffer.charAt(i))) {
	  i += 1;
	}
	String s = buffer.substring(index, i);
	index = i;
	return s;
  }
  
  private String getInteger() { //get the INT from buffer
	int i = index;
	i += 1;
	while (Character.isDigit(buffer.charAt(i))) {
	  i += 1;
	}
	String s = buffer.substring(index, i);
	index = i;
	return s;
  }
  
  public List<Token> getAllTokens() {
	boolean f = false;
	List<Token> Tokens = new ArrayList<Token>();
	while (!f) {
	  Token t = getNextToken();
	  String s = t.getValue();
	  
	  
	  if (s != "-") { // is not the end of the file
		Tokens.add(t);
		System.out.println(t.toString());
	  } else {
		Tokens.add(t);
		System.out.println(t.toString());
		f = true; // set the f to exit the while loop
	  }
	}
	return Tokens;
  }
  
  public static void main(String[] args) {
	String fileName = "testNew.txt";
	if (args.length == 0) {
	  System.out.println("You must specify a file name");
	} else {
	
	  fileName = args[0];
	}
	Lexer lexer = new Lexer(fileName);
	System.out.println(lexer.buffer);
	List<Token> l = lexer.getAllTokens();
	System.out.println(l);
  }
  
}