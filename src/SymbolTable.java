import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SymbolTable {
  HashMap<String, Integer> map = new HashMap<String, Integer>();
  int index = 0;

  
  void add(String key) {
  	map.put(key, index);
  	index += 1;
  }
  
  int getAddress(String key) {
  	if (map.containsKey(key)) {
  	  return map.get(key);
	} else {
  	  return -1;
	}
  }
  
  public String toString(String key) {
    String s = "";
    s = s + key + " " + map.get(key);
    return s;
  }
}
