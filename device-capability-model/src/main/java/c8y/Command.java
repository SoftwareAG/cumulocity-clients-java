package c8y;

import java.io.Serializable;

public class Command implements Serializable {

    private static final long serialVersionUID = -6443811928706492241L;
    
	private String text;
	
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

	@Override
	public String toString() {
		return String.format("Command [text=%s]", text);
	}

}
