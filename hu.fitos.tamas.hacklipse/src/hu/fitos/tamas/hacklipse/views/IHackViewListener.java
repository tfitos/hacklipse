package hu.fitos.tamas.hacklipse.views;

import hu.fitos.tamas.hacklipse.model.HackerElem;

public interface IHackViewListener {
	
	void titleDoubleClicked(HackerElem elem);
	
	void commentsDoubleClicked(HackerElem elem);
	
	void userDoubleClicked(HackerElem elem);
	
	void refreshPushed();
	
}
