package hu.fitos.tamas.hacklipse.service;

import hu.fitos.tamas.hacklipse.model.HackerElem;

import java.io.IOException;
import java.util.List;

public interface IHRetriever {
	
	List<HackerElem> retrieve() throws IOException;
	
}
