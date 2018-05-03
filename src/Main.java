//Noah D Garner
//Eclipse IDE
//04/5/2018
//PHASE 2
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
 
public class Main {
	public static void main(String[] args) {
	    String line = null;
	    Lexer lexer = new Lexer();
	    ArrayList<Token> tokens = new ArrayList<Token>();
	    try { 
	    	System.out.println("Scanner");
	        FileReader fileReader = new FileReader(args[0]);
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        while((line = bufferedReader.readLine()) != null) {	
	        	tokens.addAll(lexer.lex(line));        
	        }
	        bufferedReader.close(); 
	    }
	    //something wrong with the file
	    catch(FileNotFoundException ex) {
	        System.out.println(
	            "Unable to open file '" + 
	            args[0] + "'");                
	    }
	    catch(IOException ex) {
	        System.out.println("Error reading file '" + args[0] + "'");                  
	    }
	    for (Token token : tokens)
        	System.out.println(token.toString());
	    
	    for(Token token : tokens) {
	    	if(token.type==TokenType.ERROR) {
		    	System.out.println("Error, something went wrong.");
		    	System.exit(0);
		    }
	    }
	    System.out.println("\nParser");
	    Parser parser = new Parser(tokens);
	    ParseTree pt = parser.parse();
	    pt.print(0);
	    System.out.println("\nEvaluator");
	    new Evaluator(pt);

	 }
}
