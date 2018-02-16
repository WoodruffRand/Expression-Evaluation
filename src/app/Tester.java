package app;

import java.util.ArrayList;

public class Tester {
	
	public static void main(String [] args) {
		ArrayList<Variable> vars = new ArrayList<>(); 
		ArrayList<Array> arrays = new ArrayList<>();
		
		Expression.makeVariableLists("4*miners[10]-9/(y+20)", vars, arrays);
		
	}

}
