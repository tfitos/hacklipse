package hu.fitos.tamas.hacklipse;

import hu.fitos.tamas.hacklipse.model.HackerElem;
import hu.fitos.tamas.hacklipse.service.HackerNewsRetriever;

import java.io.IOException;
import java.util.List;

public class HacklipeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		
		HackerNewsRetriever retriever = new HackerNewsRetriever();
		List<HackerElem> elems = retriever.retrieve();
		for(HackerElem elem : elems){
			System.out.println("====");
			System.out.println(elem.getTitle());
			System.out.println(elem.getUsername() + " - " + elem.getUserUrl());
			System.out.println(elem.getComment() + " - " + elem.getCommentUrl());
			
		}
		
//		Document doc = Jsoup.connect("http://news.ycombinator.com").get();
//		Elements elements = doc.select(".title");
//		for(Element element : elements){
//			Elements as = element.select("a");
//			if(!as.isEmpty()){
//				Element a = as.first();
//				System.out.println("============");
//				System.out.println("Title=" + a.text());
//				System.out.println("Link=" + a.attr("href"));
//				System.out.println("Domain=" + element.select("span").text());
//				System.out.println("============");
//			}
//		}
	}

}
