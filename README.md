# Data-Structures
Projects completed for CS-112
# Mini Cipher System
  MCS Encryption/Decryption Algorithm
In this assignment, you will implement a mini cipher system that field agents could use to encrypt their messages securely. You will use 1 to 26 integers as keys, plus two flag numbers which are 27 and 28. You will store the keys and flag numbers in a CIRCULAR linked list. What follows is a description of the encryption/decryption algorithm.

The algorithm starts with a sequence in some random order. It uses this sequence to generate what is called a keystream, which is a sequence of numbers called keys. Each key will be a number between 1 and 26. Imagine that you want to encrypt the following message to send to your friend:

   DUDE, WHERE'S MY CAR?
Imagine also that you start with the following 26 keys, with the two flags given values of 27 (Flag "A") and 28 (Flag "B"):
  INITIAL SEQUENCE:   13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 27 7 14 5 4 28 11 16 6
Starting with this sequence, you will get the following keystream (you will see how), one key for every alphabetic character in the message to be encrypted. Here's the message again, with everything but the letters taken out of consideration, followed by a parallel list of integers which correspond to the positions of the message letters in the alphabet, and finally, the keystream, one key per character:
Message:     D   U    D   E   W   H    E    R   E    S    M    Y    C    A   R

Alphabet:    4   21   4   5   23  8    5    18  5    19   13   25   3    1   18
Position

Keystream:   7   16   5   8   8   15   26   9   14   23   12   15   25   3   1
Encryption is then done by simply adding each key of the keystream to the corresponding alphabetic position, and if this sum is greater than 26, subtracting 26 from the sum. Here's the resulting sequence of numbers:
            11   11   9   13  5   23   5    1   19   16   25   14   2    4   19
The numbers are converted back to letters, to get the following encrypted message.
Encrypted:   KKIMEWEASPYNBDS
Decryption follows a similar process.
When the decrypter gets the coded message, she generates the keystream in exactly the same way, using the same initial sequence as the encryption. Then, the keystream is subtracted from the alphabetic position values of the letters in the coded message. If a code value is equal or smaller than the corresponding decryption key, 26 is first added to it and then the key is subtracted:

Code:       11   11   9   13   5   23   5   1   19   16   25   14    2   4   19

Keystream:   7   16   5    8   8   15  26   9   14   23   12   15   25   3    1

Message:     4   21   4    5   23   8   5  18    5   19   13   25    3   1   18

             D   U    D    E   W    H   E   R    E   S    M    Y     C   A    R
Generating the keystream
Here is the algorithm to generate each key of the keystream, starting with the initial sequence.
Get Key

Execute the following four steps:
Step 1 (Flag A): Find Flag "A" (27) and move it ONE position down by swapping it with the number below (after) it.
This results in the following, after swapping 27 with 7 in the starting sequence:
  INITIAL SEQUENCE:      13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 27 7 14 5 4 28 11 16 6
                                                                             ^^^^
  SEQUENCE AFTER STEP 1: 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7 27 14 5 4 28 11 16 6
                                                                             ^^^^
If the flag happens to be the last one in the sequence, then loop around and swap it with the first. For example:
  5 ... 27
Here 5 is the first number and 27 is the last number. Swapping them will give:
  27 ... 5
Step 2 (Flag B): Find Flag "B" (28) and move it TWO numbers down by swapping it with the numbers below (after) it.
This results in the following, after moving 28 two numbers down in the sequence that resulted after step 1:
  
  SEQUENCE AFTER STEP 1: 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7 27 14 5 4 28 11 16                                       
  SEQUENCE AFTER STEP 2: 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7 27 14 5 4 11 16 28 6
                                                                                       
If the flag happens to be the last (or second to last) number in the sequence, then loop around and swap it with the number(s) in the front. For example:
   5 6 ... 10 28
Here 28 is the last one. Moving it one position down gives:
   28 6 ... 10 5
and moving it one more position down gives:
   6 28 ... 10 5
Step 3 (Triple Shift): Swap all the numbers before the first (closest to the top/front) flag with the numbers after the second flag:
  
  SEQUENCE AFTER STEP 2: 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7|27 14 5 4 11 16 28|6                             
  SEQUENCE AFTER STEP 3: 6|27 14 5 4 11 16 28|13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7
                      
