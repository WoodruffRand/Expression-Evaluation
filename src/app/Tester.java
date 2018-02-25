package app;

import java.util.ArrayList;

public class Tester {
	
	public static void main(String [] args) {

		testMakeVars();
		testIndex();
		
	}
	
	public static void testIndex() {
		String oppandI =Expression.opAndIndex("3+4");
		System.out.println(oppandI);
	}
	
	public static void testMakeVars() {
		testMakeVars("4*22-9/(8+20)", 0, 0);
		testMakeVars("4*Miners[3+7]*22-9/(20)", 0 ,1);
		testMakeVars("4*(3+7)*22-9/(y+20)", 1 ,0);
		testMakeVars("4*Miners[3+7]*22-9/(y+20)", 1 ,1);
		testMakeVars("4*Miners[3+7]*(3*x)/(z-2)+m*n-22-9/(y+20)+students[builders[graders[]]]", 5 ,4); //lets go NUTS
	}
	
	public static void testMakeVars(String s, int v, int a) {
		ArrayList<Variable> vars = new ArrayList<>(); 
		ArrayList<Array> arrays = new ArrayList<>();
		Expression.makeVariableLists(s, vars, arrays);
		
		assert(arrays.size()== a);
		assert(vars.size()== v);
		//System.out.println("passed make vars");
		
	}


}
