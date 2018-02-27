package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

public class Tester {
	
	public static void main(String [] args) {

		testMakeVars();
		//testIndex();
		//testGetOpp();
		//testGetIndex();
		//testIsVar();
		//testIsArray();
		testEvaluateConsts();
		try {
			testVarsEvaluate();
			testVarsEvaluate2();//thank gerd
			testVarsEvaluate3();// no suprizes here 
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Tada! All tests passed.");
	}
	
	/*
	
	private static void testIsArray() {
		assert(Expression.isArray("b[]") );
		assert(!Expression.isArray("b+a[])") );
		assert(!Expression.isArray("b()") );
		assert(Expression.isArray("bmoo[3+1]") );
		assert(Expression.isArray("bmoo[3+1/bmoo[9]]") );
	}
	 */


	public static void testEvaluateConsts() {
		//System.out.println(Expression.evaluate("33+444", null, null) );
		assert(Expression.evaluate("33+444", null, null)== 477 );
		assert(Expression.evaluate("2+2", null, null)== 4 );
		assert(Expression.evaluate("2-2", null, null)== 0 );
		assert(Expression.evaluate("2*8", null, null)== 16 );
		assert(Expression.evaluate("8/2", null, null)== 4 );
		assert(Expression.evaluate("(2+8)/2", null, null)== 5 );
		assert(Expression.evaluate("(2-8)/3", null, null)== -2 );
		assert(Expression.evaluate("(5+3)/(3-1)", null, null)== 4 );
		assert(Expression.evaluate("(2+3*(5+3))/(3-1)", null, null)== 13 );
	}
	
	public static void testVarsEvaluate() throws IOException {
		ArrayList<Variable> vars = new ArrayList<>(); 
		ArrayList<Array> arrays = new ArrayList<>();
		Expression.makeVariableLists("A[4]+B[2]a+b+d", vars, arrays);
		String fname = "etest1.txt";
		Scanner scfile;
		try {
			scfile = new Scanner(new File(fname));
			Expression.loadVariableValues(scfile, vars, arrays);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		assert(Expression.evaluate("33+444", vars, arrays)== 477 );
		//System.out.println(Expression.evaluate("a+d+b", vars, arrays));
		assert(Expression.evaluate("a+b+d", vars, arrays)== 61 );
		assert(Expression.evaluate("3+a", vars, arrays)== 6 );
		
		assert(Expression.evaluate("3+B[2]", vars, arrays)== 4 );
		assert(Expression.evaluate("B[A[0]+2]", vars, arrays)== 1 );//nested test
		assert(Expression.evaluate("B[A[0]+a-1]", vars, arrays)== 1 );//nested test
		assert(Expression.evaluate("B[A[0]+a-1]/10", vars, arrays)== (float)0.1 );//nested with outside opps
		assert(Expression.evaluate("B[0]+A[0]", vars, arrays)== 0 );//side by side
		assert(Expression.evaluate("B[(3+(a-2)/(b-1))/2]+A[B[(b-10)/(-4)]+1]", vars, arrays)== 4 );//side by side
		assert(Expression.evaluate("B  [ (3 	+(	a	-2	 ) /		(  b -		  	1	) 	 	) 	 	/	  		2 ]	+ 		A [ 		 B  [(  	b	-1		0   )/( -4  )]	+1]", vars, arrays)== 4 );//now with white spaces!! 
		//System.out.println(Expression.evaluate("(a + A[a*2-b])", vars, arrays) );//side by side
		assert(Expression.evaluate("(a + A[a*2-b])", vars, arrays)== 8 );//Sesh
		assert(Expression.evaluate("a - (b+A[B[2]])*d + 3", vars, arrays)== -106.0 );//Sesh
		assert(Expression.evaluate("3*-2", vars, arrays)==-2);
		
		
		
	}
	
	
	public static void testVarsEvaluate2() throws IOException {
		ArrayList<Variable> vars = new ArrayList<>(); 
		ArrayList<Array> arrays = new ArrayList<>();
		Expression.makeVariableLists("B  [ (3 	+(	a	-2	 ) /		(  b -		  	1	) 	 	) 	 	/	  		2 ]	+ 		A [ 		 B  [(  	b	-1		0   )/( -4  )]	+1]", vars, arrays);
		String fname = "etest1.txt";
		Scanner scfile;
		try {
			scfile = new Scanner(new File(fname));
			Expression.loadVariableValues(scfile, vars, arrays);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		assert(Expression.evaluate("B  [ (3 	+(	a	-2	 ) /		(  b -		  	1	) 	 	) 	 	/	  		2 ]	+ 		A [ 		 B  [(  	b	-1		0   )/( -4  )]	+1]", vars, arrays)== 4 );//now with white spaces!! 
		
		
		
	}
	
	public static void testVarsEvaluate3() throws IOException {
		ArrayList<Variable> vars = new ArrayList<>(); 
		ArrayList<Array> arrays = new ArrayList<>();
		Expression.makeVariableLists("B[(3+(a-2)/(b-1))/2]+A[B[(b-10)/(-4)]+1]", vars, arrays);
		String fname = "etest1.txt";
		Scanner scfile;
		try {
			scfile = new Scanner(new File(fname));
			Expression.loadVariableValues(scfile, vars, arrays);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		assert(Expression.evaluate("B[(3+(a-2)/(b-1))/2]+A[B[(b-10)/(-4)]+1]", vars, arrays)== 4 );//now with white spaces!! 
		
		
		
	}

	
	/*
	public static void testIsVar() {
		assert(Expression.isVar("X"));
		assert(!Expression.isVar("4"));
		assert(Expression.isVar("Bobby"));
		assert(!Expression.isVar("Bobby[]"));
		assert(!Expression.isVar("Bobby[4]"));
	}
	
	public static void testGetIndex() {
		assert(Expression.getIndex("+,45")==45);
		assert(Expression.getIndex("+,99")==99);	
	}
	
	public static void testGetOpp() {
		assert(Expression.getOpp("+,45").equals("+"));
		assert(Expression.getOpp("-,9999").equals("-"));
		assert(Expression.getOpp("*,4").equals("*"));
		assert(Expression.getOpp("/,01").equals("/"));
		assert(Expression.getOpp("/01").equals(""));
		assert(Expression.getOpp(",").equals(""));
		assert(Expression.getOpp("").equals(""));
	}
	
	public static void testIndex() {
		String oppandI =Expression.opAndIndex("3+4");
		assert(oppandI.equals("+,1"));
		oppandI =Expression.opAndIndex("3-4");
		assert(oppandI.equals("-,1"));
		oppandI =Expression.opAndIndex("3/4");
		assert(oppandI.equals("/,1"));
		oppandI =Expression.opAndIndex("3*4");
		assert(oppandI.equals("*,1"));
		
		
		oppandI =Expression.opAndIndex("(3+4)");
		assert(oppandI.equals(","));
		oppandI =Expression.opAndIndex("(3-4)");
		assert(oppandI.equals(","));
		oppandI =Expression.opAndIndex("34");
		assert(oppandI.equals(","));
		oppandI =Expression.opAndIndex("3*4-1");
		assert(oppandI.equals("-,3"));
		oppandI =Expression.opAndIndex("3*(4-1)");
		assert(oppandI.equals("*,1"));
		oppandI =Expression.opAndIndex("(3-4)+1)");
		assert(oppandI.equals("+,5"));
		oppandI =Expression.opAndIndex("3/(4-1)");
		assert(oppandI.equals("/,1"));
		oppandI =Expression.opAndIndex("3+(4-1)");
		assert(oppandI.equals("+,1"));
	}
	
	*/
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
