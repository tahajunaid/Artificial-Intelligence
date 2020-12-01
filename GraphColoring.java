
import java.util.*;

class Graph {
	private int vCount;
	private List<Integer>[] adj;
	
	public int getvCount() {
		return vCount;
	}

	public Graph(int vCount) {
		this.vCount = vCount;
		adj = (List<Integer>[]) new List[vCount];
		for (int i = 0; i < vCount; i++)
			adj[i] = new ArrayList<Integer>();
	}

	public void addEdge(int i, int j) {
		adj[i].add(j);
		adj[j].add(i);
	}

	public void removeEdge(int i, int j) {
		Iterator<Integer> it = adj[i].iterator();
		while (it.hasNext()) {
			if (it.next() == j) {
				it.remove();
				break;
			}
		}
		Iterator<Integer> it2 = adj[j].iterator();
		while (it.hasNext()) {
			if (it.next() == i) {
				it.remove();
				return;
			}
		}
	}
	
	public boolean hasEdge(int i, int j) {
		return adj[i].contains(j);
	}

	public List<Integer> neighbours(int vertex) {
		return adj[vertex];
	}

	public void printGraph() {
		for (int i = 0; i < vCount; i++) {
			List<Integer> edges = neighbours(i);
			System.out.print(i + ": ");
			for (int j = 0; j < edges.size(); j++) {
				System.out.print(edges.get(j) + " ");
			}
			System.out.println();
		}
	}
}
public class GraphColoring {

	public static void main(String[] args) {
	
		Graph g = new Graph(5);

		System.out.println("Graph:");

		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 2);
		g.addEdge(1, 3);
		g.addEdge(2, 3);
		g.addEdge(3, 4);

		g.printGraph();

		backTrackingColoring(g, 3);
	}
	

	// Backtracking Coloring Utility Functions
	public static boolean isSafe(int v, Graph g, int colors[], int cr) {
		for (int i = 0; i < g.getvCount(); i++) {
			if (g.hasEdge(v, i) && cr == colors[i]) {
				return false;
			}
		}
		return true;

	}

	public static boolean graphColoringUtil(Graph g, int m, int colors[], int v) {
		// all vertices have a color then just true
		if (v == g.getvCount())
			return true;

		// try different colors for v
		for (int cr = 1; cr <= m; cr++) {
			// Check if assignment of color cr to v is fine
			if (isSafe(v, g, colors, cr)) {
				colors[v] = cr;
				// recur to assign colors to rest of the vertices
				if (graphColoringUtil(g, m, colors, v + 1))
					return true;

				// If assigning color cr doesn't lead
				// to a solution then remove
				colors[v] = 0;
			}
		}

		// if no color can be assigned then return false
		return false;
	}

	// Main Backtracking Coloring Function
	public static void backTrackingColoring(Graph g, int m) {
		int V = g.getvCount();

		// color array
		int colors[] = new int[V];

		// initialize all color values to 0
		Arrays.fill(colors, 0);

		// call graphColoringUtil for vertex 0
		if (!graphColoringUtil(g, m, colors, 0)) {
			System.out.println("Solution does not exist");
		}

		printColors(colors);
	}

	// Print Colors Function
	public static void printColors(int[] colors) {
		for (int i = 0; i < colors.length; i++)
			System.out.println("Vertex " + i + " --->  Color " + colors[i]);
	}
}