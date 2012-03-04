package hu.fitos.tamas.hacklipse.views;

import hu.fitos.tamas.hacklipse.model.HackerElem;

public interface IHackViewListener {
	
	void doubleClicked(HackerElem elem);
	
	void refreshPushed();
	
}
