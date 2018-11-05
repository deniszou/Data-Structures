package prefixTree;

import java.util.ArrayList;

/**
 * This class implements a PrefixTree.
 * 
 * @author Sesh Venugopal
 *
 */
public class PrefixTree {

	// prevent instantiation
	private PrefixTree() {
	}

	/**
	 * Builds a PrefixTree(Prefixtionary-tree) by inserting all words in the input array, one at a
	 * time, in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words
	 * in the input array are all lower case.
	 * 
	 * @param allWords
	 *            Input array of words (lowercase) to be inserted.
	 * @return Root of PrefixTree with all words inserted from the input array
	 */
	public static PrefixTreeNode buildPrefixTree(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		PrefixTreeNode root= new PrefixTreeNode(null,null,null);
		if (allWords.length==0) {
			return root;
		}
		Indices firstIndex=new Indices(0,(short)0,(short)(allWords[0].length()-1));
		root.firstChild=new PrefixTreeNode(firstIndex,null,null);
		PrefixTreeNode ptr=root.firstChild;
		PrefixTreeNode prev=root.firstChild;
		for(int i=1;i<allWords.length;i++) {
			int startX=0;
			int endX=0;
			int wordX=0;
			int prefix=0;
			while(ptr != null) {
				startX = ptr.substr.startIndex;
				endX = ptr.substr.endIndex;
				wordX = ptr.substr.wordIndex;
				if(startX < allWords[i].length()) {
					int pos = 0;
					while(pos < allWords[wordX].substring(startX, endX+1).length() && pos < allWords[i].substring(startX).length()
							&& allWords[wordX].substring(startX, endX+1).charAt(pos) == allWords[i].substring(startX).charAt(pos))
						{pos++;}
					prefix=pos-1;
				
					switch(prefix) {
					case -1:
						prev=ptr;
						ptr=ptr.sibling;
						break;
					}
					if(prefix!=-1) {
						prefix += startX;
						if (prefix < endX){
							prev = ptr;
							break;
						}
						if(prefix == endX) {
							prev=ptr;
							ptr=ptr.firstChild;
						}
					}
				}else {
					prev = ptr;
					ptr = ptr.sibling;
				}
			}
			if(ptr != null) {
				PrefixTreeNode tempChild = prev.firstChild; 
				Indices newCurrentIndices = new Indices(prev.substr.wordIndex, (short)(prefix+1), (short)prev.substr.endIndex);
				prev.substr.endIndex = prefix; 
				prev.firstChild = new PrefixTreeNode(newCurrentIndices, null, null);
				prev.firstChild.firstChild = tempChild;
				Indices indices = new Indices(i, (short)(prefix+1), (short)(allWords[i].length()-1));
				prev.firstChild.sibling = new PrefixTreeNode(indices, null, null);
			} else {
				prev.sibling = new PrefixTreeNode(new Indices(i, (short)startX, (short)(allWords[i].length()-1)), null, null);
			}
			ptr = root.firstChild;
			prev = root.firstChild;
		}
		return root;
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		//return null;
	}
	
		
	private static String getCommon(String check,String node) {
		String prefix="";
/*		String temp="";
		int save=0;*/
		for(int i=1;i<=check.length();i++) {
				if (check.substring(0,i).equals(node.substring(0,i))){
					prefix=check.substring(0,i);
			}
		}
		return prefix;
	}
	/*private static String getCommon(String check,String[] words) {
		String prefix="";
		String temp="";
		int save=0;
		for(int i=1;i<=check.length();i++) {
			for(int j=0;j<words.length;j++) {
				if (check.equals(words[j])) {
					continue;
				}
				if (check.substring(0,i).equals(words[j].substring(0,i))){
					temp=check.substring(0,i);
				}
			}
			if(!prefix.equals(temp)) {
				prefix=temp;
			}
		}
		return prefix;
	}*/

	/**
	 * Given a PrefixTree, returns the "completeWordList" for the given prefix, i.e. all the
	 * leaf nodes in the PrefixTree whose words start with this prefix. For instance,
	 * if the PrefixTree had the words "bear", "bull", "stock", and "bell", the
	 * completeWordList for prefix "b" would be the leaf nodes that hold "bear",
	 * "bull", and "bell"; for prefix "be", the completeWordList would be the leaf nodes
	 * that hold "bear" and "bell", and for prefix "bell", completeWordList would be the
	 * leaf node that holds "bell". (The last example shows that an input prefix can
	 * be an entire word.) The order of returned leaf nodes DOES NOT MATTER. So, for
	 * prefix "be", the returned list of leaf nodes can be either hold [bear,bell]
	 * or [bell,bear].
	 *
	 * @param root
	 *            Root of PrefixTree that stores all words to search on for completeWordList
	 * @param allWords
	 *            Array of words that have been inserted into the PrefixTree
	 * @param prefix
	 *            Prefix to be completed with words in PrefixTree
	 * @return List of all leaf nodes in PrefixTree that hold words that start with the
	 *         prefix, order of leaf nodes does not matter. If there is no word in
	 *         the tree that has this prefix, null is returned.
	 */
	public static ArrayList<PrefixTreeNode> completeWordList(PrefixTreeNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		if(root!=null) {
			ArrayList<PrefixTreeNode> leaves = new ArrayList<>();
			PrefixTreeNode ptr = root;	
			while(ptr != null) {
				//Get the substring at this node
				if(ptr.substr == null) //Possible that we're checking on root
					ptr = ptr.firstChild;
				
				String full = allWords[ptr.substr.wordIndex];
				if(prefix.startsWith(full.substring(0, ptr.substr.endIndex+1)) || full.startsWith(prefix)) {
					if(ptr.firstChild == null) { 
						leaves.add(ptr);
						ptr = ptr.sibling;
						
					} else {
						leaves.addAll(completeWordList(ptr.firstChild, allWords, prefix));
						ptr = ptr.sibling;
					}
				} else {
					ptr = ptr.sibling;
				}
			}
			return leaves;
		}else
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return null;
	}

	public static void print(PrefixTreeNode root, String[] allWords) {
		System.out.println("\nPrefixTree\n");
		print(root, 1, allWords);
	}

	private static void print(PrefixTreeNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (PrefixTreeNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}