If there are no numbers before the first flag, then the second flag will become the last one in the modified sequence. Similarly, if there are no numbers after the second flag, then the first flag will become the first number in the modified sequence.
Step 4 (Count Shift): Look at the value of the last number in the sequence. Count down that many numbers from the first number, and move those numbers to just before the last number:
  
  SEQUENCE AFTER STEP 3: 6 27 14 5 4 11 16 28 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 7
  SEQUENCE AFTER STEP 4: 28 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 6 27 14 5 4 11 16 7
                                                                            
If the last number happens to be Flag B (28), use 27 (instead of 28) as its value for this step.
After these four steps are done, look at the value of the first number. If it is 28, then treat the value as 27. Count down by that many numbers from the first. Look at the value of the next number. If it happens NOT to be 27 or 28, this is the key. Otherwise, repeat the whole process (Flag A through Count Shift) with the latest (current) sequence (NOT the initial sequence).
  
  SEQUENCE AFTER STEP 4: 28 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 6 27 14 5 4 11 16 7
  SEQUENCE AFTER STEP 5: 28 13 10 19 25 8 12 20 18 26 1 9 22 15 3 17 24 2 21 23 6 27 14 5 4 11 16 7
  
In this example, the first number is 28, so treat it as 27. The 27th number in the sequence is 16, and the next number is 7, so 7 is the key.
Once a key is found, the algorithm is repeated to find the subsequent keys, starting every time with the current sequence (NOT the initial sequence).

# PrefixTree
Summary
You will write an application to build a tree structure called PrefixTree for a dictionary of English words, and use the PrefixTree to generate completeWordList(list of words starting with the given prefix) for string searches.

PrefixTree Structure
A PrefixTree is a general tree, in that each node can have any number of children, where the root contains the prefix of its leaves. The tree is organized and created based on Huffman coding.  It is used to store a dictionary (list) of words that can be searched on, in a manner that allows for efficient generation of completeWordList.

The word list is originally stored in an array, and the PrefixTree is built off of this array. 

# Toy Search Engine
Summary
You will implement a toy search engine to do two things: (a) gather and index keys that appear in a set of plain text documents, and (b) search for user-input keys against the index and return a list of matching documents in which these keys occur. (A word/keyword in the text document is called a key)
Following is the sequence of method calls that will be performed on a ToySearchEngine object, to index and search keys. 
ToySearchEngine() - Already implemented.
The constructor creates new (empty) keysIndex and noiseWords hash tables. The keysIndex hash table is the MASTER hash table, which indexes all keys from all input documents. The noiseWords hash table stores all the noise words. Both of these are fields in the ToySearchEngine class.

Every key in the keysIndex hash table is a keyword. The associated value for a key is an array list of (document,frequency) pairs for the documents in which the key occurs, arranged in descending order of frequencies. A (document,frequency) pair is held in an Occurrence object. The Occurrence class is defined in the ToySearchEngine.java file, at the top. In an Occurrence object, the document field is the name of the document, which is basically the file name, e.g. AliceCh1.txt.

void buildIndex(String docsFile, String noiseWordsFile) -
Indexes all the keys in all the input documents. See the method documentation and body in the ToySearchEngine.java file for details.

If you want to index the given sample documents, the first parameter would be the file docs.txt and the second parameter would be the noise words file, noisewords.txt

After this method finishes executing, the full index of all keys found in all input documents will be in the keysIndex hash table.

The buildIndex methods calls methods loadKeysFromDocument and mergeKeys, both of which you need to implement.

HashMap<String,Occurrence> loadKeysFromDocument(String docFile) - 
This method creates a hash table for all keys in a single given document. See the method documentation for details.

This method MUST call the getKey method, which you need to implement.

String getKey(String word) - 
Given an input word read from a document, it checks if the word is a key, and returns the key equivalent if it is.

FIRST, see the method documentation in the code for details, including a specific short list of punctuations to consider for filtering out. THEN, look at the following illustrative examples of input word, and returned value. 
 
Input Parameter - Returned value

distance. - distance (strip off period)

equi-distant - null (not all alphabetic characters)

Rabbit - rabbit(convert to lowercase)

Through - null (noise word)

we're - null (not all alphabetic characters)

World... - world (strip trailing periods)

World?! - world (strip trailing ? and !)

What,ever - null (not all alphabetic characters)

Observe that (as per the rules described in the method documentation), if there is more than one trailing punctuation (as in the "World..." and "World?!" examples above), the method strips all of them. Also, the last example makes it clear that punctuation appearing anywhere but at the end is not stripped, and the word is rejected.

