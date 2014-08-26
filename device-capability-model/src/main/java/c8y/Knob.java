package c8y;

/**
 * Provides a representation for a control knob.
 * 
 * A control knob is a rotary control used to provide input to a device 
 * when grasped by an operator and turned, so that the degree of rotation 
 * corresponds to the desired input. 
 */
public class Knob {
	private long knobState;
	
	public void setKnobValue(Long knobValue){
		this.knobState=knobValue;
	}
	
	public Long getKnobValue(){
		return knobState;
	}
	
}
