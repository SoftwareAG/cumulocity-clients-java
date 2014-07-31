package c8y;

public class RelayArray {
	
	private Relay relays[];
	
	public Relay[] getRelays(){
		return relays;
	}
	
	public void setRelays(Relay[] relays){
		this.relays=relays;
	}
	
	@Override
	public int hashCode() {
		return relays == null ? 0 : relays.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof RelayArray))
			return false;
		if (((RelayArray)obj).relays.length!=relays.length)
			return false;
		
		for(int i=0; i<relays.length;i++) {
			if(relays[i]==null) {
				if(((RelayArray)obj).relays[i]!=null)
					return false;
			}
			else if(relays[i].equals(((RelayArray)obj).relays[i]))
				return false;
		}
		return true;
	}
}