Note that this is a much simplified filtering mechanism, and will reject certain words that might be accepted by a real-world engine. But the idea is to not unduly complicate this process, focusing instead on hash tables, which is the point of this assignment. So, just stick to the rules described here.

void mergeKeys - 
Merges the keys loaded from a single document (in method loadKeysFromDocument) into the global keysIndex hash table.

See the method documentation for details. This method MUST call the insertLastOccurence method, which you need to implement.

ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) - You implement.
See the method documentation for details. Note that this method uses binary search on frequency values to do the insertion. The return value is the sequence of mid points encountered during the search, using the regular (not lazy) binary search we covered in class. This return value is not used by the calling method-it is only going to be used for grading this method.

For example, suppose the list had the following frequency values (including the last one, which is to be inserted):

     --------------------
     12  8  7  5  3  2  6
     --------------------
      0  1  2  3  4  5  6
Then, the binary search (on the list excluding the last item) would encounter the following sequence of midpoint indexes:
    2  4  3
Note that if a subarray has an even number of items, then the midpoint is the last item in the first half.
After inserting 6, the input list would be updated to this:

     --------------------
     12  8  7  6  5  3  2
     --------------------
      0  1  2  3  4  5  6
and the sequence 2 4 3 would be returned.
If the new item is a duplicate of something that already exists, it doesn't matter if the new item is placed before or after the existing item.

Note that the items are in DESCENDING order, so the binary search would have to be done accordingly.

# Relationship Graph
Background
In this program, you will implement some useful algorithms for graphs that represent relationships (e.g. Facebook). A relationship graph is an undirected graph without any weights on the edges. It is a simple graph because there are no self loops (a self loop is an edge from a vertex to itself), or multiple edges (a multiple edge means more than edge between a pair of vertices).

The vertices in the graphs for this assignment represent two kinds of people: students and non-students. Each vertex will store the name of the person. If the person is a student, the name of the school will also be stored.

Here's a sample relationship graph:

     (sam,rutgers)---(jane,rutgers)-----(bob,rutgers)   (sergei,rutgers)
                          |                 |             |
                          |                 |             |
                     (kaitlin,rutgers)   (samir)----(aparna,rutgers)
                          |                            |
                          |                            |
    (ming,penn state)--(nick,penn state)----(ricardo,penn state)
                          |
                          |
                     (heather,penn state)


                   (michele,cornell)----(rachel)     
                          | 
                          | 
     (rich,ucla)---(tom,ucla)
Note that the graph may not be connected, as seen in this example in which there are two "islands" or cliques that are not connected to each other by any edge. Also see that all the vertices represent students with names of schools, except for rachel and samir, who are not students.
Algorithms

Shortest path: Intro chain
sam wants an intro to aparna through friends and friends of friends. There are two possible chains of intros:

  sam--jane--kaitlin--nick--ricardo--aparna

            or

  sam--jane--bob--samir--aparna
The second chain is preferable since it is shorter.
If sam wants to be introduced to michele through a chain of friends, he is out of luck since there is no chain that leads from sam to michele in the graph.

Note that this algorithm does NOT have any restriction on the composition of the vertices: a vertex along the shortest chain need NOT be a student at a particular school, or even a student. In other words, this algorithm is not about students, let alone students at a particular school. So, for instance, you may need to find the shortest path (intro chain) from nick to samir, which will be:

   nick--ricardo--aparana--samir
which consists of two penn state students, one rutgers student, and one non-student.

Cliques: Student cliques at a school
Students tend to form cliques with their friends, which creates islands that do not connect with each other. If these cliques could be identified, particularly in the student population at a particular school, introductions could be made between people in different cliques to build larger networks of relationships at that school.

In the sample graph, there are two cliques of students at rutgers:

     (sam,rutgers)---(jane,rutgers)-----(bob,rutgers)    (sergei,rutgers)
                          |                                |
                          |                                |
                     (kaitlin,rutgers)             (aparna,rutgers)
Note that in the graph these are not islands since samir connects them. However, since samir is not a student at rutgers, it results in two cliques of rutgers students that don't know each other through another rutgers student.

At penn state, there is a single clique of students:

    (ming,penn state)----(nick,penn state)----(ricardo,penn state)
                          |
                          |
                     (heather,penn state)
Also, a single clique of students at ucla:

     (rich,ucla)---(tom,ucla)
And a single clique of students at cornell:

             (michele,cornell)


