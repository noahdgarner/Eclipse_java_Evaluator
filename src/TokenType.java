//Noah D Garner
//Eclipse IDE
//04/5/2018
//PHASE 2
public enum TokenType {
    NUMBER("[0-9]+"), 
    BOOL("true|false"),
    PUNCTUATION(":=|==|=|[!|[-]|*|/|+|&|||)|(|;]"),
    KEYWORD("\\bif\\b|\\bthen\\b|\\belse\\b|\\bendif\\b|\\bwhile\\b|\\bdo\\b|\\bendwhile\\b|\\bskip\\b"),
    IDENTIFIER("([a-z]|[A-Z])([0-9]|[a-z]|[A-Z])*"),
    WHITESPACE("[\t\f\r\n]+"),
    ERROR ("[$|@|.|%|~|`|,|#|^|{|}|<|>|?|:|\"|_]+");
	public final String pattern;
	private TokenType(String pattern) {
		this.pattern = pattern;
    }
}
