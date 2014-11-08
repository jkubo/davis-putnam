import java.util.ArrayList;
import java.util.StringTokenizer;

public class BackEnd extends Main {
	static ArrayList<Integer> indices = new ArrayList<Integer>();
	
	public static void start() {
		StringTokenizer st = new StringTokenizer(sbDP.toString().replaceAll("\\s", " "));
		sbBE.append('\n').append("PATH:").append('\n');
		while(true) {
			int I = Integer.parseInt(st.nextToken());
			if(I == 0) break;
			String C = st.nextToken();
			if(C.equals("T")) indices.add(I);
		}
		for(int i=0; i<steps; i++) {
			sbBE.append(look.get(indices.get(i)-1)).append('\n');
		}
	}
}