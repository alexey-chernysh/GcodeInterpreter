package Token;


public class TokenAlfa extends Token {
	
	private TokenEnum type_;
	
	public	TokenAlfa(String st, TokenEnum t, int s, int e){
		super(st, s, e); 
		this.type_ = t;
	}
	
	public	TokenEnum getType(){ return this.type_; }

}
