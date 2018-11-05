package tse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class ToySearchEngine {
	
	/**
	 * This is a hash table of all keys. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keysIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keysIndex and noiseWords hash tables.
	 */
	public ToySearchEngine() {
		keysIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of key occurrences
	 * in the document. Uses the getKey method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keys in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeysFromDocument(String docFile) 
	throws FileNotFoundException {
		HashMap<String, Occurrence> allKeys = new HashMap<>();
		Scanner sc = new Scanner(new File(docFile));
		sc.reset();
		while(sc.hasNext()) {
			String preKey = sc.next();
			String key = getKey(preKey);
			if (key==null || key.length()==0) {
				continue;
			}
			Occurrence freq = allKeys.get(key);
			if (freq!=null) {
				freq.frequency++;
				allKeys.put(key, freq);
			}else {
				allKeys.put(key, new Occurrence(docFile, 1));
			}
		}
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		sc.close();
		return allKeys;
	}
	
	/**
	 * Merges the keys for a single document into the master keysIndex
	 * hash table. For each key, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same key's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeys(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for (String currentKey: kws.keySet()) {
			ArrayList<Occurrence> freqList= new ArrayList<Occurrence>();
			if(keysIndex.containsKey(currentKey)) {
				freqList = keysIndex.get(currentKey);
				freqList.add(kws.get(currentKey));
				insertLastOccurrence(freqList);
				keysIndex.put(currentKey, freqList);
			}else {
				freqList.add(kws.get(currentKey));
				keysIndex.put(currentKey,freqList);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * Note: No other punctuation characters will appear in grading testcases
	 * 
	 * @param word Candidate word
	 * @return Key (word without trailing punctuation, LOWER CASE)
	 */
	public String getKey(String word) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		word = word.toLowerCase();
		for (int i = word.length()-1;i >= 0;i--) {
			char c = word.charAt(i);
			if (c >= 97 && c <= 122) {
				word=word.substring(0,i+1);
				if (noiseWords.contains(word)) {
					return null;
				}
				break;
			}
		}
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (c <= 64||(c >= 91 && c <= 96)||c >= 123) {
				return null;
			}
		}
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if (occs.size() <= 1) {
			return null;
		}
		ArrayList<Integer>graded = new ArrayList<Integer>();
		Occurrence last = occs.remove(occs.size()-1);
		int left = 0;
		int right = occs.size()-1;
		int mid = 0;
		while (left <= right) {
			mid=(left+right)/2;
			graded.add(mid);
			if (occs.get(mid).frequency==last.frequency) {
				break;
			}
			if (last.frequency > occs.get(mid).frequency) {
				right = mid-1;
			}else {
				left= mid+1;
			}
		}
		if (occs.get(mid).frequency>=last.frequency) {
			occs.add(mid+1,last);
		}else
			occs.add(mid, last);
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return graded;
	}
	
	/**
	 * This method indexes all words found in all the input documents. When this
	 * method is done, the keysIndex hash table will be filled with all keys,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void buildIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all words
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeysFromDocument(docFile);
			mergeKeys(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> top5 = new ArrayList<String>();
		ArrayList<Occurrence> aOc = keysIndex.get(kw1);
		ArrayList<Occurrence> bOc = keysIndex.get(kw2);
		if(aOc == null && bOc == null)
			return null;
		int k=0;
		if(aOc!=null && bOc!=null) {
			while (top5.size()<5 && aOc.size()>0 && bOc.size()>0) {
				if (aOc.get(0).frequency > bOc.get(0).frequency) {
					if (!top5.contains(aOc.get(0).document)) {
						top5.add(aOc.get(0).document);
					}
					k=0;
				} 
				if (aOc.get(0).frequency < bOc.get(0).frequency) {
					if (!top5.contains(bOc.get(0).document)) {
						top5.add(bOc.get(0).document);
					}
					k=1;
				}else if(aOc.get(0).frequency == bOc.get(0).frequency){
					if ((!top5.contains(bOc.get(0).document)&&(!top5.contains(aOc.get(0).document)))) {
						top5.add(aOc.get(0).document);
						top5.add(bOc.get(0).document);
					}
					k=2;
				}
				if(aOc.get(0).document==bOc.get(0).document || k==2) {
					aOc.remove(0);
					bOc.remove(0);
					continue;
				}
				if(k==0) {
					aOc.remove(0);
					continue;
				}
				if(k==1) {
					bOc.remove(0);
					continue;
				}
			}
		}
		if((aOc == null || aOc.size()==0) && bOc !=null) {
			while(top5.size()<5 && bOc.size()>0) {
				if (!top5.contains(bOc.get(0).document)) {
				top5.add(bOc.get(0).document);
				}
				bOc.remove(0);
			}
			return top5;
		}
		if((bOc == null || bOc.size()==0) && aOc !=null) {
			while(top5.size()<5 && aOc.size()>0) {
				if (!top5.contains(aOc.get(0).document)) {
				top5.add(aOc.get(0).document);
				}
				aOc.remove(0);
			}
			return top5;
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return top5;
	
	}
}
