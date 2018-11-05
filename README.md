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
