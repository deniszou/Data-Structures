package tse;
import java.util.*;
public class ToySearchEngineApp {
	public static void main(String[] args) {
		ToySearchEngine tse = new ToySearchEngine();
		try {
			tse.buildIndex("docs.txt","noisewords.txt");	
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		Set<String> results = tse.keysIndex.keySet();
		
		System.out.println("\n\n");
		for(String s : results) {
			System.out.println(s + " " + tse.keysIndex.get(s).toString());
		}
		ArrayList<String> top5 = tse.top5search("united", "alas");
		//ArrayList<String> top5 = lse.top5search("test", "test");
		if(top5 != null)
			System.out.println("\n\nResults: " + top5.toString());
		else
			System.out.println("\n\nResults: null");
	}
}
