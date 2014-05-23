import net.datastructures.*;
import java.util.Map;
import java.util.HashMap;

/**
 * NamedAdjacencyMapGraph.java
 * Subclass of AdjacencyMapGraph that allows accessing vertices according
 * to their names.  Names are actually anything of the generic type V.
 * 
 * @author Tom Cormen
 *
 * @param <V> generic type for vertices
 * @param <E> generic type for edges
 */

public class NamedAdjacencyMapGraph<V,E> extends AdjacencyMapGraph<V,E> {
  // Maintain a correspondence from a vertex name to the Vertex object.
  Map<V,Vertex<V>> vertexMap = new HashMap<V,Vertex<V>>();
  
  public NamedAdjacencyMapGraph(boolean directed) {
    super(directed);
  }
  
  // Overrides the inherited insertVertex method to also insert into the vertexMap.
  public Vertex<V> insertVertex(V name) {
    Vertex<V> newVertex = super.insertVertex(name);
    vertexMap.put(name, newVertex);
    return newVertex;
  }
  
  // Return the Vertex object corresponding to a name, or null if
  // no corresponding object.
  public Vertex<V> getVertex(V name) {
    return vertexMap.get(name);
  }
  
  // Return true if a Vertex with the name is in the graph.
  public boolean vertexInGraph(V name) {
    return vertexMap.containsKey(name);
  }
  
  // Insert an edge based on the names of its vertices.
  public Edge<E> insertEdge(V uName, V vName, E element)
      throws IllegalArgumentException {
    return super.insertEdge(getVertex(uName), getVertex(vName), element);
  }
  
  // Return the edge from uName to vName, or null if they are not adjacent.
  public Edge<E> getEdge(V uName, V vName) throws IllegalArgumentException {
    return super.getEdge(getVertex(uName), getVertex(vName));
  }
}