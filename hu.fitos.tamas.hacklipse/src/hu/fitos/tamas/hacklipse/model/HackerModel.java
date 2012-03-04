package hu.fitos.tamas.hacklipse.model;

import java.util.ArrayList;
import java.util.List;

public class HackerModel {
	
	private List<HackerElem> hackerElems = new ArrayList<HackerElem>();
	
	
	
	public void setHackerElems(List<HackerElem> elems){
		
		if(elems == null){
			// hackerElems cannot be null
			elems = new ArrayList<HackerElem>();
		}
		this.hackerElems = elems;
	}
	
	public void addHackerElem(HackerElem elem){
		hackerElems.add(elem);
	}
	
	/**
	 * @return never null, empty at least
	 */
	public List<HackerElem> getHackerElems() {
		return hackerElems;
	}
}
