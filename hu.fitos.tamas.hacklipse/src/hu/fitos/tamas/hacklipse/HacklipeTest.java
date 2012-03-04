package hu.fitos.tamas.hacklipse;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HacklipeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		Document doc = Jsoup.connect("http://news.ycombinator.com").get();
		Elements elements = doc.select(".title");
		for(Element element : elements){
			Elements as = element.select("a");
			if(!as.isEmpty()){
				Element a = as.first();
				System.out.println("============");
				System.out.println("Title=" + a.text());
				System.out.println("Link=" + a.attr("href"));
				System.out.println("Domain=" + element.select("span").text());
				System.out.println("============");
			}
		}
	}

}
