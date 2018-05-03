//Noah D Garner
//5-1-2018
//ECLIPSE IDE
//PHASE 3
import java.util.HashMap;

public class Evaluator {
	
	boolean skip = false;
	HashMap<String, String> table = new HashMap<String, String>();
	
	public Evaluator (ParseTree tree) {
		EvaluateStatement(tree);
		printEvaluator();
	}
	
	public void EvaluateStatement (ParseTree tree) {
		if(compare(tree,";"))	{
			EvaluateStatement(tree.children.get(0));
			EvaluateBaseStatement(tree.children.get(1));
		}
		else
			EvaluateBaseStatement(tree);
	}
	
	public void EvaluateBaseStatement (ParseTree tree) {
		if(compare(tree,"skip")) {
			skip = true;
			return;
		}
		EvaluateAssignment(tree);
		EvaluateIfStatement(tree);
		EvaluateWhileStatement(tree);
	}
	
	public void EvaluateAssignment (ParseTree tree) {
		if(compare(tree,":=")) { 
			String x = getID(tree.children.get(0));
			String y = EvaluateExpression(tree.children.get(1));
			table.put(x,y);
		}
	}
	
	public void EvaluateIfStatement (ParseTree tree) {
		if(compare(tree,"if")) {
			if(Boolean.parseBoolean(EvaluateBoolExpression(tree.children.get(0)))) {
				EvaluateStatement(tree.children.get(1));}
			else {
				EvaluateStatement(tree.children.get(2));
			}
		}
	}
	
	public void EvaluateWhileStatement (ParseTree tree) {
		if(compare(tree,"while")) {
			while(Boolean.parseBoolean(EvaluateBoolExpression(tree.children.get(0)))) {
				EvaluateStatement(tree.children.get(1));
				if (skip) {
					skip = false;
					break;
				}
			}
		}
	}
	
	public String EvaluateExpression (ParseTree tree) {
		if(EvaluateNumExpression(tree) == null)
			return EvaluateBoolExpression(tree);
		return EvaluateNumExpression(tree);
	}
	
	public String EvaluateNumExpression (ParseTree tree) {
		if(compare(tree,"=") || compare(tree,"|") || compare(tree,"&") || compare(tree,"!") || tree.root.type == TokenType.BOOL)
			return null;
		if(compare(tree,"+")) {
			int expression = Integer.parseInt(EvaluateNumTerm(tree.children.get(0)))+
							 Integer.parseInt(EvaluateNumTerm(tree.children.get(1)));
			return Integer.toString(expression);
		}
		return EvaluateNumTerm(tree);
	}
	
	public String EvaluateNumTerm (ParseTree tree) {
		if(compare(tree,"-")) {
			int expression = Integer.parseInt(EvaluateNumFactor(tree.children.get(0)))-
							 Integer.parseInt(EvaluateNumFactor(tree.children.get(1)));
			return Integer.toString(expression);
		}
		return EvaluateNumFactor(tree);
	}
	
	public String EvaluateNumFactor (ParseTree tree) {
		if(compare(tree,"/")) {
			int expression = Integer.parseInt(EvaluateNumPiece(tree.children.get(0)))
							/Integer.parseInt(EvaluateNumPiece(tree.children.get(1)));
			return Integer.toString(expression);
		}
		return EvaluateNumPiece(tree);
	}
	
	public String EvaluateNumPiece (ParseTree tree) {
		if(compare(tree,"*")) {
			int expression = Integer.parseInt(EvaluateNumElement(tree.children.get(0)))*
							 Integer.parseInt(EvaluateNumElement(tree.children.get(1)));
			return Integer.toString(expression);
		}
		return EvaluateNumElement(tree);
		
	}
	
	public String EvaluateNumElement (ParseTree tree) {
		if(tree.root.type == TokenType.NUMBER)
			return getNumber(tree);
		if(tree.root.type == TokenType.IDENTIFIER)
			return table.get(getID(tree));
		else
			return EvaluateNumExpression(tree);
	}
	
	public String EvaluateBoolExpression (ParseTree tree) {
		if(compare(tree,"=")) {
			boolean expression = Boolean.parseBoolean(EvaluateBoolTerm(tree.children.get(0)))==
							 	 Boolean.parseBoolean(EvaluateBoolTerm(tree.children.get(1)));
			return Boolean.toString(expression);
		}
		return EvaluateBoolTerm(tree);
	}
	
	public String EvaluateBoolTerm (ParseTree tree) {
		if(compare(tree,"|")) {
			boolean expression = Boolean.parseBoolean(EvaluateBoolFactor(tree.children.get(0)))||
								 Boolean.parseBoolean(EvaluateBoolFactor(tree.children.get(1)));
			return Boolean.toString(expression);
		}
		return EvaluateBoolFactor(tree);
	}
	
	public String EvaluateBoolFactor (ParseTree tree) {
		if(compare(tree,"&")) {
			boolean expression = Boolean.parseBoolean(EvaluateBoolPiece(tree.children.get(0)))&&
							 	 Boolean.parseBoolean(EvaluateBoolPiece(tree.children.get(1)));
			return Boolean.toString(expression);
		}
		return EvaluateBoolPiece(tree);
	}
	
	public String EvaluateBoolPiece (ParseTree tree) {
		if(compare(tree,"!")) {
			boolean expression = Boolean.parseBoolean(EvaluateBoolElement(tree.children.get(0)));
			return Boolean.toString(!expression);
		}
		return EvaluateBoolElement(tree);
	}
	
	public String EvaluateBoolElement (ParseTree tree) {
		if(compare(tree,"==")) {
			boolean expression = Integer.parseInt(EvaluateNumExpression(tree.children.get(0)))==
					Integer.parseInt(EvaluateNumExpression(tree.children.get(1)));
			return Boolean.toString(expression);
		}
		if(tree.root.type == TokenType.BOOL)
			return getBool(tree);
		if(tree.root.type == TokenType.IDENTIFIER)
			return table.get(getID(tree));
		else
			return EvaluateBoolExpression(tree);
	}
	
	public void printEvaluator () {
		//obtained from https://stackoverflow.com/questions/5920135/printing-hashmap-in-java
		System.out.println("Output:");  
		for (String name: table.keySet()){
            String key =name.toString();
            String value = table.get(name).toString();  
            System.out.println(key + " = " + value);  
		} 
	}
	//helpers
	public Boolean compare(ParseTree tree, String x) {
		return tree.root.data.equals(x);
	}
	public String getID(ParseTree tree) {
		if(tree.root.type.equals(TokenType.IDENTIFIER))
				return tree.root.data;
		return null;
	}
	public String getNumber(ParseTree tree) {
		if(tree.root.type.equals(TokenType.NUMBER))
				return tree.root.data;
		return null;
	}
	public String getBool(ParseTree tree) {
		if(tree.root.type.equals(TokenType.BOOL))
				return tree.root.data;
		return null;
	}
}
