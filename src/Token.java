//Noah D Garner
//Eclipse IDE
//04/5/2018
//PHASE 2
public class Token {
	public TokenType type;
    public String data;
    public Token(TokenType type, String data) {
      this.type = type;
      this.data = data;
    }
    
    @Override
    public String toString() {
      return String.format("%s %s", type.name(),data);
    }
}
