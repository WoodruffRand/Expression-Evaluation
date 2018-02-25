package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	Scanner sc = new Scanner(expr);
    	sc.useDelimiter("\\[|\\]|\\)|\\(|/|\\*|-|\\+");
    	
    	
    	while(sc.hasNext()) {
    		String v = sc.next();
    		int index = expr.indexOf(v)+v.length();
    		char delm = ' ';
    		if(index <expr.length()-1) delm = expr.charAt(index);
    		if(delm == '[') {
    			Array temp =new Array(v);
    			arrays.add(temp);
    		}else if(!isNumber(v)) {
    			Variable temp =new Variable(v);
    			vars.add(temp);
    		}
    		
    	}
    	sc.close();
    }
    
    private static boolean isNumber(String str) {
    	for( int i = 0; i<str.length(); i++) {
    		if(str.charAt(i)<'0' || str.charAt(i)>'9') {
    			 if(str.charAt(i) != '.')return false;
    		}
    		
    	}
    	return true;
    }
    
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	// TODO make this dumb thing work
    	//if(isVar(exp)) return getVar(expr,vars);
    	//if(isArray) return getArray(expr, arrays);
    	if(isNumber(expr)) return Integer.parseInt(expr);
    	String opAndIndex =opAndIndex(expr);
    	
    	String currOpp = ""; //substring before comma
    	if(currOpp.equals("") && expr.charAt(0)=='('&& expr.charAt(expr.length()-1)==')')
    		return evaluate(expr, vars, arrays); //substring each side by 1
    	
    	int oppIndex  = Integer.parseInt("1"); // substring after comma
    	String lh ="";
    	String rh = "";
    	
    	if(currOpp.equals("*")) return evaluate(lh,vars, arrays)*evaluate(rh,vars, arrays);
    	if(currOpp.equals("/")) return evaluate(lh,vars, arrays)/evaluate(rh,vars, arrays);
    	if(currOpp.equals("+")) return evaluate(lh,vars, arrays)+evaluate(rh,vars, arrays);
    	if(currOpp.equals("-")) return evaluate(lh,vars, arrays)-evaluate(rh,vars, arrays);
    	 
    		
    	return -666;
    	//return evaluate;
    }
    
    public static String opAndIndex(String exp) {
    	String rtn ="";
    	String opps = "*/+-";
    	
    	for(int i =0 ; i<opps.length(); i++) {
    		int depth = 0;
    		for(int j = 0; j<exp.length(); j++) {
    			if(exp.charAt(j) == '(' || exp.charAt(j) == '[') depth--;
    			if(exp.charAt(j) == ')' || exp.charAt(j) == ']') depth++;
    			
    			if(depth >0 && exp.charAt(j)== opps.charAt(i) ) return opps.charAt(i)+","+i;
    			
    		}
    	}
    	return ""; //no unbound opp found
    	
    }
}
