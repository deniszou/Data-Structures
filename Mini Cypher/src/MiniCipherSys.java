package mcs;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a mini cipher system.
 * 
 * @author RU NB CS112
 */
public class MiniCipherSys {
	
	/**
	 * Circular linked list that is the sequence of numbers for encryption
	 */
	SeqNode seqRear;
	
	/**
	 * Makes a randomized sequence of numbers for encryption. The sequence is 
	 * stored in a circular linked list, whose last node is pointed to by the field seqRear
	 */
	public void makeSeq() {
		// start with an array of 1..28 for easy randomizing
		int[] seqValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < seqValues.length; i++) {
			seqValues[i] = i+1;
		}
		
		// randomize the numbers
		Random randgen = new Random();
 	        for (int i = 0; i < seqValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = seqValues[i];
	            seqValues[i] = seqValues[other];
	            seqValues[other] = temp;
	        }
	     
	    // create a circular linked list from this sequence and make seqRear point to its last node
	    SeqNode sn = new SeqNode();
	    sn.seqValue = seqValues[0];
	    sn.next = sn;
	    seqRear = sn;
	    for (int i=1; i < seqValues.length; i++) {
	    	sn = new SeqNode();
	    	sn.seqValue = seqValues[i];
	    	sn.next = seqRear.next;
	    	seqRear.next = sn;
	    	seqRear = sn;
	    }
	}
	
	/**
	 * Makes a circular linked list out of values read from scanner.
	 */
	public void makeSeq(Scanner scanner) 
	throws IOException {
		SeqNode sn = null;
		if (scanner.hasNextInt()) {
			sn = new SeqNode();
		    sn.seqValue = scanner.nextInt();
		    sn.next = sn;
		    seqRear = sn;
		}
		while (scanner.hasNextInt()) {
			sn = new SeqNode();
	    	sn.seqValue = scanner.nextInt();
	    	sn.next = seqRear.next;
	    	seqRear.next = sn;
	    	seqRear = sn;
		}
	}
	private void swap(SeqNode pre) {
		SeqNode s1 = new SeqNode();
		s1=pre.next;
		int fir=s1.seqValue;
		SeqNode s2 = new SeqNode();
		s2=s1.next;
		int sec=s2.seqValue;
		SeqNode next = new SeqNode();
		next=s2.next;
		pre.next=s2;
		s2.next=s1;
		s1.next=next;
		if(seqRear.seqValue==sec) {
			seqRear=s2.next;
		}else if(fir==seqRear.seqValue) {
			do {
				seqRear=seqRear.next;
			}while(seqRear.next.seqValue!=fir);
		}
	}
	/**
	 * Implements Step 1 - Flag A - on the sequence.
	 */
	void flagA() {
	    // COMPLETE THIS METHOD
		SeqNode aFlag = new SeqNode();
		aFlag = seqRear;
		do {
			aFlag=aFlag.next;
		}while (aFlag.next.seqValue!=27);
		swap(aFlag);
	}
	
	/**
	 * Implements Step 2 - Flag B - on the sequence.
	 */
	void flagB() {
	    // COMPLETE THIS METHOD
		SeqNode bFlag = new SeqNode();
		bFlag=seqRear;
			do {
				bFlag=bFlag.next;
			}while (bFlag.next.seqValue!=28);
			swap(bFlag);
			swap(bFlag.next);
	}
	
	/**
	 * Implements Step 3 - Triple Shift - on the sequence.
	 */
	void tripleShift() {
	    // COMPLETE THIS METHOD
		SeqNode front = seqRear.next;
		SeqNode back = seqRear;
		SeqNode f2 = new SeqNode();
		SeqNode f1 = new SeqNode();
		SeqNode pf1 = new SeqNode();
		SeqNode af2 = new SeqNode();
		if(front.seqValue>=27 && back.seqValue<27){
			seqRear=seqRear.next;
			do {
				seqRear=seqRear.next;
			}while(seqRear.seqValue<27);
		}
		if(front.seqValue<27 && back.seqValue>=27){
			do {
				seqRear=seqRear.next;
			}while(seqRear.next.seqValue<27);
		}
		if(front.seqValue<27 && back.seqValue<27) {
			f1=front;
			pf1=back;
			do {
				f1=f1.next;
				pf1=pf1.next;
			}while(f1.seqValue<27);
			f2=f1;
			do {
				f2=f2.next;
			}while (f2.seqValue<27);
			af2=f2.next;
			f2.next=front;
			pf1.next=af2;
			back.next=f1;
			do {
				seqRear=seqRear.next;
			}while(seqRear!=pf1);
		}
	}
	
	/**
	 * Implements Step 4 - Count Shift - on the sequence.
	 */
	void countShift() {
	    // COMPLETE THIS METHOD
		SeqNode front = seqRear.next;
		SeqNode back = seqRear;
		SeqNode prBlock = seqRear;
		SeqNode endBlock = front;
		int rearVal=seqRear.seqValue;
		if (rearVal<27) {
		int i=1;
		while(i<rearVal) {
			endBlock=endBlock.next;
			i++;
		}
		SeqNode newFront=endBlock.next;
		do {
			prBlock=prBlock.next;
		}while(prBlock.next!=seqRear);
		prBlock.next=front;
		endBlock.next=back;
		seqRear.next=newFront;
		}
	}
	
	/**
	 * Gets a key. Calls the four steps - Flag A, Flag B, Triple Shift, Count Shift, then
	 * counts down based on the value of the first number and extracts the next number 
	 * as key. But if that value is 27 or 28, repeats the whole process (Flag A through Count Shift)
	 * on the latest (current) sequence, until a value less than or equal to 26 is found, 
	 * which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		int key=0;
		do {
			flagA();
			flagB();
			tripleShift();
			countShift();
			int frontVal=seqRear.next.seqValue;
			int i=0;
			SeqNode kPtr=seqRear.next;
			while(i<frontVal) {
				kPtr=kPtr.next;
				i++;
			}
			key=kPtr.seqValue;
		}while(key>=27);
		return key;
	    // COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	    //return -1;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(SeqNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.seqValue);
		SeqNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.seqValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		//getKey();
		//printList(seqRear);
		message=message.toUpperCase();
		char c;
		int cVal=0;
		String encrypt="";
		for(int i=0;i<message.length();i++) {
			c=message.charAt(i);
			if(c >= 'A' && c <= 'Z') {
				cVal=c+getKey();
				if (cVal>'Z') {
					cVal-=26;
				}
				c=(char)(cVal);
				encrypt=encrypt+c;
			}
		}
		return encrypt;
	    // COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	    //return null;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String decrypt="";
		int cVal=0;
		char c;
		for(int i=0;i<message.length();i++) {
			c=message.charAt(i);
			cVal=c-getKey();
			if (cVal<='A') {
				cVal+=26;
			}
			if (cVal>'Z') {
				cVal-=26;
			}
			c=(char)(cVal);
			decrypt+=c;
		}
		return decrypt;
	    // COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	    //return null;
	}
}
