package hu.fitos.tamas.hacklipse.mvc;

public interface HEventListener<T extends HEvent> {
	
    void actionPerformed(T e);
    
}
