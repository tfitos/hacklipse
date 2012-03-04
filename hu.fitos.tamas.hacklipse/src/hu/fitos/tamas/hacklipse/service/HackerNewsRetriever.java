package hu.fitos.tamas.hacklipse.service;

import hu.fitos.tamas.hacklipse.model.HackerElem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HackerNewsRetriever implements IHRetriever{
	
	public List<HackerElem> retrieve() throws IOException{
		
		List<HackerElem> hackerElems = new ArrayList<HackerElem>();
		Document doc = Jsoup.connect("http://news.ycombinator.com").get();
		Elements elements = doc.select(".title");
		int i = 1;
		for(Element element : elements){
			Elements as = element.select("a");
			if(!as.isEmpty()){
				Element a = as.first();
				
				// last 'More' filtered out
				if(!"More".equals(a.text())){
					HackerElem hackerElem = new HackerElem();
					hackerElem.setNumber(i);
					hackerElem.setTitle(a.text());
					hackerElem.setUrl(a.attr("href"));
					hackerElem.setDomain(element.select("span").text());
					hackerElems.add(hackerElem);
					i++;
				}
			}
		}
		
		return hackerElems;
	}
}
