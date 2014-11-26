package c8y;

import java.io.Serializable;

public class Command implements Serializable {

    private static final long serialVersionUID = -6443811928706492241L;
    
	private String text;
	private String syntax;
	private String result;
	
	public Command() {}
	
	public Command(String text) {
	    this.text = text;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getSyntax() {
		return syntax;
	}
	
	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return String.format("Command [text=%s]", text);
	}

}
