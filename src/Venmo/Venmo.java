package Venmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.text.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Venmo {
  
  // This holds the latest timestamp transaction that is being processed, init to 0.
  private long maxTimeStamp = 0;
  
  // edgeMap is the graph of user relationships. The graph is implemented as a adjacency list of vertices.
  // Each vertex is an actor/target and the associated ArrayList is a list of all user relationships.
  //
  // For a payment between an actor and target, two entries are created in the edgeMap, one for actor
  // and one for target each having an edge to the other.
  //
  // If there are multiple payments between same actor/target, the timestamp in the GraphEdge
  // reflects the most recent payment timestamp.
  private HashMap<String, ArrayList<GraphEdge>> edgeMap = new HashMap<String, ArrayList<GraphEdge>>();
  
  // medianDegreeList stores the degree of each vertex in the user relationship graph.
  //
  // This data structure is ordered by degree and updated whenever the graph is modified with addition
  // or deletion of edges.
  private ArrayList<VertexDegree> medianDegreeList = new ArrayList<VertexDegree>();

  // createdPriorityQueue is a min-heap of the payments ordered by the created timestamp.
  //
  // For each payment received within 60 secs of most recent processed timestamp, 
  // an entry is added into the createdPriorityQueue.
  //
  // This helps to evicts edges from the edgeMap and medianDegreeList when a payment is received.
  private PriorityQueue<Transaction> createdPriorityQueue = new PriorityQueue<Transaction>();
  
  private final int timeThreshold = 60 * 1000;

  public void insertTransaction(Transaction t) {
    
    if (maxTimeStamp == 0) {
      maxTimeStamp = t.getCreatedTime();
    }
    
    if (maxTimeStamp - t.getCreatedTime() > timeThreshold) {
      /* If you get an out of order transaction, which is 60 or more seconds 
       * behind current transaction being processed, return.
       */
      return;
    }
    
    /* If it is a transaction to be processed, update MaxTimeStamp
     * to transaction time.
     */
    if (t.getCreatedTime() > maxTimeStamp) { 
      maxTimeStamp = t.getCreatedTime();
    }

    // Map storing all vertices that have new degrees.
    HashMap<String, Integer> modifiedVertexMap = new HashMap<String, Integer>();

    // Create edges for the transactions in the graph.
    addEdgeToGraph(t.getActor(), t.getTarget(), t.getCreatedTime());
    addEdgeToGraph(t.getTarget(), t.getActor(), t.getCreatedTime());

    // Add the two vertices in the transaction to the modified vertex map.
    modifiedVertexMap.put(t.getActor(), edgeMap.get(t.getActor()).size());
    modifiedVertexMap.put(t.getTarget(), edgeMap.get(t.getTarget()).size());  
        
    // Insert transaction into priorityQueue ordered by creationTime 
    createdPriorityQueue.offer(t);

    // Delete all edges older than 60 seconds.
    while((maxTimeStamp - createdPriorityQueue.peek().getCreatedTime()) > timeThreshold) {
      DeleteEdgeFromGraph(createdPriorityQueue.peek().getActor(), 
                	  createdPriorityQueue.peek().getTarget(),
                	  createdPriorityQueue.peek().getCreatedTime(), 
                	  modifiedVertexMap);
      DeleteEdgeFromGraph(createdPriorityQueue.peek().getTarget(), 
  	                  createdPriorityQueue.peek().getActor(),
                          createdPriorityQueue.peek().getCreatedTime(), 
                          modifiedVertexMap);
      
      createdPriorityQueue.poll();
    }
        
    // Update medianDegreeList for all vertices with new degrees.
    for (Map.Entry me : modifiedVertexMap.entrySet()) {
      VertexDegree modifiedVertex = new VertexDegree((int)me.getValue(), (String)me.getKey());
      updateMedianDegreeList(modifiedVertex);
    }
  }

  // Public method to get the current median.
  public float getMedian() {
    float median;
    int numVertices = medianDegreeList.size();
    // Even number of vertices
    if (numVertices %2 == 0) {
      int medianIdx1 = numVertices/2;
      int medianIdx2 = medianIdx1 - 1;
      median = ((float)medianDegreeList.get(medianIdx1).getDegree() +
                (float)medianDegreeList.get(medianIdx2).getDegree())/2;
    } else {
      int medianIdx = numVertices/2;
      median = (float)medianDegreeList.get(medianIdx).getDegree();
    }
    return median;
  }
  
  // Private method to add an edge to the graph from 'vertexFrom' to 'vertexTo'
  private void addEdgeToGraph(String vertexFrom, String vertexTo, long createdTime) {
    
    ArrayList<GraphEdge> edgeList;
    if ((edgeList = edgeMap.get(vertexFrom)) != null) {
      // vertexFrom already exists in the graph. 
      // If edge to vertexTo already exists between the two, just update time.
      // If edge to vertexTo does not exist, create a new edge.
      for (GraphEdge edge: edgeList) {
        if (edge.getTarget().equals(vertexTo)) {
          edge.setCreatedTime(createdTime);
          return;
        }
      }
        GraphEdge edge = new GraphEdge(vertexTo, createdTime);
      edgeList.add(edge);
    } else {
      // VertexFrom does not exist. Create New.
      edgeList = new ArrayList<GraphEdge>();
      GraphEdge edge = new GraphEdge(vertexTo, createdTime);
      edgeList.add(edge);
      edgeMap.put(vertexFrom, edgeList);
    }
  }
  
  // Private method to delete an edge in the graph from 'vertexFrom' to 'vertexTo'
  private void DeleteEdgeFromGraph(String vertexFrom, String vertexTo, long createdTime, 
                   HashMap<String, Integer> modifiedVertexMap) {
    ArrayList<GraphEdge> edgeList;
    if ((edgeList = edgeMap.get(vertexFrom)) != null) {
      Iterator<GraphEdge> iter = edgeList.iterator();
      while (iter.hasNext()) {
        GraphEdge edge = iter.next();
        if (edge.getTarget().equals(vertexTo)) {
          if (createdTime == edge.getCreatedTime()) {
            // delete entry from hashMap 
            iter.remove();
            modifiedVertexMap.put(vertexFrom, edgeList.size());
          }
        }
      }
      if (edgeList.size() == 0) {
        // If vertex has no edges remove vertex from graph.
        edgeMap.remove(vertexFrom);
      }
    }
  }
  
  // Private method to update medianDegreeList with the new degree for the vertex.
  // vertex.getDegree() of zero means that the vertex is removed from the graph and 
  // thereby needs to be removed from the median list too.
  private void updateMedianDegreeList(VertexDegree vertex) {
    // Check if this vertex exists in the median degree list, remove the vertex with
    // the old degree and add it back at the right position based on the new degree.
    for (int removeIdx = 0; removeIdx < medianDegreeList.size(); removeIdx++) {
      VertexDegree temp = medianDegreeList.get(removeIdx); 
      if (temp.getActor().equals(vertex.getActor())) {
        // Transaction exists, remove the vertex.
        medianDegreeList.remove(temp);
        break;
      }
    }

    // This vertex does not needed to be added as it has zero degrees. Just return.
    if (vertex.getDegree() == 0)
      return;
    
    /* Adding back the vertex */
    
    for (int i=0; i < medianDegreeList.size(); i++) {
      VertexDegree temp = medianDegreeList.get(i); 
      if (temp.getDegree() >= vertex.getDegree()) {
        medianDegreeList.add(i, vertex);
        return;
      }
    }
    medianDegreeList.add(medianDegreeList.size(), vertex);
  }
  
  void printVenmo() {
    System.out.println();
    printGraph();
    printPriorityQueue();
    printMedianArray();
  }
  
  void printPriorityQueue() {
    System.out.println("PrintPriorityQueue: Size = " + createdPriorityQueue.size());    
  }
  
  void printGraph() {
    System.out.println("PrintGraph: Size = " + edgeMap.size()); 
    for (Map.Entry me : edgeMap.entrySet()) {
      System.out.println("Key: "+ me.getKey() + ", Value: " + me.getValue());
    }
  }
  
  void printMedianArray() {
    System.out.println("PrintMedianArray: Size = " + medianDegreeList.size());
    for (VertexDegree g: medianDegreeList) {
      System.out.println(g);
    }
  }
  
  public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
    
    if (args.length < 2) {
      System.out.println("Not enough arguments. Please provide input file and output file");
      return;
    }

    FileWriter outputFile = new FileWriter(args[1], false);
    PrintWriter printLine = new PrintWriter(outputFile);

    BufferedReader in = new BufferedReader(new FileReader(args[0]));
    String line;

    Venmo venmo = new Venmo();
    while((line = in.readLine()) != null) {
      JSONParser parser = new JSONParser();
      JSONObject obj = (JSONObject) parser.parse(line);
      
      String created_time = (String)obj.get("created_time");
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      Date ts = fmt.parse(created_time);
      long time = ts.getTime();
      String target = (String)obj.get("target");
      String actor = (String)obj.get("actor");
    
      venmo.insertTransaction(new Transaction(ts.getTime(), actor, target));
      printLine.printf("%.2f\n",venmo.getMedian());
    }
    in.close();
    printLine.close();
  }
}
