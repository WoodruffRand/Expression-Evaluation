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
    		char delm = '1';
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
    
    public static boolean isVar(String str) {
    	for(int i = 0; i < str.length(); i++) {
    		if( !Character.isLetter( str.charAt(i) ) ) {
    			return false;
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
    	if(isVar(expr)) return getVar(expr,vars);
    	if(isArray(expr)) return getArray(expr, vars, arrays);
    	if(isNumber(expr)) return Integer.parseInt(expr);
    	String opAndIndex =opAndIndex(expr); //opp,index
    	
    	String currOpp = getOpp(opAndIndex); //substring before comma
    	if(  currOpp.equals("") && (expr.charAt(0)=='(')&& (expr.charAt(expr.length()-1)==')')     )
    		return evaluate(expr.substring(1,expr.length()-1), vars, arrays); //substring each side by 1
    	
    	int oppIndex  = getIndex(opAndIndex); // substring after comma
    	String lh = expr.substring(0,oppIndex);
    	String rh = expr.substring(oppIndex+1,expr.length());
    	
    	if(currOpp.equals("*")) return evaluate(lh,vars, arrays)*evaluate(rh,vars, arrays);
    	if(currOpp.equals("/")) return evaluate(lh,vars, arrays)/evaluate(rh,vars, arrays);
    	if(currOpp.equals("+")) return evaluate(lh,vars, arrays)+evaluate(rh,vars, arrays);
    	if(currOpp.equals("-")) return evaluate(lh,vars, arrays)-evaluate(rh,vars, arrays);
    	return -666666666;
    }
    /**
     * Searches string expression for lowest order unbound index
     * if no index found return empty string 
     * empty string either means no opps present or they are all encapsulated in parentheses 
     * @param exp The expression to be evaluated as a string
     * @return "operand,index"
     */
    //TODO make this private before submitting
    public static String opAndIndex(String exp) {
    	String rtn =",";
    	String opps = "+-*/";
    	for(int i =0 ; i<opps.length(); i++) {
    		int depth = 0;
    		for(int j = 0; j<exp.length(); j++) {
    			if(exp.charAt(j) == '(' || exp.charAt(j) == '[') depth--;
    			if(exp.charAt(j) == ')' || exp.charAt(j) == ']') depth++;
    			if(depth >=0 && exp.charAt(j)== opps.charAt(i) ) return opps.charAt(i)+","+j;
    		}
    	}
    	return rtn; //no unbound opp found
    }
    
    //TODO make this private before submisting
    public static String getOpp(String oppAndI) {
    	int i = oppAndI.indexOf(",");
    	if(oppAndI.equals(",")) return "";
    	if(i<0) return"";
    	
    	return oppAndI.substring(0, i);
    }
    //TODO make this private
    public static int getIndex(String oi) {
    	String subStr = oi.substring(oi.indexOf(",")+1, oi.length());
    	return Integer.parseInt(subStr);
    }
    
    //TODO make java docs
    private static int getVar(String var, ArrayList<Variable> vars) {
    	for(int i = 0 ; i <vars.size(); i++) {
    		if (vars.get(i).name.equals( var)) return vars.get(i).value;
    		
    	}
    	return -66666666;//hopefully this will set off some red flags 
    }
    
    //TODO make this private 
    public static boolean isArray(String str) {
    	if(str.equals("")) return false;
    	if(!Character.isLetter(str.charAt(0) ) ) return false;
    	int i = 1;
    	while(Character.isLetter(str.charAt(i) )) {
    		i++;
    	}
    	char c = str.charAt(str.length()-1);
    	if(str.charAt(i) =='['&& str.charAt(str.length()-1) ==']') return true;
    	
    	return false;
    	
    }
    
    //TODO make private
    public static float getArray(String ary, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	int split = ary.indexOf('[');
    	String interior = ary.substring(split+1,ary.length()-1);
    	String arrayName = ary.substring(0,split);
    	
    	for(int i = 0; i<arrays.size(); i++ ) {
    		if (arrays.get(i).name.equals(arrayName)) {
    			return arrays.get(i).values[(int)evaluate(interior,vars,arrays)];
    		}
    	}
    	return -666666666;
    }
}
