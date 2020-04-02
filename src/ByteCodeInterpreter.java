import java.util.ArrayList;
import java.util.Objects;

public class ByteCodeInterpreter {
  private ArrayList<Integer> bytecode = new ArrayList<Integer>(); //hold the code for a program
  private ArrayList<Integer> memory = new ArrayList<Integer>(); // hold the main memory array
  private int accumulator = 0;
  private int memorySize; //the size of memory
  private boolean f = true; // set to see if there is an address out of range error for toString method
  public static final int LOAD = 0;
  public static final int LOADI = 1;
  public static final int STORE = 2; // constants for the commands
  
  public ByteCodeInterpreter(int size) {
	memorySize = size;
  }
  // specify the size of memory
  
  public void generate(int command, int operand) { //takes in command and operand
	bytecode.add(command);
	bytecode.add(operand); // add it to the bytecode arraylist
  }
  
  public void run() {
    memory.clear();
	for (int i = 0; i < bytecode.size(); i += 2) {
	  if (bytecode.get(i) == LOAD) { // for LOAD command
		accumulator += memory.get(bytecode.get(i + 1));
	  } else if (bytecode.get(i) == LOADI) { // for LOADI command
		accumulator += bytecode.get(i + 1);
	  } else if (bytecode.get(i) == STORE) { // for STORE command
		if (bytecode.get(i + 1) < memorySize) {
		  memory.add(bytecode.get(i + 1), accumulator);
		  accumulator = 0;
		} else {
		  f = false; // set f to false for out of range
		  break;
		}
	  }
	}
	for (int i = bytecode.size() / 2; i < memorySize; i++) {
	  memory.add(0); //add zero to the arraylist that did not filled
	}
  }
  
  public ArrayList<Integer> getBytecode() {
	return bytecode;
  }
  
  public void remove() { // remove the last two bytecode
	bytecode.remove(bytecode.size() - 1);
	bytecode.remove(bytecode.size() - 1);
  }
  
  public ArrayList<Integer> getMemory() {
	return memory;
  }
  
  @Override
  public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof ByteCodeInterpreter)) return false;
	ByteCodeInterpreter that = (ByteCodeInterpreter) o;
	return memorySize == that.memorySize &&
				   Objects.equals(getBytecode(), that.getBytecode()) &&
				   Objects.equals(memory, that.memory);
  } // equals method
  
  @Override
  public String toString() {
	if (f) {
	  return "Bytecode:" + bytecode + "\nMemory:" + memory;
	} else {
	  return "Error: Address out of range\nBytecode:" + bytecode + "\nMemory:" + memory;
	}
  } // toString method
  
  public static void main(String args[]) {
	ByteCodeInterpreter byteCodeInterpreter = new ByteCodeInterpreter(20);
	byteCodeInterpreter.generate(1, 4);
	byteCodeInterpreter.generate(2, 0);
	byteCodeInterpreter.generate(0, 0);
	byteCodeInterpreter.generate(2, 1);
	System.out.println(byteCodeInterpreter.bytecode);
	byteCodeInterpreter.run();
	System.out.println(byteCodeInterpreter.memory); //for test
  }
}
