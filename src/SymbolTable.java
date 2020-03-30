import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SymbolTable {
  private HashMap<String, Integer> map = new HashMap<String, Integer>();
  private int index = 0; // the address
  
  
  public void add(String key) { // add key into the hashmap
	map.put(key, index);
	index += 1;
  }
  
  public int getAddress(String key) { // getAddress from hashmap
	if (map.containsKey(key)) {
	  return map.get(key);
	} else {
	  return -1;
	}
  }
  
  @Override
  public boolean equals(Object o) { // equals method
	if (this == o) return true;
	if (!(o instanceof SymbolTable)) return false;
	SymbolTable that = (SymbolTable) o;
	return index == that.index &&
				   Objects.equals(map, that.map);
  }
  
  
  @Override
  public String toString() { // toString method
	return "SymbolTable:" + map;
  }
}
