package hu.fitos.tamas.hacklipse.events;

import hu.fitos.tamas.hacklipse.mvc.HEvent;
import hu.fitos.tamas.hacklipse.mvc.HEventType;

public class HModelChangedEvent implements HEvent {

	private String reason;
	private HModelChangedEventType type;
	
	public HModelChangedEvent(HModelChangedEventType type) {
		this.type = type;
	}
	
	public enum HModelChangedEventType implements HEventType{
		RETRIEVED, RETRIEVE_FAILED
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return reason;
	}
	
	@Override
	public HEventType getType() {
		return type;
	}

}
