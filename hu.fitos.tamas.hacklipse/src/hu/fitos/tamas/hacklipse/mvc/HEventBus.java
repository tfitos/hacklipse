package hu.fitos.tamas.hacklipse.mvc;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HEventBus {
	
	private Map<HEventType, List<HEventListener>> eventListenerMap = new HashMap<HEventType, List<HEventListener>>();
   
    /**
     * Adds an event listener for the given event. (synchronized)
     * 
     * @param eventType
     * @param listener
     */
	public void addEventListener(HEventType eventType,  HEventListener listener) {
        synchronized (eventListenerMap) {
            List<HEventListener> listeners = null;
            if (!eventListenerMap.containsKey(eventType)) {
                listeners = new ArrayList<HEventListener>();
                eventListenerMap.put(eventType, listeners); 
            } else {
                listeners = eventListenerMap.get(eventType);
            }

            listeners.add(listener);
        }
    }

    /**
     * Remove event listener without synchronization.
     * 
     * @param eventType
     * @param listener
     */
    private void _removeEventListener(HEventType eventType, HEventListener listener) {
        if (eventListenerMap.containsKey(eventType)) {
            List<HEventListener> listeners = eventListenerMap.get(eventType);
            listeners.remove(listener);
        }
    }

    /**
     * Remove event listener for the given event. (synchronized)
     * 
     * @param eventType
     * @param listener
     */
    public synchronized void removeEventListener(HEventType eventType, HEventListener listener) {
        synchronized (eventListenerMap) {
            _removeEventListener(eventType, listener);
        }
    }

    /**
     * Remove event listener from every event listener list. (synchronized)
     * 
     * @param listener
     *            model event listener
     **/
    public void removeEventListenerFromAll(HEventListener listener) {
        synchronized (eventListenerMap) {
            for (HEventType eventType : eventListenerMap.keySet()) {
                _removeEventListener(eventType, listener);
            }
        }
    }

    /**
     * Invoked when an event occurs. (synchronized)
     * 
     * @param event
     *            model event
     */
    public void fireEvent(HEvent event) {
        synchronized (eventListenerMap) {
            if (eventListenerMap.get(event.getType()) != null) {
            	List<HEventListener> listenerList = eventListenerMap.get(event.getType());
                for(HEventListener listener : listenerList){
                    listener.actionPerformed(event);
                }
            }
        }
    }
    
    public void removeAllListeners() {
        synchronized (eventListenerMap) {
            eventListenerMap.clear();
        }
    } 
}
