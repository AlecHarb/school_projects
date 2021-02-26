/* Alec Harb
   CSCI 241
   3/12/2019
   Purpose: The purpose of this document is to implement Dijkstra's shortest path algorithm in order to get
   the shortest path from the origin to every reachable node in the graph from the origin.
*/
package graph;

import heap.Heap;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;

/** Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm.
 * Sample usage:
 *   Graph g = // create your graph
 *   ShortestPaths sp = new ShortestPaths();
 *   Node a = g.getNode("A");
 *   sp.compute(a);
 *   Node b = g.getNode("B");
 *   LinkedList<Node> abPath = sp.getShortestPath(b);
 *   double abPathLength = sp.getShortestPathLength(b);
 *   */
public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node,PathData> paths;

    /** Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the
     * backpointer to the previous node on the shortest path.
     * Precondition: origin is a node in the Graph.*/
    public void compute(Node origin) {
      paths = new HashMap<Node,PathData>();
      PathData data = new PathData(0.0, null);
      paths.put(origin, data);

      // Initialize frontier to the start node
      Heap<Node,Double> frontier = new Heap<Node,Double>();
      frontier.add(origin, 0.0);
      Node v = origin;

      //While frontier is not empty
      while (frontier.size() > 0) {

        //Remove node with smallest priority from the frontier set into the settled set (nonexistent)
        Node f = frontier.poll();

        //For each neighbor, w, of f, if w is not in settled or frontier sets...
        HashMap<Node, Double> neighbors = f.getNeighbors();

          for (Node i : neighbors.keySet()) {
            Double dist = paths.get(f).distance + neighbors.get(i);
            //If w is not in s and frontier, w's distance = f's distance + (weight from f to w)
            // w's backpointer is f, add w to f
            if (paths.get(i) == null && !frontier.contains(i)) {
              PathData newDat = new PathData(dist, f);
              paths.put(i, newDat);
              frontier.add(i, dist);

              // do the same as the comments above, but dont add w to f
            } else if (dist < paths.get(i).distance){
              PathData newDat = new PathData(dist, f);
              paths.put(i, newDat);
            }
          }
      }
    }

    /** Returns the length of the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public double shortestPathLength(Node destination) {
        //while previous node is not null (not at origin), add distance of node to its previous node

        if (paths.get(destination) == null) {
          return Double.POSITIVE_INFINITY;
        }

        return paths.get(destination).distance;
    }

    /** Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public LinkedList<Node> shortestPath(Node destination) {
        LinkedList<Node> list = new LinkedList<>();

        //if destination is not in hashmap, it was never reached by the origin
        if (paths.get(destination) == null) {
          System.out.println("No path to origin.");
          return null;
        }
        //make a list of nodes from destination to origin
        while (paths.get(destination) != null) {
          list.addFirst(destination);
          destination = paths.get(destination).previous;
        }
        return list;
    }


    /** Inner class representing data used by Dijkstra's algorithm in the
     * process of computing shortest paths from a given source node. */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source
        int hops;
        Node bfsPrev;

        /** constructor: initialize distance and previous node */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
            hops = -1;
        }
    }


    /** Static helper method to open and parse a file containing graph
     * information. Can parse either a basic file or a DB1B CSV file with
     * flight data. See GraphParser, BasicParser, and DB1BParser for more.*/
    protected static Graph parseGraph(String fileType, String fileName) throws
        FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db1b")) {
            parser = new DB1BParser();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }

    public static void main(String[] args) {
      // read command line args
      String fileType = args[0];
      String fileName = args[1];
      String origCode = args[2];

      String destCode = null;
      if (args.length == 4) {
          destCode = args[3];
      }

      // parse a graph with the given type and filename
      Graph graph;
      try {
          graph = parseGraph(fileType, fileName);
      } catch (FileNotFoundException e) {
          System.out.println("Could not open file " + fileName);
          return;
      }
      graph.report();


      // TODO 4: create a ShortestPaths object, use it to compute shortest
      // paths data from the origin node given by origCode.

      ShortestPaths newPath = new ShortestPaths();
      Node origin = graph.getNode(origCode);
      Node dest = graph.getNode(destCode);
      newPath.compute(origin);

      graph.getNodes();

      // TODO 5:
      // If destCode was not given, print each reachable node followed by the
      // length of the shortest path to it from the origin.

      // TODO 6:
      // If destCode was given, print the nodes in the path from
      // origCode to destCode, followed by the total path length
      // If no path exists, print a message saying so.

      if (destCode == null) {
        System.out.println("Shortest paths from " + origCode + ":");
        LinkedList<Node> nodeList = new LinkedList<>();
        for (Node i : newPath.paths.keySet()) {
          nodeList.add(i);
        }

        //prints all nodes and distances from the origin
        while (!nodeList.isEmpty()) {
          Node n = nodeList.remove();
          System.out.println(n + ": " + newPath.shortestPathLength(n));
        }
      } else {
          //get shortest path distance
          //get shortest path as a linked list
          Double distance = newPath.shortestPathLength(dest);
          LinkedList<Node> nodeList = newPath.shortestPath(dest);

          //if there is no path from destination to origin, exit status 0
          if (nodeList.peekFirst() != origin) {
            System.out.println("No path exists from the destination to the origin.");
            System.exit(0);
          // else, print shortest path and distance
          } else {
            for (Node i : nodeList) {
              System.out.print(i + " ");
            }
            System.out.println(distance);
          }
      }
    }
}
