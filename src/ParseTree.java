//Noah D Garner
//Eclipse IDE
//04/5/2018
//PHASE 2
import java.util.ArrayList;

public class ParseTree {
	private static final String TAB = "        ";
	Token root;
	ArrayList<ParseTree> children;
	public ParseTree(Token r, ArrayList<ParseTree> p) {
		root = r;
		children = p;
	}
	public void print(int numTabs) {
		for(int i=0;i<numTabs;i++)
			System.out.print(TAB);
		System.out.println(root.toString());
		numTabs++;
		if(!(children==null))
			for(ParseTree child: children)
				child.print(numTabs);
	}
}
