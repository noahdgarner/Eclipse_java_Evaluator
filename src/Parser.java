//Noah D Garner
//Eclipse IDE
//04/5/2018
//PHASE 2
import java.util.ArrayList;
import java.util.Iterator;

public class Parser {
	Iterator<Token> Tokens;
	Token currToken;
	
	public ParseTree parse() {
		return parse_statement();
	}
	public Parser(ArrayList<Token> toks)   {
		Tokens = toks.iterator();
		consumeToken();
	}
	public void consumeToken() {
		if(!Tokens.hasNext())
			currToken = null;
		else
			currToken = Tokens.next();
	}
	
	public ParseTree parse_statement() {
		ParseTree tree = parse_basestatement();
		while(currToken != null &&currToken.data.equals(";")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(tree);
			ParseTree tempTree = parse_basestatement();
			if(tempTree==null)
				return new ParseTree(root,templist);
			templist.add(tempTree);
			tree = new ParseTree(root,templist);
		}
		return tree;
	}
	
	public ParseTree parse_basestatement() {
		if(currToken != null && currToken.data.equals("skip")) {
			Token root = currToken;
			consumeToken();
			return new ParseTree(root,null);
		}
		else {
			ParseTree tree = parse_assignment();
			if(tree == null) {
				tree = parse_ifstatement();
			}
			if (tree == null) {
				tree = parse_whilestatement();
			}
			return tree;
		}
	}

	public ParseTree parse_assignment() {
		if(currToken != null&&currToken.type==TokenType.IDENTIFIER) {
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(new ParseTree(currToken,null));
			consumeToken(); 
			if(currToken != null && currToken.data.equals(":=")) {
				Token root = currToken;
				consumeToken();
				templist.add(parse_expression());
				return new ParseTree(root,templist);
			}
		}
		return null;
	}
	
	public ParseTree parse_ifstatement() {
		if(currToken != null&&currToken.data.equals("if")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(parse_boolexpression());
			if(currToken.data.equals("then")) {
				consumeToken();
				templist.add(parse_statement());
			}
			else
				return null;
			if(currToken.data.equals("else")) {
				consumeToken();
				templist.add(parse_statement());
			}
			else
				return null;
			if(currToken.data.equals("endif")) {
				consumeToken();
			}
			else
				return null;
			return new ParseTree(root,templist);
		}
		return null;
	}
	
	public ParseTree parse_whilestatement() {
		if(currToken != null&&currToken.data.equals("while")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(parse_boolexpression());
			if(currToken.data.equals("do")) {
				consumeToken();
				templist.add(parse_statement());
			}
			else
				return null;
			if(currToken.data.equals("endwhile")) {
				consumeToken();
			}
			else
				return null;
			return new ParseTree(root,templist);
		}
		return null;
	}
	
	public ParseTree parse_expression() {
		ParseTree tree = parse_numexpression();
		if(tree == null) {
			tree = parse_boolexpression();
		}
		return tree;
	}

	public ParseTree parse_numexpression() {
		ParseTree tree = parse_numterm();
		while(currToken != null && currToken.data.equals("+")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(tree);
			templist.add(parse_numterm());
			tree = new ParseTree(root,templist);
		}	
		return tree;
	}
		
	public ParseTree parse_numterm() {
		ParseTree tree = parse_numfactor();
		while(currToken != null &&currToken.data.equals("-")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(tree);
			templist.add(parse_numfactor());
			tree = new ParseTree(root,templist);
		}	
		return tree;
	}
	
	public ParseTree parse_numfactor() {
		ParseTree tree = parse_numpiece();
		while(currToken != null &&currToken.data.equals("/")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(tree);
			templist.add(parse_numpiece());
			tree = new ParseTree(root,templist);
		}	
		return tree;
	}
	
	public ParseTree parse_numpiece() {
		ParseTree tree = parse_numelement();
		while(currToken != null &&currToken.data.equals("*")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(tree);
			templist.add(parse_numelement());
			tree = new ParseTree(root,templist);
		}	
		return tree;
	}

	public ParseTree parse_numelement() {
		if(currToken.type==TokenType.NUMBER ||currToken.type==TokenType.IDENTIFIER) {
			ParseTree tree = new ParseTree(currToken,null); //leaf node no children
			consumeToken();
			return tree;
		}
		else if (currToken.data.equals("(")){
			consumeToken();
			ParseTree tree = parse_numexpression();
			assert(currToken.data.equals(")"));
			consumeToken();
			return tree;
		} else {
			System.out.println("Runtime error");
			return null;}
	} 
	
	//iterates 0 or 1
	public ParseTree parse_boolexpression() {
		ParseTree tree = parse_boolterm();
		if(currToken != null &&currToken.data.equals("=")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(tree);
			templist.add(parse_boolterm());
			tree = new ParseTree(root,templist);
		}
		return tree;
	}

	public ParseTree parse_boolterm() {
		ParseTree tree = parse_boolfactor();
		while(currToken != null &&currToken.data.equals("|")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(tree);
			templist.add(parse_boolfactor());
			tree = new ParseTree(root,templist);
		}
		return tree;
	}
	
	public ParseTree parse_boolfactor() {
		ParseTree tree = parse_boolpiece();
		while(currToken != null &&currToken.data.equals("&")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(tree);
			templist.add(parse_boolpiece());
			tree = new ParseTree(root,templist);
		}
		return tree;
	}
	
	public ParseTree parse_boolpiece() {
		if(currToken != null && currToken.data.equals("!")) {
			Token root = currToken;
			consumeToken();
			ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
			templist.add(parse_boolelement());
			return new ParseTree(root,templist);
		}
		else {
			return parse_boolelement();
		}
	}

	public ParseTree parse_boolelement() {
		ParseTree tree;
		if (currToken.data.equals("(")){
			consumeToken();
			tree = parse_boolexpression();
			assert(currToken.data.equals(")"));
			consumeToken();
		} else
			tree = null;
		
		if (tree == null) {
			tree = parse_numexpression();
			if (tree != null) {
				if(currToken != null && currToken.data.equals("==")) {
					Token root = currToken;
					consumeToken();
					ArrayList<ParseTree> templist = new ArrayList<ParseTree>();
					templist.add(tree);
					templist.add(parse_numexpression());
					tree = new ParseTree(root,templist);
				}
			} 
			else {
				if(currToken.type==TokenType.BOOL ||currToken.type==TokenType.IDENTIFIER) {
					tree = new ParseTree(currToken,null); //leaf node no children
					consumeToken();
				} 
				else
					tree = null;
			}
		}
		return tree;
	}
}