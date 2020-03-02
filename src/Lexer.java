import javax.print.DocFlavor;
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
  
  String buffer;
  int index = 0;
  int lineN = 1;

  public static final String INTTOKEN = "INT";
  public static final String IDTOKEN = "ID";
  public static final String ASSMTTOKEN = "ASSMT";
  public static final String PLUSTOKEN = "PLUS";
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
	String s = "";
	
	while (index < buffer.length() && ((int) buffer.charAt(index) == 32 || (int) buffer.charAt(index) == 13 || (int) buffer.charAt(index) == 9 || (int) buffer.charAt(index) == 10)) {
	  if ((int) buffer.charAt(index) == 13) {
	    lineN += 1;
	    //System.out.println(lineN);
	  }
	  index += 1;
	}
	if (index == buffer.length() - 1 || index == buffer.length())
	  return new Token(EOFTOKEN, "-", lineN);
	char c = buffer.charAt(index);
	if (Character.isLetter(c)) {
	  return new Token(IDTOKEN, getIdentifier(), lineN);
	} else if (Character.isDigit(c)) {
	  return new Token(INTTOKEN, getInteger(), lineN);
	} else if (c == '=') {
	  index += 1;
	  return new Token(ASSMTTOKEN, "=", lineN);
	} else if (c == '+') {
	  index += 1;
	  return new Token(PLUSTOKEN, "+", lineN);
	} else {
	  index += 1;
	  return new Token("UNKNOWN", Character.toString(c), lineN);
	}
  }
  
  private String getIdentifier() {
	int i = index;
	i += 1;
	while (Character.isLetter(buffer.charAt(i)) || Character.isDigit(buffer.charAt(i))) {
	  i += 1;
	}
	String s = buffer.substring(index, i);
	index = i;
	return s;
  }
  
  private String getInteger() {
	int i = index;
	i += 1;
	while (Character.isDigit(buffer.charAt(i))) {
	  i += 1;
	}
	String s = buffer.substring(index, i);
	index = i;
	return s;
  }
  
  List<Token> getAllTokens() {
	boolean f = false;
	List<Token> Tokens = new ArrayList<Token>();
	while (!f) {
	  Token t = getNextToken();
	  String s = t.getValue();
	 
	  
	  if (s != "-") {
		Tokens.add(t);
		System.out.println(t.toString());
	  } else {
	    Tokens.add(t);
	    System.out.println(t.toString());
		f = true;
	  }
	}
	return Tokens;
  }
  
  public static void main(String[] args) {
	String fileName = "testId.txt";
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