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
	
	private static final String BASEURL = "http://news.ycombinator.com";
	
	public List<HackerElem> retrieve() throws IOException{
		
		List<HackerElem> hackerElems = new ArrayList<HackerElem>();
		Document doc = Jsoup.connect(BASEURL).get();
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
					
					Elements userAndcommentAs = element.parent().nextElementSibling().select(".subtext > a");
					if(userAndcommentAs != null && userAndcommentAs.size() == 2){
						Element userA = userAndcommentAs.get(0);
						hackerElem.setUsername(userA.text());
						hackerElem.setUserUrl(BASEURL + "/" + userA.attr("href"));
						
						Element commentA = userAndcommentAs.get(1);
						hackerElem.setComment(commentA.text());
						hackerElem.setCommentUrl(BASEURL + "/" + commentA.attr("href"));
					}
					
					hackerElems.add(hackerElem);
					i++;
				}
			}
		}
		
		return hackerElems;
	}
}
