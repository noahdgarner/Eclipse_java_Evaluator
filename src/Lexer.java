//Noah D Garner
//Eclipse IDE
//04/5/2018
//PHASE 2
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    //list of tokens
    public Lexer() {}
    
    public ArrayList<Token> lex(String input) {
	    ArrayList<Token> tokens = new ArrayList<Token>();
	    StringBuffer tokenPatternsBuffer = new StringBuffer();
	    
	    for (TokenType tokenType : TokenType.values())
	    	tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
	    
	    Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));
	    Matcher matcher = tokenPatterns.matcher(input);
	    
	    while (matcher.find()) {
		      if (matcher.group(TokenType.NUMBER.name()) != null) {
		        tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
		        continue;} 
		      if (matcher.group(TokenType.IDENTIFIER.name()) != null) {
	    	    tokens.add(new Token(TokenType.IDENTIFIER, matcher.group(TokenType.IDENTIFIER.name())));
	    	    continue;}
		      else if (matcher.group(TokenType.PUNCTUATION.name()) != null) {
		        tokens.add(new Token(TokenType.PUNCTUATION, matcher.group(TokenType.PUNCTUATION.name())));
		        continue;} 
		      else if (matcher.group(TokenType.KEYWORD.name()) != null) {
			    tokens.add(new Token(TokenType.KEYWORD, matcher.group(TokenType.KEYWORD.name())));
			    continue;}
		      else if (matcher.group(TokenType.BOOL.name()) != null) {
			    tokens.add(new Token(TokenType.BOOL, matcher.group(TokenType.BOOL.name())));
			    continue;}
		      else if (matcher.group(TokenType.WHITESPACE.name()) != null)
		        continue;
		      else {
		        tokens.add(new Token(TokenType.ERROR, matcher.group(TokenType.ERROR.name())));
		        break;}
	    }
	    return tokens;
    }
}







