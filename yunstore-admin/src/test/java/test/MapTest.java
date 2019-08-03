package test;

import java.util.HashMap;
import java.util.Set;


public class MapTest {

	public static void map() {
		HashMap<String, String> map = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			map.put("key"+i, "value"+i);
		}
		Set<String> keySet = map.keySet();
		for(String string : keySet) {
			System.out.println(string + ":" + map.get(string));
		}
	}
	
	
	
	public static void main(String[] args) {
		map();
	}
	
	
}
