import java.io.IOException;
import java.util.ArrayList;


/**
 * 
SAMPLE INPUT:

START A B C D E F G  GOAL
GOLD WAND RUBY 
5
START TREASURES TOLLS NEXT A C D
A TREASURES TOLLS NEXT START B D
B TREASURES GOLD TOLLS WAND NEXT A E
C TREASURES TOLLS GOLD NEXT START D GOAL
D TREASURES WAND TOLLS NEXT START A C E F
E TREASURES TOLLS NEXT B D F G
F TREASURES TOLLS RUBY NEXT D E G GOAL
G TREASURES RUBY TOLLS WAND NEXT E F
GOAL TREASURES TOLLS NEXT C F GOAL

*/


public class Main {
	static String propFile = "props.txt";
	static StringBuilder sbDP = new StringBuilder();
	static StringBuilder sbFE = new StringBuilder();
	static StringBuilder sbBE = new StringBuilder();
	static Integer steps;
	static ArrayList<String> look = new ArrayList<String>();
	
	public static void main(String args[]) throws IOException {
		FrontEnd.start();
		DavisPutnam.start();
		BackEnd.start();
		System.out.print(sbDP);
		System.out.print(sbFE);
		System.out.print(sbBE);
	}
}
