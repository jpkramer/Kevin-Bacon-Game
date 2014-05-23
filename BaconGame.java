import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import net.datastructures.Edge;
import net.datastructures.Vertex;

/**
 * BaconGame
 * 
 * Implements graphs in order to play the Kevin Bacon game
 * Requires encoded text files actors.txt, movies.txt, and movie-actors.txt
 * 
 * @author JonathanKramer
 *
 */



public class BaconGame {
	
	
	/**
	 * Creates a NamedAdjacencyMapGraph to represent all the file data
	 * 
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 */
	public static NamedAdjacencyMapGraph<String,String> BaconGraph() throws IOException, FileNotFoundException, IllegalArgumentException {
		
		BufferedReader actorsFile =  new BufferedReader(new FileReader("actors.txt"));
		BufferedReader moviesFile =  new BufferedReader(new FileReader("movies.txt"));
		BufferedReader movieActorsFile =  new BufferedReader(new FileReader("movie-actors.txt"));
		
		NamedAdjacencyMapGraph<String,String> baconGraph = new NamedAdjacencyMapGraph<String,String>(false);
		HashMap<String, String> moviesMap = new HashMap<String, String>();
		HashMap<String, String> actorsMap = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> movieActorsMap = new HashMap<String, ArrayList<String>>();

		String newLine1 = moviesFile.readLine();
		ArrayList<String> movies = new ArrayList<String>();
		
		while (newLine1 != null) {newLine1.indexOf('|');
			String movieCode = newLine1.substring(0,newLine1.indexOf('|'));
			String movieName = newLine1.substring(newLine1.indexOf('|') +1, newLine1.length());
			
			moviesMap.put(movieCode, movieName);
			movies.add(movieName);
			newLine1 = moviesFile.readLine();
			
		}
		
		String newLine2 = actorsFile.readLine();
			
		while (newLine2 != null) {
			String actorCode = newLine2.substring(0,newLine2.indexOf('|'));
			String actorName = newLine2.substring(newLine2.indexOf('|') +1, newLine2.length());
				
			actorsMap.put(actorCode, actorName);
			baconGraph.insertVertex(actorName);
			newLine2 = actorsFile.readLine();	
		
		}
		
		String newLine3 = movieActorsFile.readLine();
		
		while (newLine3 != null) {	
			String mCode = newLine3.substring(0,newLine3.indexOf('|'));
			String aCode = newLine3.substring(newLine3.indexOf('|') +1, newLine3.length());
			
			String mName = moviesMap.get(mCode);
			String aName = actorsMap.get(aCode);
			
			if (movieActorsMap.containsKey(mName)) {
				movieActorsMap.get(mName).add(aName);
			}
			else {
				ArrayList<String> actors = new ArrayList<String>();
				movieActorsMap.put(mName, actors);
				movieActorsMap.get(mName).add(aName);
			}
			
			newLine3 = movieActorsFile.readLine();
			//System.out.println(movieActorsMap);
			
		}
		
		for(int i = 0; i < movies.size(); i++) {
		
			String currentMovie = movies.get(i);
			ArrayList<String> actors = movieActorsMap.get(currentMovie);
		
			//System.out.println(currentMovie);
			//System.out.println(actors);
		if(actors != null) {
			for (int j = 0; j < actors.size(); j++) {
				String currentActor = actors.get(j);
		
				for(int k = j+1; k < actors.size(); k++) {
					String nextActor = actors.get(k);
					try {
						baconGraph.insertEdge(currentActor, nextActor, currentMovie);
					} catch (IllegalArgumentException e) {}
				}
			}
		}
	}	
		
		actorsFile.close();
		moviesFile.close();
		movieActorsFile.close();
		
		//System.out.print(baconGraph);
		
		return baconGraph;
		
	}
	
	/**
	 * Uses the baconGraph to perform a Breadth-First Search
	 * Stores the search tree as a directed graph
	 * 
	 * @param baconGraph
	 * @return
	 */
	public static NamedAdjacencyMapGraph<String,String> BFS(NamedAdjacencyMapGraph<String,String> baconGraph) {
		
		NamedAdjacencyMapGraph<String,String> bfs = new NamedAdjacencyMapGraph<String,String>(true);
		
		/*
		 * These Vertex<String> objects in this will come from baconGraph (with all the actors and movies).
		 * Whenever I dequeue a vertex from this queue, I will add a NEW vertex (a corresponding one)
		 * to the BFS tree.
		 */
		Queue<Vertex<String>> queue = new LinkedList<Vertex<String>>();
		
		Vertex<String> s = baconGraph.getVertex("Kevin Bacon");
		bfs.insertVertex("Kevin Bacon");
		
		queue.add(s);  // initialize the queue to contain only the start vertex s
		
		while (!queue.isEmpty()) {
			Vertex<String> current = queue.poll(); 
			//System.out.print(current.getElement());
		  for (Edge<String> edge : baconGraph.outgoingEdges(current)) {
		  	//System.out.print(edge.getElement());
		  	Vertex<String> next = baconGraph.opposite(current, edge);
		  	//System.out.print(next.getElement());
		    if (!bfs.vertexInGraph(next.getElement())){
		      bfs.insertVertex(next.getElement());
		      bfs.insertEdge(next.getElement(), current.getElement(), edge.getElement());
		      //System.out.print(next.getElement());
		      queue.add(next); 
		    }
		  }
		}
		
		return bfs;
		
	}

	/**
	 * Finds the path from a given actor to Kevin Bacon using the BFS graph
	 * Outputs the actors Kevin Bacon number and the path taken to get to Kevin Bacon
	 * 
	 * @param bfs
	 * @param name
	 */
	public static void find(NamedAdjacencyMapGraph<String,String> bfs, String name) {
		
		ArrayList<String> traverse = new ArrayList<String>();
		
		Vertex<String> current = bfs.getVertex(name);
		
		while (bfs.outgoingEdges(current).iterator().hasNext()) {
			Edge<String> edge = bfs.outgoingEdges(current).iterator().next();
		  Vertex<String> next = bfs.opposite(current, edge);
		 	traverse.add(current.getElement() + " appeared in " + edge.getElement() +  " with " + next.getElement());
		  current = next;
		}  
		
		int baconNumber = traverse.size();
		
		System.out.println(name + "'s Kevin Bacon number is " + baconNumber + ".");
		
		for(int i = 0; i<traverse.size(); i++) {
			System.out.println(traverse.get(i));
		}
		
	}
	
	/**
	 * Main function that scans for user input and runs the game 
	 * Checks to make sure actor is in database and has a non-infinite Kevin Bacon number.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	 public static void main (String [] args) throws FileNotFoundException, IllegalArgumentException, IOException  {
		 
		Scanner scanner = new Scanner(System.in);
		NamedAdjacencyMapGraph<String,String> baconGraph = BaconGraph();
		NamedAdjacencyMapGraph<String,String> bfs = BFS(baconGraph);
		
		while(true) {
			
		 // Scan for actor name
			System.out.print("Enter the name of an actor: ");
			String name = scanner.nextLine();
		 
		 // if actor name is not in the Bacon Graph...it is not in database 
			if (!baconGraph.vertexInGraph(name))
				System.out.println(name + " is not in our database.");
				
		 // else if actor name is not in the BFS...it has an infinite bacon number 
			else if (!bfs.vertexInGraph(name))
				System.out.println(name + "'s Kevin Bacon number is infinite.");
			
		 // else use find() method 
			else
				find(bfs,name);
				System.out.println("");
			
		}
		
	 }
	 
}
