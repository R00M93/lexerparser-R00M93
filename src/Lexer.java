import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to build an array of Tokens from an input file
 * @author wolberd
 * @see Token
 * @see Parser
 * @see ByteCodeInterpreter
 */
public class Lexer {

    String buffer;
    int index = 0;
    public static final String INTTOKEN="INT";
    public static final String IDTOKEN="ID";
    public static final String ASSMTTOKEN="ASSMT";
    public static final String PLUSTOKEN="PLUS";
    public static final String EOFTOKEN="EOF";

    /**
     * call getInput to get the file data into our buffer
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
    private void getInput(String fileName)  {
        try {
            Path filePath = Paths.get(fileName);
            byte[] allBytes = Files.readAllBytes(filePath);
            buffer = new String (allBytes);
        } catch (IOException e) {
            System.out.println ("You did not enter a valid file name in the run arguments.");
            System.out.println ("Please enter a string to be parsed:");
            Scanner scanner = new Scanner(System.in);
            buffer=scanner.nextLine();
        }



    }


    public static void main(String[] args) {
        String fileName="";
        if (args.length==0) {
            System.out.println("You must specify a file name");
        } else {

            fileName=args[0];
        }
        Lexer lexer = new Lexer(fileName);

        System.out.println(lexer.buffer);


    }
}
	