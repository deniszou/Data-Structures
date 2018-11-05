package apps;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		Queue <Person> queue = new Queue<Person>();
		boolean[]visited = new boolean[g.members.length];
		visited[g.map.get(p1)] = true;
		ArrayList<String>first = new ArrayList<String>();
		queue.enqueue(g.members[g.map.get(p1)]);
		Queue<ArrayList<String>>chains = new Queue<ArrayList<String>>();
		first.add(g.members[g.map.get(p1)].name);
		chains.enqueue(first);
		do {
			Person current = queue.dequeue();
			visited[g.map.get(current.name)] = true;
			ArrayList<String> sift = chains.dequeue();
			Friend next = g.members[g.map.get(current.name)].first;
			while(next != null) {
				if(!visited[next.fnum]) {
					ArrayList<String> finale = new ArrayList<String>(sift);
					finale.add(g.members[next.fnum].name);
					if(g.members[next.fnum].name.equals(p2)) {
						return finale;
					}
					queue.enqueue(g.members[next.fnum]);
					chains.enqueue(finale);
				}
				next=next.next;
			}
		}while(queue.size()>0);
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		boolean[] visited = new boolean[g.members.length];
		ArrayList<ArrayList<String>> bigCliques = new ArrayList<>();
		
		for(int i = 0; i < g.members.length; i++) {
			Person p = g.members[i];
			if(visited[i] || !p.student)
				continue;
			
			ArrayList<String> nClique = new ArrayList<>();
			cliqueHelper(g, school, nClique, i, visited);
			if(nClique.size() > 0) {
				if(nClique != null) {
					bigCliques.add(nClique);
				}
			}	
		}
		
		return bigCliques;
		
	}
	private static void cliqueHelper(Graph g, String school, ArrayList<String> nClique, 
			 int i, boolean[] visited) {
		Person person = g.members[i];
		if(helperHelper(visited[i],person.student,person.school.equals(school))) {
					nClique.add(person.name);
		}
		
		visited[g.map.get(person.name)] = true;

		Friend pos = g.members[i].first;
		while(pos != null) {
			int number = pos.fnum;
			Person friendPerson = g.members[number];
			if(helperHelper(visited[number],friendPerson.student,school.equals(friendPerson.school))){
						cliqueHelper(g, school, nClique, number, visited);
			}
			pos = pos.next;
		}
		
	}
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	private static boolean helperHelper(boolean one, boolean two, boolean three) {
		if(!one && two && three) {
			return true;
		}else return false;
	}
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		boolean[] visited = new boolean[g.members.length];
		HashMap<String, Integer> back = new HashMap<>();
		ArrayList<String> used = new ArrayList<>();
		ArrayList<String> allConnectors = new ArrayList<>();
		HashMap<String, Integer> dfsNums = new HashMap<>();
		
		for(int i = 0; i < g.members.length; i++) {
			if(visited[i])
				continue;
			
			connectorHelper(g, new int[] {0,0}, allConnectors, dfsNums, i, visited, back, used);
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return allConnectors;
		
		
	}
	private static void connectorHelper(Graph g, int[] numbers, ArrayList<String> connectors, HashMap<String, Integer> dfsNums, 
			int i, boolean[] visited, HashMap<String, Integer> back, ArrayList<String> used) {

		boolean swich;
		Person person = g.members[i];		
		visited[g.map.get(person.name)] = true;
		dfsNums.put(person.name, numbers[0]);
		back.put(person.name, numbers[1]);

		Friend curr = g.members[i].first;
		while(curr != null) {
			int fNum = curr.fnum;
			Person friendMem = g.members[fNum];
			
			
			if(!visited[fNum]) {
				numbers[0]++;
				numbers[1]++;
				swich=false;
				connectorHelper(g, numbers, connectors, dfsNums, fNum, visited,back, used);
				
				if(dfsNums.get(person.name) > back.get(friendMem.name)) {
					int mBack = Math.min(back.get(person.name), 
							back.get(friendMem.name));
					
					back.put(person.name, mBack);
				}
				
				if(helperHelper(connectors.contains(person.name), (used.contains(person.name) || !swich),
						(back.get(friendMem.name) >= dfsNums.get(person.name)))) {
							connectors.add(person.name);
				}
				
				used.add(person.name);
				
			} else {
				int mBack = Math.min(back.get(person.name), 
						dfsNums.get(friendMem.name));
				
				back.put(person.name, mBack);
			}
			
			curr = curr.next;
		}
		
	}
}

