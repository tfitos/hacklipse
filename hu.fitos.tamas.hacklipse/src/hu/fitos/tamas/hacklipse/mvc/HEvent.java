package hu.fitos.tamas.hacklipse.mvc;


public interface HEvent {
	
	/**
	 * Returns the type of the event. This should be a constant and unique value for each HEvent implementation.
	 * 
	 * @return
	 */
	public HEventType getType();

}
