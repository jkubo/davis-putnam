import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.StringTokenizer;

public class DavisPutnam extends Main {
	static ArrayList<HashSet<Integer>> clauseAL = new ArrayList<HashSet<Integer>>();
	static ArrayList<HashSet<Integer>> tempAL = new ArrayList<HashSet<Integer>>();
	static HashSet<Integer> propSet = new HashSet<Integer>();
	static HashSet<Integer> tempSet = new HashSet<Integer>();
	static String[] name;
	static Character[] status;
	
	public static void start() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(propFile));
		StringTokenizer st;
		
		while(true) {
			// Parsing
			st = new StringTokenizer(br.readLine());
			int t = Integer.parseInt(st.nextToken());
			// Stop when the first number if 0
			if(t == 0) break;
			
			// Storing data
			HashSet<Integer> clause = new HashSet<Integer>();
			clause.add(t);
			propSet.add(Math.abs(t));
			while(st.hasMoreTokens()) {
				int num = Integer.parseInt(st.nextToken());
				clause.add(num);
				propSet.add(Math.abs(num));
			}
			clauseAL.add(clause);
		}
		name = new String[propSet.size()];
		
		reset();
		// REP is the maximum number of rounds
		int REP = 15000;
		while(REP-- != 0) {
			if(dp(tempAL)) break;
			else reset();
		}
		
		// Writing the output into the string builder
		for(int i=0; i<status.length; i++) {
			if(!status[i].equals('T') && !status[i].equals('F')) {
				// Unsatisfiable
				sbDP = new StringBuilder();
				break;
			}
			sbDP.append(i+1).append(' ').append(status[i]).append('\n');
		}
		sbDP.append(0).append('\n');
		br.close();
	}
	
	/**
	 * Copies the main data into the temp structures to reuse the original data
	 */
	static void reset() {
		// Clear tempAL and tempSet
		tempAL.clear();
		tempSet.clear();
		
		// Restore clauseAL -> tempAL and clauseSet -> tempSet
		for(HashSet<Integer> hs : clauseAL) {
			HashSet<Integer> temp = new HashSet<Integer>();
			temp.addAll(hs);
			tempAL.add(temp);
		}
		tempSet.addAll(propSet);
		
		// Clear statuses
		status = new Character[propSet.size()];
		Arrays.fill(status, 'P');
	}
	
	/**
	 * Checks if there is literal statement
	 * Returns -1 if there isn't, -2 if unsatisfiable, the index if found a size 1 without conflict
	 * @param al
	 * @return
	 */
	static int hasLiteral(ArrayList<HashSet<Integer>> al) {
		ArrayList<Integer> props = new ArrayList<Integer>();
		int temp = -1;
		for(int i=0; i<al.size(); i++) {
			HashSet<Integer> clause = al.get(i);
			// Literal found
			if(clause.size() == 1) {
				int t = (int) clause.toArray()[0];
				// If there is a literal of the opposite sign, then catch
				if(props.contains(-t)) return -2;
				// Keep going if no error
				props.add(t);
				// Keep the last index of the literal
				temp = i;
			}
		}
		// Return -1 if not found and temp if found
		return props.size() == 0 ? -1 : temp;
	}
	
	/**
	 * Remove the element and set the status used into the character array
	 * @param rm
	 * @return
	 */
	static ArrayList<HashSet<Integer>> removeStuff(int rm) {
		// set array status
		status[Math.abs(rm)-1] = rm > 0 ? 'T' : 'F';
		
		// Remove element or clause depending on sign
		for(HashSet<Integer> clause : tempAL) {
			if(clause.contains(-rm)) clause.remove(-rm);
			if(clause.contains(rm)) clause.clear();
		}
		tempSet.remove(Math.abs(rm));
		return tempAL;
	}
	
	/**
	 * Main davis-putnam algorithm solved recursively
	 * @param al
	 * @return
	 */
	static boolean dp(ArrayList<HashSet<Integer>> al) {
		int index = 0;
		// Edge and base cases
		while((index = hasLiteral(al)) != -1) {
			// Not satisfiable
			if(index == -2) {
				reset();
				return false;
			}
			// Remove the first element of the index
			removeStuff((int) al.get(index).toArray()[0]);
		}
		
		// Satisfied
		if(tempSet.isEmpty()) return true;
		
		// Recurse on removed tempAL and tempSet random algorithm
		int rand = new Random().nextInt(tempSet.size());
		int t = (int) tempSet.toArray()[rand];
		if(dp(removeStuff(t))) return true;
		if(dp(removeStuff(-t))) return true;
		return false;
	}
}