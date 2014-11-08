import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class FrontEnd extends Main{
	// Use nodes as a look up and revNodes as reverse map
	static ArrayList<Node> nodes = new ArrayList<Node>();
	static HashMap<Node, Integer> revNodes = new HashMap<Node, Integer>();
	static ArrayList<String> reqs = new ArrayList<String>();
	static HashMap<String, Integer> revReqs = new HashMap<String, Integer>();
	
	static int[][] AT, HAS, AVAIL;
	
	public static void start() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(propFile));
		StringTokenizer st;
		
		// Parse the nodes
		st = new StringTokenizer(br.readLine());
		while(st.hasMoreTokens())
			nodes.add(new Node(st.nextToken()));
		for(int i=0; i<nodes.size(); i++)
			revNodes.put(nodes.get(i), i);
		
		// Parse the requirements
		st = new StringTokenizer(br.readLine());
		while(st.hasMoreTokens())
			reqs.add(st.nextToken());
		for(int i=0; i<reqs.size(); i++)
			revReqs.put(reqs.get(i), i);
		
		// Parse the count
		steps = Integer.parseInt(br.readLine())+1;
		
		boolean flag = true;
		while(flag) {
			// Parse the information as nodes
			st = new StringTokenizer(br.readLine());
			String name = st.nextToken();
			if(name.equals("GOAL"))
				flag = false;
			
			// Add all treasures
			String pt1, pt2;
			while(!(pt1 = st.nextToken()).equals("TOLLS")) {
				if(pt1.equals("TREASURES")) continue;
				for(Node n : nodes) {
					if(n.name.equals(name)) n.treasures.add(pt1);
				}
			}
			// Add all tolls
			while(!(pt2 = st.nextToken()).equals("NEXT")) {
				for(Node n : nodes) {
					if(n.name.equals(name)) n.tolls.add(pt2);
				}
			}
			// Add all reachable nodes
			while(st.hasMoreTokens()) {
				for(Node n : nodes) {
					if(n.name.equals(name)) n.next.add(st.nextToken());
				}
			}
		}
		setPropIndices(revNodes.size(), revReqs.size());
		writeProps(bw);
		
		for(int i=0; i<look.size(); i++) {
			sbFE.append(look.get(i)).append(' ').append(i+1).append('\n');
		}
		bw.write("0");
		bw.close();
	}
	
	static void writeProps(BufferedWriter bw) throws IOException {
		for(int i=0; i<steps; i++) {
			// P1
			for(Node n1 : nodes) {
				for(Node n2 : nodes) {
					if(!n1.name.equals(n2.name)) bw.write(prop1(n1,n2,i));
				}
			}
			// P2
			for(String s : reqs) {
				bw.write(prop2(s,i));
			}
			for(Node n : nodes) {
				// P6
				for(String s : n.tolls) {
					bw.write(prop6(n, s, i));
				}
			}
		}
		for(int i=0; i<steps-1; i++) {
			// P3
			for(Node n1 : nodes) {
				ArrayList<Node> al = new ArrayList<Node>();
				for(Node n2 : nodes) {
					for(String s : n1.next) {
						if(n2.name.equals(s)) al.add(n2);
					}
				}
				bw.write(prop3(n1, al, i));
			}
			for(Node n : nodes) {
				// P4
				for(String s : n.tolls) {
					bw.write(prop4(n, s, i));
				}
				// P5
				for(String s : n.treasures) {
					bw.write(prop5(n, s, i));
				}
				// P7
				for(String s : reqs) {
					if(!n.treasures.contains(s)) bw.write(prop7(n, s, i));
				}
			}
			for(String s : reqs) {
				// P8
				bw.write(prop8(s, i));
				// P9
				bw.write(prop9(s, i));
			}
			for(Node n : nodes) {
				// P10
				for(String s : reqs) {
					if(!n.tolls.contains(s)) bw.write(prop10(n, s, i));
				}
			}
		}
		// P11
		bw.write(prop11());
		// P12
		for(String s : reqs) {
			bw.write(prop12(s));
		}
		// P13
		bw.write(prop13(steps));
	}
	
	static void setPropIndices(int s1, int s2) {
		AT = new int[s1][steps];
		HAS = new int[s2][steps];
		AVAIL = new int[s2][steps];
		
		int index = 1;
		for(int i=0; i<AT.length; i++) {
			for(int j=0; j<AT[0].length; j++) {
				AT[i][j] = index;
				look.add(nodes.get(i).name+" "+j);
				index++;
			}
		}
		for(int i=0; i<HAS.length; i++) {
			for(int j=0; j<HAS[0].length; j++) {
				HAS[i][j] = index;
				index++;
			}
		}
		for(int i=0; i<AVAIL.length; i++) {
			for(int j=0; j<AVAIL[0].length; j++) {
				AVAIL[i][j] = index;
				index++;
			}
		}
	}
	
	// Types of propositions
	static String prop1(Node n1, Node n2, int t) {
		return "-"+AT[revNodes.get(n1)][t]+" -"+AT[revNodes.get(n2)][t]+"\n";
	}
	static String prop2(String s, int t) {
		return "-"+HAS[revReqs.get(s)][t]+" -"+AVAIL[revReqs.get(s)][t]+"\n";
	}
	static String prop3(Node n, ArrayList<Node> al, int t) {
		String list = "";
		for(Node node : al)
			list += " "+AT[revNodes.get(node)][t+1];
		return "-"+AT[revNodes.get(n)][t]+list+"\n";
	}
	static String prop4(Node n, String s, int t) {
		return "-"+AT[revNodes.get(n)][t+1]+" "+HAS[revReqs.get(s)][t]+"\n";
	}
	static String prop5(Node n, String s, int t) {
		return "-"+AVAIL[revReqs.get(s)][t]+" -"+AT[revNodes.get(n)][t+1]+" "+HAS[revReqs.get(s)][t+1]+"\n";
	}
	static String prop6(Node n, String s, int t) {
		return "-"+AT[revNodes.get(n)][t]+" -"+HAS[revReqs.get(s)][t]+"\n";
	}
	static String prop7(Node n, String s, int t) {
		return "-"+AVAIL[revReqs.get(s)][t]+" -"+AT[revNodes.get(n)][t+1]+" "+AVAIL[revReqs.get(s)][t+1]+"\n";
	}
	static String prop8(String s, int t) {
		return AVAIL[revReqs.get(s)][t]+" -"+AVAIL[revReqs.get(s)][t+1]+"\n";
	}
	static String prop9(String s, int t) {
		return AVAIL[revReqs.get(s)][t]+" "+HAS[revReqs.get(s)][t]+" -"+HAS[revReqs.get(s)][t+1]+"\n";
	}
	static String prop10(Node n, String s, int t) {
		return "-"+HAS[revReqs.get(s)][t]+" -"+AT[revNodes.get(n)][t+1]+" "+HAS[revReqs.get(s)][t+1]+"\n";
	}
	static String prop11() {
		for(Node node : nodes)
			if(node.name.equals("START")) return AT[revNodes.get(node)][0]+"\n";
		return null;
	}
	static String prop12(String s) {
		return AVAIL[revReqs.get(s)][0]+"\n";
	}
	static String prop13(int t) {
		for(Node node : nodes)
			if(node.name.equals("GOAL")) return AT[revNodes.get(node)][t-1]+"\n";
		return null;
	}
}

class Node {
	String name;
	ArrayList<String> treasures = new ArrayList<String>();
	ArrayList<String> tolls = new ArrayList<String>();
	ArrayList<String> next = new ArrayList<String>();
	
	public Node(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "\n"+name+": "+treasures+" "+tolls+" "+next;
	}
}